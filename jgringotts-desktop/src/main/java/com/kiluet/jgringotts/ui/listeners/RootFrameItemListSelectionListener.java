package com.kiluet.jgringotts.ui.listeners;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class RootFrameItemListSelectionListener implements ListSelectionListener {

    private JGringotts desktop;

    public RootFrameItemListSelectionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            String key = desktop.rootFrameItemList.getSelectedValue();
            try {
                Item item = desktop.getJgringottsDAOBean().getItemDAO().findByName(key);
                if (item != null && StringUtils.isNotEmpty(item.getDescription())) {
                    this.desktop.rootFrameItemDescriptionTextarea.setText(item.getDescription());
                } else {
                    this.desktop.rootFrameItemDescriptionTextarea.setText("");
                }
            } catch (JGringottsDAOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
