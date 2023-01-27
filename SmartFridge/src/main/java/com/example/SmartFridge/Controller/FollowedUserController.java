package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.DTO.userDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

import static com.example.SmartFridge.DAO.UserDAO.getListOfFollowedUser;
public class FollowedUserController {
    @FXML
    private TableView<userDTO> UserTable;
    @FXML
    public TableColumn<userDTO, String> UsernameColumn;
    @FXML
    public TableColumn<userDTO, String> CountryColumn;

    @FXML
    public TableColumn UnFollowButtonColumn;
    @FXML
    public TextField SearchUser;


    private ObservableList<userDTO> data = FXCollections.observableArrayList();
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
        UsernameColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("username")
        );
        CountryColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("country")
        );
        UnFollowButtonColumn.setCellValueFactory(
                new PropertyValueFactory<>(""));

        Callback<TableColumn<userDTO, String>, TableCell<userDTO, String>> cellFactory
                = new Callback<>() {
            @Override
            public TableCell call(final TableColumn<userDTO, String> param) {
                final TableCell<userDTO, String> cell = new TableCell<userDTO, String>() {

                    final Button btn = new Button("Unfollow");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                userDTO user = getTableView().getItems().get(getIndex());
                                //userDAO.follow_a_user(Integer.parseInt(Application.authenticatedUser.id),user.getId());
                                UserDAO.unfollowUser(Application.authenticatedUser.id, user.getId());

                                //btn.setText("Unfollowed");
                                data.remove(getIndex());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        UnFollowButtonColumn.setCellFactory(cellFactory);
        UserTable.setItems(data);

        UserTable.setRowFactory( tv -> {
            TableRow<userDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    userDTO userDTO = row.getItem();
                    AllUsersController.viewRecipesOfTheUser(userDTO);
                }
            });
            return row ;
        });

        fillTable();
    }

    static int called_times = 0;

    //fill the table with 20 followed user. By pressing the button the user will visualize
    // 20 user in plus appended at the end of the previous list of user
    public void fillTable() {
        int limit_views_user = 20;
        List<userDTO> users = getListOfFollowedUser(limit_views_user, called_times);
        if(users != null) {
            for (userDTO us : users) {
                data.add(us);
            }
            called_times++;
        }
    }
    public void refresh(){
        called_times = 0;
        data.clear();
        int limit_views_user = 20;
        List<userDTO> users = getListOfFollowedUser(limit_views_user, called_times);
        if(users != null) {
            for (userDTO us : users) {
                data.add(us);
            }
            called_times++;
        }
    }
    public void Search_for_followed_user() {
        String username = SearchUser.getText();
        if(true) {
            try {
                List<userDTO> searched_user = UserDAO.Search_for_followed_user(username);

                if(searched_user.size()!=0)
                {
                    data.clear();
                    data.add(searched_user.get(0));
                    UserTable.setItems(data);
                }
            } catch (Error e){
                System.out.println(e);
            }
        }
        else fillTable();
    }



    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }


}

