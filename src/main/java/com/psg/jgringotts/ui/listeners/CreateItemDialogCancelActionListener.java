package com.psg.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.psg.jgringotts.JGringotts;

public class CreateItemDialogCancelActionListener implements ActionListener {

    private JGringotts desktop;

    public CreateItemDialogCancelActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {
        desktop.createItemDialog.setVisible(false);
    }

}
