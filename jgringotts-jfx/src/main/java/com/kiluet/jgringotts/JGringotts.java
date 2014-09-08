package com.kiluet.jgringotts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.JGringottsDAOManager;
import com.kiluet.jgringotts.dao.model.Item;

public class JGringotts extends Application {

    private final Logger logger = LoggerFactory.getLogger(JGringotts.class);

    private final KeyCombination saveKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    private Stage stage;

    private BorderPane rootLayout;

    private final List<Integer> countList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private final List<Button> pinButtonList = Arrays.asList(new Button(), new Button(), new Button(), new Button(),
            new Button(), new Button(), new Button(), new Button(), new Button());

    private ListView<String> itemListView = new ListView<String>();

    private final ContextMenu deleteContextMenu = new ContextMenu();

    private TextArea contentArea = new TextArea();

    private ObservableList<String> observableList = FXCollections.observableArrayList();

    private final Button loginButton = new Button("Login");

    private final TextField usernameTextField = new TextField();

    private final PasswordField passwordField = new PasswordField();

    private final PasswordField pinField = new PasswordField();

    private JGringottsDAOManager daoMgr;

    public JGringotts() {
        super();
    }

    public String getVersion() {
        ResourceBundle bundle = ResourceBundle.getBundle("com/kiluet/jgringotts/jgringotts");
        String version = bundle.getString("version");
        return StringUtils.isNotEmpty(version) ? version : "0.0.1-SNAPSHOT";
    }

    @Override
    public void start(final Stage stage) throws Exception {

        this.stage = stage;
        this.stage.setTitle("JGringotts");

        try {
            GridPane page = buildLoginPane();
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.sizeToScene();

            stage.show();

        } catch (Exception ex) {
            logger.error("Error", ex);
        }
    }

    public void createNew(String newItem) {
        ItemDAO itemDAO = getDaoMgr().getDaoBean().getItemDAO();
        Item item = new Item();
        item.setName(newItem);
        try {
            itemDAO.save(item);
            observableList.add(newItem);
            FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
            contentArea.setText("");
        } catch (JGringottsDAOException e) {
            e.printStackTrace();
        }
    }

    private GridPane buildLoginPane() {

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPrefSize(480, 260);
        pane.setHgap(12);
        pane.setVgap(12);

        pane.setPadding(new Insets(10, 10, 10, 10));

        pane.add(new Text("Welcome to JGringotts"), 0, 0, 2, 1);

        pane.add(new Label("User Name:"), 0, 1);
        usernameTextField.setText(System.getProperty("user.name"));
        pane.add(usernameTextField, 1, 1);

        pane.add(new Label("Password:"), 0, 2);
        pane.add(passwordField, 1, 2);

        pane.add(new Label("PIN:"), 0, 3);
        pane.add(pinField, 1, 3);

        int col = 3;
        int row = 1;

        Collections.shuffle(countList);

        for (int i = 0; i < countList.size(); ++i) {

            final String pinNumber = countList.get(i).toString();
            Button button = pinButtonList.get(i);
            button.setText(pinNumber);
            button.setOnAction(e -> {
                pinField.appendText(((Button) e.getSource()).getText());
                redrawPINButtons();
            });

            pane.add(button, ++col, row);
            if ((i + 1) % 3 == 0) {
                col = 3;
                ++row;
            }
        }

        HBox box = new HBox();
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(loginButton);
        pane.add(box, 1, 4);
        loginButton.setOnAction(loginEvenHandler);
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(deleteListItemHandler);
        deleteContextMenu.getItems().add(deleteMenuItem);
        itemListView.addEventHandler(MouseEvent.MOUSE_CLICKED, showDeleteContextMenuHandler);
        itemListView.setOnMouseClicked(itemListViewSelectionHandler);
        observableList.addListener(itemListViewChangeHandler);
        contentArea.setOnKeyPressed(contentAreaKeyHandler);
        pinField.addEventFilter(KeyEvent.KEY_TYPED, u -> u.consume());
        return pane;
    }

    private void redrawPINButtons() {

        Collections.shuffle(countList);

        for (int i = 0; i < countList.size(); ++i) {
            final String pinNumber = countList.get(i).toString();
            Button button = pinButtonList.get(i);
            button.setText(pinNumber);
        }

    }

