package com.kiluet.jgringotts;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.kiluet.jgringotts.dao.JGringottsDAOManager;

public class LoginController implements Initializable {

    private final List<Integer> countList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    @FXML
    private Button pinButton1;

    @FXML
    private Button pinButton2;

    @FXML
    private Button pinButton3;

    @FXML
    private Button pinButton4;

    @FXML
    private Button pinButton5;

    @FXML
    private Button pinButton6;

    @FXML
    private Button pinButton7;

    @FXML
    private Button pinButton8;

    @FXML
    private Button pinButton9;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField pinField;

    private JGringotts app;

    public LoginController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameTextField.setText(System.getProperty("user.name"));
        passwordField.setText("asdfasdf");
        pinField.setText("11111111");

        Collections.shuffle(countList);

        final List<Button> pinButtonList = new ArrayList<Button>();
        pinButtonList.add(pinButton1);
        pinButtonList.add(pinButton2);
        pinButtonList.add(pinButton3);
        pinButtonList.add(pinButton4);
        pinButtonList.add(pinButton5);
        pinButtonList.add(pinButton6);
        pinButtonList.add(pinButton7);
        pinButtonList.add(pinButton8);
        pinButtonList.add(pinButton9);

        for (int i = 0; i < countList.size(); ++i) {

            final String pinNumber = countList.get(i).toString();
            Button button = pinButtonList.get(i);
            button.setText(pinNumber);
            button.setOnAction(e -> {
                pinField.appendText(((Button) e.getSource()).getText());
                redrawPINButtons(pinButtonList);
            });

        }

    }

    private void redrawPINButtons(List<Button> pinButtonList) {

        Collections.shuffle(countList);

        for (int i = 0; i < countList.size(); ++i) {
            final String pinNumber = countList.get(i).toString();
            Button button = pinButtonList.get(i);
            button.setText(pinNumber);
        }

    }

    @FXML
    public void doLogin() {

        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String pin = pinField.getText();
        if (pin.length() < 8) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR: PIN length");
            alert.setContentText("PIN must be a length of 8");
            alert.showAndWait();
            return;
        }
        JGringottsDAOManager daoMgr = JGringottsDAOManager.getInstance(username, password, pin);
        app.setDaoMgr(daoMgr);
        app.showMain();
    }

    public JGringotts getApp() {
        return app;
    }

    public void setApp(JGringotts app) {
        this.app = app;
    }

}
