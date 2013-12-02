package com.psg.jgringotts.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.psg.jgringotts.JGringotts;

public class RootFrameCreateItemAction extends AbstractAction {

    private static final long serialVersionUID = 5053709054460811206L;

    private JGringotts desktop;

    public RootFrameCreateItemAction(JGringotts desktop) {
        super("Create");
        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        desktop.createItemDialogNameTextField.setText("");
        desktop.createItemDialog.setVisible(true);
    }

}
