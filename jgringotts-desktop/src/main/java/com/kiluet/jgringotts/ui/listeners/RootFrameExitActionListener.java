package com.kiluet.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.kiluet.jgringotts.JGringotts;

public class RootFrameExitActionListener implements ActionListener {

    private JGringotts jgringotts;

    public RootFrameExitActionListener(JGringotts jgringotts) {
        super();
        this.jgringotts = jgringotts;
    }

    public void actionPerformed(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(this.jgringotts.rootFrame, this.jgringotts.msgConfirmExit,
                UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}
