package com.example.SmartFridge.Controller;


import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.RecipeDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class LoginController {

    // Strings which hold css elements to easily re-use in the application
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    String onClick = "-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5";
    String onReleased = "-fx-background-color: #24a0ed; -fx-text-fill: WHITE;";

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private TextField signUpUsernameTextField;
    @FXML
    private TextField signUpEmailTextField;
    @FXML
    private TextField signUpPasswordField;
    @FXML
    private TextField signUpRepeatPasswordField;
    @FXML
    private TextField signUpNameTextField;
    @FXML
    private TextField signUpSurnameTextField;
    @FXML
    private ComboBox signUpCountryTextField;
    @FXML
    private ListView listview;
    @FXML
    private Text recipetext;
    @FXML
    private Text authortext;
    @FXML
    private TextArea ingredienttext;
    @FXML
    private TextArea preparationtext;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Text pagenumber;
    ObservableList<RecipeDTO> data = FXCollections.observableArrayList();
    private int page = 0;

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
    protected void onNextClick(){
        page++;
        previousButton.setDisable(false);
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        data.clear();
        for(RecipeDTO r : recipes)
            data.add(r);
        listview.getSelectionModel().select(0);
        pagenumber.setText(Integer.toString(page));
        showRecipe();
    }

    @FXML
    protected void onPreviousClick(){
        if(page <= 0){
            return;
        }
        page--;
        pagenumber.setText(Integer.toString(page));
        if(page == 0){
            previousButton.setDisable(true);
        }
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        data.clear();
        for(RecipeDTO r : recipes)
            data.add(r);
        listview.getSelectionModel().select(0);
        showRecipe();
    }


    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        loginUsernameTextField.clear();
        loginUsernameTextField.setStyle(successStyle);
        loginPasswordField.clear();
        loginPasswordField.setStyle(successStyle);
        invalidLoginCredentials.setText("");
    }
    @FXML
    protected void onLeaveButtonClick() {
        MongoDbDriver.close();
        Neo4jDriver.close();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onCancelButtonClick2() {
        signUpCountryTextField.setValue("");
        signUpCountryTextField.setStyle(successStyle);
        signUpEmailTextField.clear();
        signUpEmailTextField.setStyle(successStyle);
        signUpNameTextField.clear();
        signUpNameTextField.setStyle(successStyle);
        signUpUsernameTextField.clear();
        signUpUsernameTextField.setStyle(successStyle);
        signUpPasswordField.clear();
        signUpPasswordField.setStyle(successStyle);
        signUpRepeatPasswordField.clear();
        signUpRepeatPasswordField.setStyle(successStyle);
        signUpSurnameTextField.setStyle(successStyle);
        signUpSurnameTextField.clear();
        invalidSignupCredentials.setText("");
    }

    @FXML
    protected void onLoginButtonClick() {
        clearloginField();
        if (loginUsernameTextField.getText().isBlank() || loginPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("All Login fields are required!");
            invalidLoginCredentials.setStyle(errorMessage);
            invalidSignupCredentials.setText("");

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
                loginUsernameTextField.requestFocus();
            } else if (loginPasswordField.getText().isBlank()) {
                loginPasswordField.setStyle(errorStyle);
                loginPasswordField.requestFocus();
            }
        } else {

            if (UserDAO.checkPassword(loginUsernameTextField.getText(), loginPasswordField.getText())) {
                invalidLoginCredentials.setText("Login Successful!");
                invalidLoginCredentials.setStyle(successMessage);
                loginUsernameTextField.setStyle(successStyle);
                loginPasswordField.setStyle(successStyle);
                invalidSignupCredentials.setText("");

                try {
                    String[] credentials = UserDAO.getUser(loginUsernameTextField.getText());

                    String id = credentials[0];
                    String username = credentials[1];

                    if (username.equals("admin")) {
                        Application.authenticatedUser = new RegisteredUser(id, username);
                        goToAdminPage();

                    } else {
                        String country = credentials[2];
                        String firstName = credentials[3];
                        String lastName = credentials[4];
                        String email = credentials[5];
                        String password = credentials[6];
                        LocalDate regdate;
                        if(credentials[7].length() > 9)
                            regdate = Utils.LOCAL_DATE(credentials[7]);
                        else
                            regdate = Utils.LOCAL_DATE_OLD(credentials[7]);
                        Application.authenticatedUser = new RegisteredUser(id, username,password, firstName, lastName, country, email,regdate);
                        goToHomePage();

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                invalidLoginCredentials.setText("Password is wrong or user is not existing.");
                loginPasswordField.setStyle(errorStyle);
                loginUsernameTextField.setStyle(errorStyle);
                invalidLoginCredentials.setStyle(errorMessage);
                invalidSignupCredentials.setText("");
            }
        }
    }

    protected void goToHomePage() throws IOException {
        Application.changeScene("MainTable");
    }

    protected void goToAdminPage() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

    public void initialize() throws IOException {
        ingredienttext.setWrapText(true);
        preparationtext.setWrapText(true);
        signUpCountryTextField.setItems(Utils.getCountryData());
        previousButton.setDisable(true);

        int limit_views_recipe = 20;
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(limit_views_recipe,20);
        listview.setCellFactory(new Callback<ListView<RecipeDTO>, ListCell<RecipeDTO>>() {
            @Override
            public ListCell call(ListView<RecipeDTO> listView) {
                final ListCell<RecipeDTO> cell = new ListCell<RecipeDTO>(){
                    final Tooltip tp = new Tooltip();
                    @Override
                    public void updateItem(RecipeDTO item, boolean empty){
                        super.updateItem(item,empty);
                        if(item != null) {
                            setText(item.getName());
                            tp.setText("Author:"+item.getAuthor()+"\n"+"Cooking time:"+item.getCooktime()+"\n"+"Total Cooking time:"+item.getTotalTime());
                            setTooltip(tp);
                            }
                    }
                };
                return cell;
            }
        });

        for(RecipeDTO us : recipes) {
            data.add(us);
        }
        listview.setItems(data);
        listview.getSelectionModel().select(0);
        showRecipe();
    }

    @FXML
    protected void showRecipe(){
        int index = listview.getSelectionModel().getSelectedIndex();
        RecipeDTO recipe = (RecipeDTO) listview.getItems().get(index);

        authortext.setText(recipe.getAuthor());
        ingredienttext.setText(recipe.getIngrients());
        preparationtext.setText(recipe.getDirection());

        recipetext.setText(recipe.getName());
    }

    @FXML
    protected void onSignUpButtonClick() throws IOException {
        clearsignupField();
        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpUsernameTextField.getText().equals("admin") ||
                signUpPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16 ||
                signUpCountryTextField.getValue() == null || signUpCountryTextField.getValue().toString().equals("") ||
                signUpPasswordField.getText().length() > 16 || signUpRepeatPasswordField.getText().length() > 16 || signUpNameTextField.getText().isBlank() ||
                signUpSurnameTextField.getText().isBlank() || signUpCountryTextField.getSelectionModel().isEmpty() ||
                signUpPasswordField.getText().equals(signUpUsernameTextField.getText())) {
            invalidSignupCredentials.setText("Please fill in all fields! Max length is 16.");
            invalidSignupCredentials.setStyle(errorMessage);
            invalidLoginCredentials.setText("");

            if (signUpUsernameTextField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16) {
                signUpUsernameTextField.setStyle(errorStyle);
                signUpUsernameTextField.requestFocus();
            } else if (signUpNameTextField.getText().isBlank() || signUpNameTextField.getText().length() > 16) {
                signUpNameTextField.setStyle(errorStyle);
                signUpNameTextField.requestFocus();
            } else if (signUpSurnameTextField.getText().isBlank() || signUpSurnameTextField.getText().length() > 16) {
                signUpSurnameTextField.setStyle(errorStyle);
                signUpSurnameTextField.requestFocus();
            } else if (signUpEmailTextField.getText().isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
                signUpEmailTextField.requestFocus();
            } else if (signUpPasswordField.getText().isBlank() || signUpPasswordField.getText().length() > 16) {
                signUpPasswordField.setStyle(errorStyle);
                signUpPasswordField.requestFocus();
            } else if (signUpRepeatPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().length() > 16) {
                signUpRepeatPasswordField.setStyle(errorStyle);
                signUpRepeatPasswordField.requestFocus();
            } else if (signUpCountryTextField.getValue() == null || signUpCountryTextField.getValue().toString().equals("") ) {
                signUpCountryTextField.setStyle(errorStyle);
                invalidSignupCredentials.setText("Please, select a country from the list.");
                signUpCountryTextField.requestFocus();
            } else if (signUpPasswordField.getText().equals(signUpUsernameTextField.getText())) {
                signUpPasswordField.setStyle(errorStyle);
                signUpPasswordField.requestFocus();
                signUpRepeatPasswordField.setStyle(errorStyle);
                invalidSignupCredentials.setText("Password and Username should not match!");
            }

        } else if (!Utils.CheckEmail(signUpEmailTextField.getText())) {
            invalidSignupCredentials.setText("The email is in incorrect format!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpEmailTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        }else if (!signUpRepeatPasswordField.getText().equals(signUpPasswordField.getText())) {
            invalidSignupCredentials.setText("The Passwords don't match!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        } else if (Objects.equals(UserDAO.findUser(signUpUsernameTextField.getText()), signUpUsernameTextField.getText())) {
            invalidSignupCredentials.setText("Username already exists!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpUsernameTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        } else {
            LocalDate nowdate = LocalDate.now();
            nowdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            RegisteredUser user = new RegisteredUser(
                    "",
                    signUpUsernameTextField.getText(),
                    signUpPasswordField.getText(),
                    signUpNameTextField.getText(),
                    signUpSurnameTextField.getText(),
                    signUpCountryTextField.getValue().toString(),
                    signUpEmailTextField.getText(),
                    nowdate
            );

            //signup of the user
            String user_index = UserDAO.signup(user);


            if(user_index.equals("error")){
                invalidSignupCredentials.setText("Something went wrong! Retry");
                invalidSignupCredentials.setStyle(errorMessage);
                invalidLoginCredentials.setText("");

            } else {
                user.setId(user_index);
                Application.authenticatedUser = user;
                goToHomePage();

            }
        }
    }

    private void clearsignupField() {
        signUpUsernameTextField.setStyle(successStyle);
        signUpSurnameTextField.setStyle(successStyle);
        signUpPasswordField.setStyle(successStyle);
        signUpRepeatPasswordField.setStyle(successStyle);
        signUpNameTextField.setStyle(successStyle);
        signUpEmailTextField.setStyle(successStyle);
        signUpCountryTextField.setStyle(successStyle);
    }

    private void clearloginField(){
        loginPasswordField.setStyle(successStyle);
        loginUsernameTextField.setStyle(successStyle);
    }
}