package com.kiluet.jgringotts.ui.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.kiluet.jgringotts.JGringotts;

public class RootFrameItemListMouseListener extends MouseAdapter {

    private JGringotts desktop;

    public RootFrameItemListMouseListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.isPopupTrigger()) {
            doPopupStuff(me);
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.isPopupTrigger()) {
            doPopupStuff(me);
        }
    }

    private void doPopupStuff(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        desktop.rootFrameItemList.setSelectedIndex(desktop.rootFrameItemList.locationToIndex(p));
        desktop.rootFrameItemListPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

}
