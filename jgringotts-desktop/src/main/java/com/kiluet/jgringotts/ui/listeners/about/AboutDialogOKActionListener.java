package com.kiluet.jgringotts.ui.listeners.about;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kiluet.jgringotts.JGringotts;

public class AboutDialogOKActionListener implements ActionListener {

    private JGringotts jGringotts;

    public AboutDialogOKActionListener(JGringotts jGringotts) {
        super();
        this.jGringotts = jGringotts;
    }

    public void actionPerformed(ActionEvent e) {
        this.jGringotts.aboutDialog.setVisible(false);
    }

}
