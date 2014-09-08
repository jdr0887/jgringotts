package com.kiluet.jgringotts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.controlsfx.dialog.Dialogs;

public class NewController {

    private JGringotts app;

    private Stage dialogStage;

    @FXML
    private TextField name;

    public NewController() {
        super();
    }

    @FXML
    private void doOK(final ActionEvent event) {
        if (isInputValid()) {
            app.createNew(this.name.getText());
            dialogStage.close();
        }
    }

    @FXML
    private void doCancel(final ActionEvent event) {
        dialogStage.close();
    }

    public JGringotts getApp() {
        return app;
    }

    public void setApp(JGringotts app) {
        this.app = app;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (StringUtils.isEmpty(name.getText())) {
            errorMessage += "No valid name!\n";
        }

        if (StringUtils.isEmpty(errorMessage)) {
            return true;
        } else {
            Dialogs.create().title("Invalid Fields").masthead("Please correct invalid fields").message(errorMessage)
                    .showError();
            return false;
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public TextField getName() {
        return name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

}
