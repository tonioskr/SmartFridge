package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.*;
import com.example.SmartFridge.DTO.IngredientInTheFridgeDTO;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.model.IngredientInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class FridgePageController {
    @FXML
    private Button backbutton;
    @FXML
    private TableView<IngredientInFridge> FridgeTable;
    @FXML
    public TableColumn<IngredientInFridge, String> ProductNameColumn;
    @FXML
    public TableColumn<IngredientInFridge, Integer> ProductQuantityColumn;
    @FXML
    public TableColumn<IngredientInFridge, Date> ProductExpireDateColumn;
    @FXML
    private Button IncrementButton;
    @FXML
    private Button DecrementButton;
    private  ObservableList<IngredientInFridge> data = FXCollections.observableArrayList();

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
    private void goToHome() {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        ProductNameColumn.setCellValueFactory(
                new PropertyValueFactory<IngredientInFridge, String>("name")
        );
        ProductQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<IngredientInFridge, Integer>("quantity")
        );
        ProductExpireDateColumn.setCellValueFactory(
                new PropertyValueFactory<IngredientInFridge, Date>("expireDate")
        );
        FridgeTable.setItems(data);
        FridgeTable.setPlaceholder(new Label("No data."));
        System.out.println("Inizializzazione dati in frigo");

        fillTable();

        DecrementButton.setOnAction(event -> {
            if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
                Integer index = FridgeTable.getSelectionModel().getSelectedIndex();
                IngredientInFridge prod = FridgeTable.getItems().get(index);
                if (prod.getQuantity() - 1 != 0) {
                    prod.setQuantity(prod.getQuantity() - 1);
                    FridgeTable.getItems().set(index, prod);
                } else
                    remove_product(event);
            }
        });

        IncrementButton.setOnAction(event -> {
            if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
                Integer index = FridgeTable.getSelectionModel().getSelectedIndex();
                IngredientInFridge prod = FridgeTable.getItems().get(index);
                prod.setQuantity(prod.getQuantity() + 1);
                FridgeTable.getItems().set(index, prod);
            }
        });
        if(Application.authenticatedUser.getUsername().equals("backbutton")){
            backbutton.setVisible(true);
        }
    }
    public void refresh() {

        if(!data.isEmpty())
            FridgeDAO.updateFridge(data);
        dropTable();
        fillTable();
    }
    @FXML
    private void fillTable() {

         System.out.println("Inserting data into fridge");

        //retrive ingredient from fridge
        ArrayList<IngredientInTheFridgeDTO> ingredientList = new ArrayList<>();
        ingredientList = IngredientInTheFridgeDAO.getProduct(Application.authenticatedUser);
        if(ingredientList == null || ingredientList.isEmpty()) {
            dropTable();
        } else
            for (IngredientInTheFridgeDTO us : ingredientList) {
                IngredientInFridge newrow = new IngredientInFridge(us.getName(), us.getQuantity(), us.getDate());
                data.add(newrow);
            }
    }

    //if fridge is empty, table must be dropped
    private void dropTable() {
        data.clear();
    }

    public void remove_product(ActionEvent actionEvent) {
        if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
            IngredientInFridge selectedItem = FridgeTable.getSelectionModel().getSelectedItem();

            //create a product and remove it from the db
            IngredientInTheFridgeDTO product_to_delete = new IngredientInTheFridgeDTO(
                    selectedItem.getName(),
                    selectedItem.getQuantity(),
                    selectedItem.getExpireDate()
            );
            IngredientInTheFridgeDAO.remove_product_mongo(product_to_delete, Application.authenticatedUser.getId());
            FridgeTable.getItems().remove(selectedItem);
        }
    }


}


