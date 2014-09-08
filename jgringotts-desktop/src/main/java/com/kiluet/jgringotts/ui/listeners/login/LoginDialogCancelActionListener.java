package com.kiluet.jgringotts.ui.listeners.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kiluet.jgringotts.JGringotts;

public class LoginDialogCancelActionListener implements ActionListener {

    private JGringotts desktop;

    public LoginDialogCancelActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {
        if (desktop.loginDialog != null) {
            desktop.loginDialog.setVisible(false);
            System.exit(0);
        }
    }

}
