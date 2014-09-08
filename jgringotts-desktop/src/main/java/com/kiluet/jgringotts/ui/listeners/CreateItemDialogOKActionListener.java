package com.kiluet.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class CreateItemDialogOKActionListener implements ActionListener {

    private JGringotts desktop;

    public CreateItemDialogOKActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {
        ItemDAO itemDAO = desktop.getJgringottsDAOBean().getItemDAO();

        try {
            Item item = new Item();
            item.setName(desktop.createItemDialogNameTextField.getText());
            itemDAO.save(item);
        } catch (JGringottsDAOException e1) {
            e1.printStackTrace();
        }

        desktop.rootFrameItemListModel.clear();

        try {
            List<Item> itemList = itemDAO.findAll();
            List<Item> newItemList = new ArrayList<Item>(itemList);
            Collections.sort(newItemList, (Item o1, Item o2) -> o1.getName().compareTo(o2.getName()));
            newItemList.forEach(u -> desktop.rootFrameItemListModel.addElement(u.getName()));
        } catch (JGringottsDAOException e1) {
            e1.printStackTrace();
        }

        desktop.createItemDialog.setVisible(false);
    }

}
