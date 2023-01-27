package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.DTO.userDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ProfilePageController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #green; -fx-border-width: 2; -fx-border-radius: 5;";

    @FXML
    private Label Username;
    @FXML
    private TextField FirstName;
    @FXML
    private TextField LastName;
    @FXML
    private Label Email;
    @FXML
    private ComboBox Country;
    @FXML
    private TextField NewPassword;
    @FXML
    private Button ChangePasswordButton;
    @FXML
    private Button changeFieldButton;
    @FXML
    private Label labeltext;
    @FXML
    private Button gohomebutton;
    @FXML
    private Button deleteuserbutton;
    @FXML
    private Label registrationdate;

    public void initialize()
    {
        Username.setText(Application.authenticatedUser.getUsername());
        FirstName.setText(Application.authenticatedUser.getFirstName());
        LastName.setText(Application.authenticatedUser.getLastName());
        Email.setText(Application.authenticatedUser.getEmail());
        registrationdate.setText(Application.authenticatedUser.getRegistrationDate().format(Utils.dateFormatter));
        int index = Utils.getCountryData().indexOf(Application.authenticatedUser.getCountry());
        Country.setItems(Utils.getCountryData());
        Country.getSelectionModel().select(index);


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
    @FXML
    protected void onChangePasswordButtonClick()
    {

        String password;
        if(NewPassword.getText().isBlank()) {
            password = Application.authenticatedUser.getPassword();
        }
        else {
            password = NewPassword.getText();
        }
        if(FirstName.getText().isBlank()) {
            FirstName.setText(Application.authenticatedUser.getFirstName());
        }
        if(LastName.getText().isBlank()) {
            LastName.setText(Application.authenticatedUser.getLastName());
        }

            userDTO user_to_modify = new userDTO();
            user_to_modify.setPassword(password);
            user_to_modify.setName(FirstName.getText());
            user_to_modify.setCountry(Country.getSelectionModel().getSelectedItem().toString());
            user_to_modify.setSurname(LastName.getText());

            Application.authenticatedUser.setPassword(password);
            Application.authenticatedUser.setFirstName(FirstName.getText());
            Application.authenticatedUser.setLastName(LastName.getText());
            Application.authenticatedUser.setCountry(Country.getSelectionModel().getSelectedItem().toString());

            //UserDAO.changePassword(Application.authenticatedUser, NewPassword.getText());
            UserDAO.changeField(Application.authenticatedUser, user_to_modify);

            labeltext.setText("Fields changed");
            labeltext.setStyle(successMessage);
    }

    @FXML
    protected void onDeleteUserClick()
    {
        try{
            UserDAO.deleteUser(Application.authenticatedUser);
            GoToLoginPage();
        } catch (Exception error){
            System.out.println(error);
        }
    }

    @FXML
    protected void onGoToHomeClick() throws IOException {
        Application.changeScene("HomePage");
    }
    @FXML
    protected void GoToLoginPage() throws IOException {
        Application.changeScene("LoginPage");
    }

    @FXML
    protected void showMyRecipe() throws IOException {
        Application.changeScene("showMyRecipe");
    }
}
