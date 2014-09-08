package com.kiluet.jgringotts.ui.actions;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class RootFrameSaveItemAction extends AbstractAction {

    private static final long serialVersionUID = 5053709054460811206L;

    private JGringotts desktop;

    public RootFrameSaveItemAction(JGringotts desktop) {
        super("Save");
        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        String key = this.desktop.rootFrameItemList.getSelectedValue();

        try {
            ItemDAO itemDAO = desktop.getJgringottsDAOBean().getItemDAO();
            Item item = itemDAO.findByName(key);
            item.setDescription(this.desktop.rootFrameItemDescriptionTextarea.getText());
            item.setModified(new Date());
            itemDAO.save(item);
        } catch (JGringottsDAOException e1) {
            e1.printStackTrace();
        }
    }

}
