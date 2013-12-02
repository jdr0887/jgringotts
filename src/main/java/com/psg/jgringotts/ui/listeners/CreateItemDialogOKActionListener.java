package com.psg.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.psg.jgringotts.JGringotts;
import com.psg.jgringotts.dao.ItemDAO;
import com.psg.jgringotts.dao.JGringottsDAOException;
import com.psg.jgringotts.dao.model.Item;

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
            Collections.sort(newItemList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (Item item : newItemList) {
                desktop.rootFrameItemListModel.addElement(item.getName());
            }
        } catch (JGringottsDAOException e1) {
            e1.printStackTrace();
        }

        desktop.createItemDialog.setVisible(false);
    }

}
