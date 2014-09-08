package com.kiluet.jgringotts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainController {

    private JGringotts app;

    public MainController() {
        super();
    }

    @FXML
    private void showAbout(final ActionEvent event) {
        app.showAbout();
    }

    @FXML
    private void doNew(final ActionEvent event) {
        app.doNew();
    }

    @FXML
    private void doExport(final ActionEvent event) {
        app.doExport();
    }

    @FXML
    private void doImport(final ActionEvent event) {
        app.doImport();
    }

    @FXML
    private void doExit(final ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
                app.showAbout();
            }
        }
    }

    public JGringotts getApp() {
        return app;
    }

    public void setApp(JGringotts app) {
        this.app = app;
    }

}
