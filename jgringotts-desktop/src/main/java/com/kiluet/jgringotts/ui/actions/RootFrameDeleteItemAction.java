package com.kiluet.jgringotts.ui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class RootFrameDeleteItemAction extends AbstractAction {

    private static final long serialVersionUID = 5053709054460811206L;

    private JGringotts desktop;

    public RootFrameDeleteItemAction(JGringotts desktop) {
        super("Delete");
        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        int idx = this.desktop.rootFrameItemList.getSelectedIndex();
        String key = this.desktop.rootFrameItemList.getSelectedValue();

        int response = JOptionPane.showConfirmDialog(this.desktop.rootFrame, "Are you sure you want to delete '" + key
                + "'?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            ItemDAO itemDAO = this.desktop.getJgringottsDAOBean().getItemDAO();
            try {
                Item item = itemDAO.findByValue(key);
                itemDAO.delete(item);
            } catch (JGringottsDAOException e1) {
                e1.printStackTrace();
            }

            desktop.rootFrameItemListModel.clear();

            try {
                List<Item> itemList = itemDAO.findAll();
                List<Item> newItemList = new ArrayList<Item>(itemList);

                Collections.sort(newItemList, (Item a, Item b) -> a.getValue().compareTo(b.getValue()));

                for (Item item : newItemList) {
                    desktop.rootFrameItemListModel.addElement(item.getValue());
                }
            } catch (JGringottsDAOException e1) {
                e1.printStackTrace();
            }

        }

    }
}
