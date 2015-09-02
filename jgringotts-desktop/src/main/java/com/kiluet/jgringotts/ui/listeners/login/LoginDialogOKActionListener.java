package com.kiluet.jgringotts.ui.listeners.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.JGringottsDAOManager;
import com.kiluet.jgringotts.dao.model.Item;

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

        JGringottsDAOManager daoMgr;
        try {
            daoMgr = JGringottsDAOManager.getInstance(username, password, bootPassword);
            desktop.setJgringottsDAOBean(daoMgr.getDaoBean());
            ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
            List<Item> itemList = itemDAO.findAll();
            List<Item> newItemList = new ArrayList<Item>(itemList);
            Collections.sort(newItemList, (Item o1, Item o2) -> o1.getValue().compareTo(o2.getValue()));
            newItemList.forEach(u -> desktop.rootFrameItemListModel.addElement(u.getValue()));
        } catch (JGringottsDAOException e2) {
            e2.printStackTrace();
        }

        desktop.loginDialog.setVisible(false);
        desktop.rootFrame.setVisible(true);
    }
}
