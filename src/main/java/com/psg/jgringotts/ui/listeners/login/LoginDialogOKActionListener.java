package com.psg.jgringotts.ui.listeners.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.psg.jgringotts.JGringotts;
import com.psg.jgringotts.dao.ItemDAO;
import com.psg.jgringotts.dao.JGringottsDAOException;
import com.psg.jgringotts.dao.JGringottsDAOManager;
import com.psg.jgringotts.dao.model.Item;

public class LoginDialogOKActionListener implements ActionListener {

    private JGringotts desktop;

    public LoginDialogOKActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {

        String username = desktop.loginDialogUsernameTextField.getText();
        String password = new String(desktop.loginDialogPasswordPasswordField.getPassword());
        String bootPassword = new String(desktop.loginDialogBootPasswordPasswordField.getPassword());

        JGringottsDAOManager daoMgr = JGringottsDAOManager.getInstance(username, password, bootPassword);
        desktop.setJgringottsDAOBean(daoMgr.getDaoBean());

        try {
            ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
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

        desktop.loginDialog.setVisible(false);
        desktop.rootFrame.setVisible(true);
    }
}