    private EventHandler<KeyEvent> contentAreaKeyHandler = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {
            if (event.isControlDown()) {
                boolean matched = saveKeyCombination.match(event);
                if (matched) {
                    ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
                    try {
                        Item item = itemDAO.findByName(itemListView.getSelectionModel().getSelectedItem());
                        item.setDescription(contentArea.getText());
                        itemDAO.save(item);
                    } catch (JGringottsDAOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    private EventHandler<ActionEvent> deleteListItemHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
            try {
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name)) {
                    Item item = itemDAO.findByName(name);
                    itemDAO.delete(item);
                    observableList.remove(name);
                    FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
                    if (!itemListView.getItems().isEmpty()) {
                        name = itemListView.getSelectionModel().getSelectedItem();
                        item = itemDAO.findByName(name);
                        if (item != null && StringUtils.isNotEmpty(item.getDescription())) {
                            contentArea.setText(item.getDescription());
                        }
                    }
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }

    };

    private EventHandler<MouseEvent> showDeleteContextMenuHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            String name = itemListView.getSelectionModel().getSelectedItem();
            if (StringUtils.isNotEmpty(name)) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    deleteContextMenu.show(itemListView, event.getScreenX(), event.getScreenY());
                } else {
                    deleteContextMenu.hide();
                }
            }
        }

    };

    private EventHandler<MouseEvent> itemListViewSelectionHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
            try {
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name)) {
                    contentArea.setText(itemDAO.findByName(name).getDescription());
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }

    };

    private ListChangeListener<String> itemListViewChangeHandler = new ListChangeListener<String>() {

        @Override
        public void onChanged(Change<? extends String> event) {
            ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
            try {
                String name = itemListView.getSelectionModel().getSelectedItem();
                if (StringUtils.isNotEmpty(name) && !observableList.isEmpty()) {
                    Item item = itemDAO.findByName(name);
                    if (item != null) {
                        contentArea.setText(item.getDescription());
                    }
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }
        }

    };

    private EventHandler<ActionEvent> loginEvenHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            String pin = pinField.getText();
            if (pin.length() < 8) {
                Dialogs.create().message("PIN must be a length of 8").showError();
                return;
            }

            JGringottsDAOManager daoMgr = JGringottsDAOManager.getInstance(username, password, pin);
            setDaoMgr(daoMgr);

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(JGringotts.class.getResource("main.fxml"));
                rootLayout = (BorderPane) loader.load();

                MainController controller = loader.getController();
                controller.setApp(JGringotts.this);

                ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
                List<Item> itemList = itemDAO.findAll();

                if (itemList != null && !itemList.isEmpty()) {
                    itemListView.getItems().clear();
                    FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
                    itemList.forEach(u -> observableList.add(u.getName()));
                    itemListView.setItems(observableList);

                    itemListView.getSelectionModel().selectFirst();
                    contentArea.setText(itemDAO.findByName(itemListView.getSelectionModel().getSelectedItem())
                            .getDescription());
                }

                SplitPane sp = new SplitPane();

                final StackPane sp1 = new StackPane();
                sp1.getChildren().add(itemListView);

                final StackPane sp2 = new StackPane();
                sp2.getChildren().add(contentArea);

                sp.getItems().addAll(sp1, sp2);
                sp.setDividerPositions(0.3f, 0.6f);

                rootLayout.setCenter(sp);

                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
            } catch (JGringottsDAOException | IOException e) {
                e.printStackTrace();
            }

        }

    };

    public void showAbout() {
        Dialogs.create().masthead("JGringotts").message(String.format("Version: %s", getVersion())).showInformation();
    }

    public void doExport() {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setTitle("Export Items");
        File directory = chooser.showDialog(stage);

        ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
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

    public void doImport() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setTitle("Import Items");
        List<File> fileList = chooser.showOpenMultipleDialog(stage);

        ItemDAO itemDAO = daoMgr.getDaoBean().getItemDAO();
        if (fileList != null && !fileList.isEmpty()) {
            for (File file : fileList) {
                try {
                    Item item = new Item();
                    item.setName(file.getName());
                    item.setDescription(FileUtils.readFileToString(file));
                    itemDAO.save(item);
                    observableList.add(item.getName());
                } catch (IOException | JGringottsDAOException e) {
                    e.printStackTrace();
                }
            }
            FXCollections.sort(observableList, (String a, String b) -> a.compareTo(b));
            itemListView.setItems(observableList);
        }
    }

    public void doNew() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JGringotts.class.getResource("new.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JGringottsDAOManager getDaoMgr() {
        return daoMgr;
    }

    public void setDaoMgr(JGringottsDAOManager daoMgr) {
        this.daoMgr = daoMgr;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void main(String[] args) {
        try {
            JGringotts.launch(JGringotts.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
