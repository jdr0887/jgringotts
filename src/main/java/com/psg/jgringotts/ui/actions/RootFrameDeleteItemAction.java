package com.psg.jgringotts.ui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.psg.jgringotts.JGringotts;
import com.psg.jgringotts.dao.ItemDAO;
import com.psg.jgringotts.dao.JGringottsDAOException;
import com.psg.jgringotts.dao.model.Item;

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
                Item item = itemDAO.findByName(key);
                itemDAO.delete(item);
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

        }

    }

}
