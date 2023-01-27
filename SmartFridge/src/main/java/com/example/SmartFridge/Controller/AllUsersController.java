package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.DTO.userDTO;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.example.SmartFridge.DAO.UserDAO.*;

public class AllUsersController {
    @FXML
    private TableView<userDTO> UserTable;
    @FXML
    public TableColumn<userDTO, String> UsernameColumn;
    @FXML
    public TableColumn<userDTO, String> CountryColumn;
    @FXML
    public TableColumn<userDTO, LocalDate> NumberOfRecipe;
    @FXML
    public TableColumn FollowButtonColumn;
    @FXML
    public TextField SearchUser;

    @FXML
    private Button Show_Suggested_User;
    @FXML
    private Button backbutton;
    @FXML
    private Button show_more;
    static int called_times = 0;
    /*
    token used to the following feauture: when i want to see the suggested user, i click on it, then
    the "show more" button will be disable cause i will upload only max 40 suggested user,
    and the button "Show_Suggested_User" will be transformed to "show_all_user" which will show all the user not followed yet.
    . When we will click on that button, the "show user" button will not be disable anymore, and Show_Suggested_User will return
    to the funcionality of showing the the suggested user. The token allows to switch
    the button functionality from Show_Suggested_User to show_all_user and viceversa
    */
    private boolean token_show_suggested_all = true;


    private static ObservableList<userDTO> data = FXCollections.observableArrayList();

    public void refresh() {
        called_times = 0;
        String username = SearchUser.getText();
        int limit_views_user = 20;
        List<userDTO> users = Search_for_Unfollowed_user(username,limit_views_user, called_times);
        if(users == null || users.size() == 0) return;
        data.clear();
        for (userDTO us : users) {
            data.add(us);
        }
    }

    public void initialize() {
        UsernameColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("username")
        );
        CountryColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("country")
        );
        FollowButtonColumn.setCellValueFactory(
                new PropertyValueFactory<>(""));

        Callback<TableColumn<userDTO, String>, TableCell<userDTO, String>> cellFactory
                = new Callback<>() {
            @Override
            public TableCell call(final TableColumn<userDTO, String> param) {
                final TableCell<userDTO, String> cell = new TableCell<userDTO, String>() {

                    Button btn;

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            if(Application.authenticatedUser.getUsername().equals("admin"))
                            {

                                //disable Show_Suggested_User button
                                Show_Suggested_User.setDisable(true);

                                btn = new Button("Delete");
                                btn.setOnAction(event->{
                                    userDTO selectedItem = getTableView().getItems().get(getIndex());
                                    UserTable.getItems().remove(selectedItem);
                                    UserDAO.deleteUser(new RegisteredUser(selectedItem));
                                    btn.setText("Delete");
                                });
                            } else {
                                btn = new Button("Follow");
                                btn.setOnAction(event -> {
                                    userDTO user = getTableView().getItems().get(getIndex());
                                    UserDAO.follow_a_user(Application.authenticatedUser.id, user.getId());
                                    btn.setDisable(true);
                                    getTableView().getItems().remove(getIndex());
                                });
                            }
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        FollowButtonColumn.setCellFactory(cellFactory);
        UserTable.setItems(data);


        UserTable.setRowFactory( tv -> {
            TableRow<userDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    userDTO userDTO = row.getItem();
                    viewRecipesOfTheUser(userDTO);
                }
            });
            return row ;
        });
        fillTable();
        if(Application.authenticatedUser.getUsername().equals("admin")){
            Show_Suggested_User.setDisable(true);
            //show_more.setDisable(true);
            backbutton.setVisible(true);
        }
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
    static void viewRecipesOfTheUser(userDTO userDTO) {
        try {
            AllRecipesController.utente = userDTO.getUsername();
            Application.changeScene("AllRecipes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void show_suggested_user(ActionEvent actionEvent) {
        if(token_show_suggested_all) {
            List<userDTO> users = UserDAO.getListOfSuggesteUser();
            if (users != null) {
                data.clear();
                for (userDTO us : users) {
                    data.add(us);
                }
            } else {
                data.clear();
                UserTable.setItems(data);
            }
            //this beacuse i will load all the suggested user at once
            show_more.setDisable(true);
            Show_Suggested_User.setText("Show all user");
            token_show_suggested_all=false;
        }
        else{
            data.clear();
            UserTable.setItems(data);
            called_times=0;
            show_more.setDisable(false);
            fillTable();
            Show_Suggested_User.setText("Show suggested user");
            token_show_suggested_all=true;
        }
    }

    //fill the table with 20 user. By pressing the button the user will visualize
    // 20 user in plus appended at the end of the previous list of user


    public void fillTable() {
        String username = SearchUser.getText();
        int limit_views_user = 20;
        List<userDTO> users = Search_for_Unfollowed_user(username,limit_views_user, called_times);
        if(users == null || users.size() == 0) return;
        for (userDTO us : users) {
            data.add(us);
        }
        called_times++;
    }

    public void Search_for_user() {
        if (Application.authenticatedUser.getUsername().equals("admin")) {
            userDTO searched_user = UserDAO.searchforadmin(SearchUser.getText());

        data.clear();
        if (searched_user != null) {

            data.add(searched_user);
            UserTable.setItems(data);
            return;
        }
    }
            String username = SearchUser.getText();
            if (!username.equals("")) {
                try {
                    List<userDTO> searched_user = UserDAO.Search_for_Unfollowed_user(username, 20, 0);

                    data.clear();
                    if (searched_user != null) {
                        for (userDTO us : searched_user) {
                            data.add(us);
                        }
                        UserTable.setItems(data);
                    }
                } catch (Error e) {
                    System.out.println(e);
                }
            } else {
                data.clear();
                UserTable.setItems(data);
                //restore all the user
                called_times = 0;
                fillTable();
            }
        }

        @FXML
        protected void goToHome () throws IOException {
            try {
                if (Application.authenticatedUser.getUsername().equals("admin"))
                    Application.changeScene("HomePageAdmin");
                else
                    Application.changeScene("HomePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


