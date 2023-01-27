package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.IngredientDAO;
import com.example.SmartFridge.DTO.IngredientDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class AddIngredientToFridgeController {
    @FXML
    public TextField Name;
    @FXML
    public TextField Category;
    @FXML
    public TextField Carbs;
    @FXML
    public TextField Fat;
    @FXML
    public TextField Measure;
    @FXML
    public TextField Grams;
    @FXML
    public TextField Calories;
    @FXML
    public TextField Protein;
    @FXML
    public TextField Fiber;
    @FXML
    public Label invalidAdd;
    @FXML
    public Button add;


    public static IngredientDTO row;
    public static boolean modify;

    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";

    public void initialize()
    {
        if(row != null)
        {
            Name.setDisable(true);
            Name.setText(row.getFood());
            Grams.setText(row.getGrams());
            Fat.setText(row.getFat());
            Fiber.setText(row.getFiber());
            Measure.setText(row.getMeasure());
            Calories.setText(Integer.toString(row.getCalories()));
            Category.setText(row.getCategory());
            Protein.setText(row.getCategory());
            Carbs.setText(row.getCarbs());

            add.setOnAction(actionEvent -> {

                row.setGrams(Grams.getText());
                row.setFat(Fat.getText());
                row.setFiber(Fiber.getText());
                row.setMeasure(Measure.getText());
                row.setCalories(Integer.parseInt(Calories.getText()));
                row.setCategory(Category.getText());
                row.setProtein(Protein.getText());
                row.setCarbs((Carbs.getText()));

                boolean esito = IngredientDAO.updateIngredient(row);
                if(esito){
                    invalidAdd.setText("Ingredient updated ");
                    invalidAdd.setStyle(successMessage);
                }else{
                    invalidAdd.setText("Ingredient not updated ");
                    invalidAdd.setStyle(errorMessage);
                }

            });
        } else
            add.setOnAction(event->{
                AddProduct();
            });
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
    public boolean checkInput()
    {
        if(Name.getText().isBlank() || Grams.getText().isBlank() || Category.getText().isBlank() || Calories.getText().isBlank()
         || Fat.getText().isBlank() || Fiber.getText().isBlank() || Protein.getText().isBlank() || Carbs.getText().isBlank() ||
                Measure.getText().isBlank())
                return false;

        if(!Utils.isNumeric(Grams.getText()) || !Utils.isNumeric(Calories.getText()) || !Utils.isNumeric(Fat.getText())
          || !Utils.isNumeric(Fiber.getText()) || !Utils.isNumeric(Protein.getText()) || !Utils.isNumeric(Carbs.getText()))
            return false;

        return true;
    }

    public void AddProduct()
    {
        if(checkInput())
        {
            IngredientDTO ingredient = new IngredientDTO(Name.getText(), Measure.getText(), Grams.getText(), Integer.parseInt(Calories.getText()),
                    Protein.getText(), Fat.getText(), Fiber.getText(), Carbs.getText(), Category.getText());

            IngredientDAO.addIngredient(ingredient);
            invalidAdd.setText("Product added ");
            invalidAdd.setStyle(successMessage);
        } else {
            invalidAdd.setText("Product not added, retry");
            invalidAdd.setStyle(errorMessage);
        }
    }
    @FXML
    private void goToAllIngredient()
    {
        try {
            row = null;
            modify = false;
            Application.changeScene("AllIngredient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
