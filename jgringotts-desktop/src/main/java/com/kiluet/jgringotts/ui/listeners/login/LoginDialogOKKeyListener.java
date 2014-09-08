package com.kiluet.jgringotts.ui.listeners.login;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.kiluet.jgringotts.JGringotts;

public class LoginDialogOKKeyListener implements KeyListener {

    private JGringotts desktop;

    public LoginDialogOKKeyListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            desktop.loginDialogOKActionListener.actionPerformed(null);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {

    }

}
