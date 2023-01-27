package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.Utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class HomePageController {
    @FXML
    private Label Username;
    @FXML
    private int tabnumber;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab TabUser;
    @FXML
    private Parent UserTab;
    @FXML
    private AllUsersController UserTabController;
    @FXML
    private FollowedUserController FollowTabController;
    @FXML
    private FridgePageController FridgeTabController;


    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FollowedUser.fxml"));
        FollowedUserController follow = loader.getController();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, tab, t1) -> {
            
            if(t1.getText().equals("Users")) {
                UserTabController.refresh();
            } else if (t1.getText().equals("Following")) {
                FollowTabController.refresh();
            } else if (t1.getText().equals("Fridge")) {
                FridgeTabController.refresh();
            }
        });
    }

    @FXML
    protected void logout() throws IOException {
        Application.authenticatedUser = null;
        goToLogin();
    }
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
    public void changeTab(int i) throws InterruptedException {
        tabnumber = i;
    }
    @FXML
    protected void leave() throws  IOException{
            MongoDbDriver.close();
            Neo4jDriver.close();
            Platform.exit();
    }
    @FXML
    protected void goToLogin() throws IOException {
        Application.changeScene("LoginPage");
    }
    @FXML
    protected void goToProfile() throws IOException {
        Application.changeScene("ProfilePage");
    }
    @FXML
    protected void goToFridge() throws IOException {
        Application.changeScene("FridgePage");
    }

    @FXML
    protected void goToUsers() throws IOException {
        Application.changeScene("AllUsers");
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
    protected void goToFolloewdUser() throws IOException {
        Application.changeScene("FollowedUser");
    }
}
