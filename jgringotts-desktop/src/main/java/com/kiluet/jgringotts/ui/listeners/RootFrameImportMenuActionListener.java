package com.kiluet.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;

import com.kiluet.jgringotts.JGringotts;
import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class RootFrameImportMenuActionListener implements ActionListener {

    private JGringotts desktop;

    public RootFrameImportMenuActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {
        this.desktop.rootFrameImportFileChooser.setMultiSelectionEnabled(true);
        this.desktop.rootFrameImportFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = this.desktop.rootFrameImportFileChooser.showDialog(desktop.rootFrame, "Import");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] fileArray = this.desktop.rootFrameImportFileChooser.getSelectedFiles();
            ItemDAO itemDAO = this.desktop.getJgringottsDAOBean().getItemDAO();
            for (File file : fileArray) {
                try {
                    Item item = new Item();
                    item.setValue(file.getName());
                    item.setDescription(FileUtils.readFileToString(file));
                    itemDAO.save(item);
                } catch (JGringottsDAOException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
