package com.psg.jgringotts.ui.listeners.about;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.psg.jgringotts.JGringotts;

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
