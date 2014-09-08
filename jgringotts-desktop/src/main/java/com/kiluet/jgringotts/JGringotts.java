package com.kiluet.jgringotts;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionListener;

import com.kiluet.jgringotts.dao.JGringottsDAOBean;
import com.kiluet.jgringotts.ui.actions.RootFrameCreateItemAction;
import com.kiluet.jgringotts.ui.actions.RootFrameDeleteItemAction;
import com.kiluet.jgringotts.ui.actions.RootFrameSaveItemAction;
import com.kiluet.jgringotts.ui.listeners.CreateItemDialogCancelActionListener;
import com.kiluet.jgringotts.ui.listeners.CreateItemDialogOKActionListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameAboutMenuActionListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameExitActionListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameExportMenuActionListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameImportMenuActionListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameItemListMouseListener;
import com.kiluet.jgringotts.ui.listeners.RootFrameItemListSelectionListener;
import com.kiluet.jgringotts.ui.listeners.about.AboutDialogOKActionListener;
import com.kiluet.jgringotts.ui.listeners.login.LoginDialogCancelActionListener;
import com.kiluet.jgringotts.ui.listeners.login.LoginDialogOKActionListener;
import com.kiluet.jgringotts.ui.listeners.login.LoginDialogOKKeyListener;

import cookxml.cookswing.CookSwing;

//import cookxml.cookswing.CookSwing;

public class JGringotts {

    private static final String[] SYSTEM_PROPERTY_KEYS = { "java.version", "java.vender", "java.vendor.url",
            "java.home", "java.vm.specification.version", "java.vm.specification.vendor", "java.vm.specification.name",
            "java.vm.version", "java.vm.vendor", "java.vm.name", "java.specification.version",
            "java.specification.vendor", "java.specification.name", "java.class.version", "java.class.path",
            "java.library.path", "java.io.tmpdir", "java.compiler", "java.ext.dirs", "os.name", "os.arch",
            "os.version", "file.separator", "path.separator", "line.separator", "user.name", "user.home", "user.dir" };

    protected static final Map readSystemProperties() {
        try {
            return System.getProperties();
        } catch (Exception ex) {
            // there is a security manager, so we can try to get it using
            // getProperty instead
            Map<String, String> properties = new HashMap<String, String>();
            String[] propertyKeys = SYSTEM_PROPERTY_KEYS;
            for (int i = 0; i < propertyKeys.length; ++i) {
                try {
                    String key = propertyKeys[i];
                    String value = System.getProperty(key);
                    if (value == null)
                        continue;
                    properties.put(key, value);
                } catch (Exception ex2) {
                }
            }
            return properties;
        }
    }

    public final ListSelectionListener rootFrameItemListSelectionListener = new RootFrameItemListSelectionListener(this);

    public final MouseListener rootFrameItemListMouseListener = new RootFrameItemListMouseListener(this);

    public final KeyListener loginDialogOKKeyListener = new LoginDialogOKKeyListener(this);

    public final ActionListener loginDialogOKActionListener = new LoginDialogOKActionListener(this);

    public final ActionListener loginDialogCancelActionListener = new LoginDialogCancelActionListener(this);

    public final ActionListener aboutDialogOKActionListener = new AboutDialogOKActionListener(this);

    public final ActionListener rootFrameExitActionListener = new RootFrameExitActionListener(this);

    public final ActionListener rootFrameAboutMenuActionListener = new RootFrameAboutMenuActionListener(this);

    public final ActionListener createItemDialogOKActionListener = new CreateItemDialogOKActionListener(this);

    public final ActionListener createItemDialogCancelActionListener = new CreateItemDialogCancelActionListener(this);

    public final ActionListener rootFrameImportMenuActionListener = new RootFrameImportMenuActionListener(this);

    public final ActionListener rootFrameExportMenuActionListener = new RootFrameExportMenuActionListener(this);

    public Action rootFrameCreateItemAction = new RootFrameCreateItemAction(this);

    public Action rootFrameDeleteItemAction = new RootFrameDeleteItemAction(this);

    public Action rootFrameSaveItemAction = new RootFrameSaveItemAction(this);

    public Preferences preferences = Preferences.userRoot();

    public Map systemProperties = readSystemProperties();

    public String msgConfirmExit, msgCopyright;

    public JDialog loginDialog, aboutDialog, createItemDialog;

    public JFrame rootFrame;

    public JFileChooser rootFrameImportFileChooser, rootFrameExportFileChooser;

    public JPasswordField loginDialogPasswordPasswordField, loginDialogBootPasswordPasswordField;

    public JTextField loginDialogUsernameTextField, createItemDialogNameTextField;

    public JScrollPane rootFrameItemDescriptionScrollPane;

    public JTextArea rootFrameItemDescriptionTextarea;

    public JSplitPane rootFrameSplitPane;

    public JList<String> rootFrameItemList;

    public DefaultListModel<String> rootFrameItemListModel;

    public JPopupMenu rootFrameItemListPopupMenu;

    public JButton registerDialogOKButton, registerDialogCancelButton, loginDialogRegisterButton, loginDialogOKButton,
            loginDialogCancelButton, createItemDialogOKButton, createItemDialogCancelButton;

    private JGringottsDAOBean jgringottsDAOBean;

    public JGringotts() {
        CookSwing cookSwing = new CookSwing(this);
        this.rootFrame = (JFrame) cookSwing.render("com/kiluet/jgringotts/ui/mainFrame.xml");
        init();
    }

    private void init() {
        String username = System.getProperty("user.name");
        loginDialogUsernameTextField.setText(username);
        this.rootFrameItemListPopupMenu.add(rootFrameCreateItemAction);
        this.rootFrameItemListPopupMenu.add(rootFrameDeleteItemAction);
        this.rootFrameItemList.add(this.rootFrameItemListPopupMenu);
        this.loginDialog.setVisible(true);
        this.rootFrameSplitPane.setDividerLocation(0.35);
        this.rootFrameItemDescriptionTextarea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK), "saveAction");
        this.rootFrameItemDescriptionTextarea.getActionMap().put("saveAction", rootFrameSaveItemAction);
    }

    public JGringottsDAOBean getJgringottsDAOBean() {
        return jgringottsDAOBean;
    }

    public void setJgringottsDAOBean(JGringottsDAOBean jgringottsDAOBean) {
        this.jgringottsDAOBean = jgringottsDAOBean;
    }

    public static void main(String[] args) {
        new JGringotts();
    }
}
