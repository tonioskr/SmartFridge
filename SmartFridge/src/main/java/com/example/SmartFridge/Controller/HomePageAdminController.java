package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HomePageAdminController {
    @FXML
    private Label Username;
    @FXML
    private Button Products;
    @FXML
    private Button Recipes;
    @FXML
    private Button Users;

    public void setClick(MouseEvent mouseEvent) {
        Utils.setClick(mouseEvent);
    }
    public void unsetClick(MouseEvent mouseEvent) {
        Utils.unsetClick(mouseEvent);
    }
    public void setOver(MouseEvent mouseEvent) {
        Utils.setOver(mouseEvent);
    }
    public void unsetOver(MouseEvent mouseEvent) {
        Utils.unsetOver(mouseEvent);
    }
    public void initialize() {
        Username.setText("Admin");

        }
    @FXML
    protected void logout() throws IOException {
        Application.authenticatedUser = null;
        goToLogin();
    }
    @FXML
    protected void goToLogin() throws IOException {
        Application.changeScene("LoginPage");
    }
    @FXML
    protected void goToProducts() throws IOException {
        Application.changeScene("AllIngredient");
    }

    @FXML
    protected void goToRecipes() throws IOException {
        Application.changeScene("AllRecipes");
    }

    @FXML
    protected void goToUsers() throws IOException {
        Application.changeScene("AllUsers");
    }

    @FXML
    protected void goToAnalytics() throws IOException {
        Application.changeScene("Analytics");
    }
}
