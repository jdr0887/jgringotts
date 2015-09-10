package com.kiluet.jgringotts;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.JGringottsDAOManager;
import com.kiluet.jgringotts.dao.model.Item;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JGringotts extends Application {

    private static final Logger logger = LoggerFactory.getLogger(JGringotts.class);

    private Stage stage;

    private BorderPane rootLayout;

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

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JGringotts.class.getResource("login.fxml"));
            AnchorPane rootPane = loader.load();

            LoginController loginController = loader.getController();
            loginController.setApp(this);

            if (getVersion().contains("SNAPSHOT")) {
                loginController.getPasswordField().setText("asdfasdf");
                loginController.getPinField().setText("11111111");
            }

            Scene scene = new Scene(rootPane);
            this.stage.setScene(scene);
            this.stage.sizeToScene();

            this.stage.show();

        } catch (Exception ex) {
            logger.error("Error", ex);
        }
    }

    public void showMain() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JGringotts.class.getResource("main.fxml"));
            rootLayout = loader.load();

            MainController controller = loader.getController();
            controller.setApp(this);

            ItemDAO itemDAO = getDaoMgr().getDaoBean().getItemDAO();
            try {
                List<Item> itemList = itemDAO.findAll();

                if (itemList != null && !itemList.isEmpty()) {
                    controller.getItemListView().getItems().clear();
                    FXCollections.sort(controller.getObservableList(), (String a, String b) -> a.compareTo(b));
                    itemList.forEach(u -> controller.getObservableList().add(u.getValue()));
                    controller.getItemListView().setItems(controller.getObservableList());
                    controller.getItemListView().getSelectionModel().selectFirst();
                }
            } catch (JGringottsDAOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    try {
                        Item item = itemDAO
                                .findByValue(controller.getItemListView().getSelectionModel().getSelectedItem());
                        item.setDescription(controller.getItemContentTextArea().getText());
                        itemDAO.save(item);
                    } catch (JGringottsDAOException e) {
                        e.printStackTrace();
                    }
                }

            });

            stage.show();

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
