package com.psg.jgringotts.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;

import com.psg.jgringotts.JGringotts;
import com.psg.jgringotts.dao.ItemDAO;
import com.psg.jgringotts.dao.JGringottsDAOException;
import com.psg.jgringotts.dao.model.Item;

public class RootFrameExportMenuActionListener implements ActionListener {

    private JGringotts desktop;

    public RootFrameExportMenuActionListener(JGringotts desktop) {
        super();
        this.desktop = desktop;
    }

    public void actionPerformed(ActionEvent e) {
        this.desktop.rootFrameExportFileChooser.setMultiSelectionEnabled(false);
        this.desktop.rootFrameExportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = this.desktop.rootFrameExportFileChooser.showDialog(desktop.rootFrame, "Export");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File directory = this.desktop.rootFrameExportFileChooser.getSelectedFile();
            ItemDAO itemDAO = this.desktop.getJgringottsDAOBean().getItemDAO();
            try {
                List<Item> itemList = itemDAO.findAll();
                for (Item item : itemList) {
                    File f = new File(directory, item.getName());
                    FileUtils.writeStringToFile(f, item.getDescription());
                }
            } catch (JGringottsDAOException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
