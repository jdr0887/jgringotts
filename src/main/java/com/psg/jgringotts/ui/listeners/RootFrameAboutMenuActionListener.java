package com.psg.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.psg.jgringotts.JGringotts;

public class RootFrameAboutMenuActionListener implements ActionListener {

    private JGringotts jGringotts;

    public RootFrameAboutMenuActionListener(JGringotts jGringotts) {
        super();
        this.jGringotts = jGringotts;
    }

    public void actionPerformed(ActionEvent e) {
        this.jGringotts.aboutDialog.setVisible(true);
    }

}
