package com.kiluet.jgringotts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private ListView<String> itemListView;

    @FXML
    private TextArea itemContentTextArea;

    @FXML
    private Label dateLabel;

    private final ContextMenu contextMenu = new ContextMenu();

    private final ObservableList<String> observableList = FXCollections.observableArrayList();

    private JGringotts app;

    public MainController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.setOnAction(addListItemHandler);
        contextMenu.getItems().add(addMenuItem);

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(deleteListItemHandler);
        contextMenu.getItems().add(deleteMenuItem);

        itemListView.setCellFactory(TextFieldListCell.forListView());
        itemListView.setItems(observableList);
        itemListView.addEventHandler(MouseEvent.MOUSE_CLICKED, showContextMenuHandler);

        itemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                logger.debug("ENTERING changed(ObservableValue<? extends String>, String, String)");
                ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
                try {
                    String name = itemListView.getSelectionModel().getSelectedItem();
                    if (StringUtils.isNotEmpty(name)) {
                        Item item = itemDAO.findByValue(name);
                        if (item != null && StringUtils.isNotEmpty(item.getDescription())) {
                            itemContentTextArea.setText(item.getDescription());
                        } else {
                            itemContentTextArea.setText("");
                        }
                    }
                } catch (JGringottsDAOException e) {
                    e.printStackTrace();
                }

            }
        });

        dateLabel.setText(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
    }

    @FXML
    private void showAbout(final ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("JGringotts :: About");
        alert.setHeaderText("About JGringotts");
        alert.setContentText(String.format("Version: %s", app.getVersion()));
        alert.showAndWait();
    }

    @FXML
    private void onEditCommit(ListView.EditEvent<String> event) {
        logger.debug("ENTERING onEditCommit(ListView.EditEvent<String>)");

        try {
            ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
            Item item = itemDAO.findByValue(itemListView.getSelectionModel().getSelectedItem());
            String newValue = event.getNewValue();
            item.setValue(newValue);
            itemDAO.save(item);
            itemListView.getItems().set(event.getIndex(), event.getNewValue());

            if (observableList.size() > 1) {
                FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
            } else {
                itemListView.getSelectionModel().select(0);
            }
            itemContentTextArea.setText(item.getDescription());
        } catch (JGringottsDAOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void doNew(final ActionEvent event) {

        try {
            ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
            Item item = new Item("New");
            item.setId(itemDAO.save(item));
            observableList.add(0, item.getValue());
            itemListView.getSelectionModel().clearAndSelect(0);
            FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
            itemContentTextArea.setText("");
        } catch (JGringottsDAOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void doExport(final ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setTitle("Export Items");
        File directory = chooser.showDialog(app.getStage());
        ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
        try {
            List<Item> itemList = itemDAO.findAll();
            for (Item item : itemList) {
                File f = new File(directory, item.getValue());
                FileUtils.writeStringToFile(f, item.getDescription());
            }
        } catch (JGringottsDAOException | IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    public void doImport(final ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setTitle("Import Items");
        List<File> fileList = chooser.showOpenMultipleDialog(app.getStage());
        ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
        if (fileList != null && !fileList.isEmpty()) {
            for (File file : fileList) {
                try {
                    Item item = new Item();
                    item.setValue(file.getName());
                    item.setDescription(FileUtils.readFileToString(file));
                    itemDAO.save(item);
                    observableList.add(item.getValue());
                } catch (IOException | JGringottsDAOException e) {
                    e.printStackTrace();
                }
            }
            FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
            itemListView.setItems(observableList);
        }
    }

    @FXML
    public void doExit(final ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void itemContentTextAreaKeyHandler(KeyEvent event) {
        logger.info("ENTERING itemContentTextAreaKeyHandler(KeyEvent)");
        try {
            ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
            Item item = itemDAO.findByValue(itemListView.getSelectionModel().getSelectedItem());
            if (item != null) {
                item.setDescription(itemContentTextArea.getText());
                itemDAO.save(item);
            }
        } catch (JGringottsDAOException e) {
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> deleteListItemHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            logger.debug("ENTERING handle(ActionEvent)");
            ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
            try {
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name)) {
                    Item item = itemDAO.findByValue(name);
                    itemDAO.delete(item);
                    observableList.remove(name);
                    if (!itemListView.getItems().isEmpty()) {
                        FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
                        item = itemDAO.findByValue(name);
                        if (item != null && StringUtils.isNotEmpty(item.getDescription())) {
                            itemContentTextArea.setText(item.getDescription());
                        }
                    } else {
                        itemContentTextArea.setText("");
                    }
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }

    };

    private EventHandler<ActionEvent> addListItemHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            contextMenu.hide();
            doNew(event);
        }

    };

    private EventHandler<MouseEvent> showContextMenuHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(itemListView, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        }

    };

    @FXML
    private void itemListViewSelectionHandler(MouseEvent event) {
        logger.debug("ENTERING itemListViewSelectionHandler(MouseEvent)");
        if (event.isPopupTrigger()) {
            try {
                ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name)) {
                    Item item = itemDAO.findByValue(name);
                    if (item != null && StringUtils.isNotEmpty(item.getDescription())) {
                        itemContentTextArea.setText(item.getDescription());
                    } else {
                        itemContentTextArea.setText("");
                    }
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }
    }

    public ListChangeListener<String> itemListViewChangeHandler = new ListChangeListener<String>() {

        @Override
        public void onChanged(Change<? extends String> event) {
            logger.debug("ENTERING onChanged(Change<? extends String>)");
            try {
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name) && !observableList.isEmpty()) {
                    ItemDAO itemDAO = app.getDaoMgr().getDaoBean().getItemDAO();
                    Item item = itemDAO.findByValue(name);
                    if (item != null) {
                        itemContentTextArea.setText(item.getDescription());
                    }
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }

    };

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
                showAbout(null);
            }
        }
    }

    public JGringotts getApp() {
        return app;
    }

    public void setApp(JGringotts app) {
        this.app = app;
    }

    public ListView<String> getItemListView() {
        return itemListView;
    }

    public void setItemListView(ListView<String> itemListView) {
        this.itemListView = itemListView;
    }

    public TextArea getItemContentTextArea() {
        return itemContentTextArea;
    }

    public void setItemContentTextArea(TextArea itemContentTextArea) {
        this.itemContentTextArea = itemContentTextArea;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public ObservableList<String> getObservableList() {
        return observableList;
    }

}
