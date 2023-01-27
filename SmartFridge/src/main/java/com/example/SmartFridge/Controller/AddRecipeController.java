package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.RecipeDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;

public class AddRecipeController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    @FXML
    private TextField RecipeTitle;
    @FXML
    private TextField PreparationTimeH;
    @FXML
    private TextField PreparationTimeM;
    @FXML
    private TextField CookTimeH;
    @FXML
    private TextField CookTimeM;
    @FXML
    private TextField TotalTimeH;
    @FXML
    private TextField TotalTimeM;
    @FXML
    private TextArea Ingredients;
    @FXML
    private TextArea Directions;
    @FXML
    private Label invalidRecipe;
    private Tooltip tooltip;

    public void initialize(){
        tooltip = new Tooltip();
        tooltip.setText("ONLY NUMBER");
        tooltip.setShowDelay(new Duration(0.1));
        Utils.numericOnly(TotalTimeH);
        Utils.numericOnly(PreparationTimeH);
        Utils.numericOnly(PreparationTimeM);
        Utils.numericOnly(CookTimeH);
        Utils.numericOnly(CookTimeM);
        Utils.numericOnly(TotalTimeM);
        TotalTimeH.setTooltip(tooltip);
        TotalTimeM.setTooltip(tooltip);
        PreparationTimeH.setTooltip(tooltip);
        PreparationTimeM.setTooltip(tooltip);
        CookTimeM.setTooltip(tooltip);
        CookTimeH.setTooltip(tooltip);
    }
    public void resetfields(ActionEvent actionEvent) {
        RecipeTitle.clear();
        PreparationTimeM.clear();
        PreparationTimeH.clear();
        CookTimeH.clear();
        CookTimeM.clear();
        TotalTimeH.clear();
        TotalTimeM.clear();
        Ingredients.clear();
        Directions.clear();
        invalidRecipe.setText("");
    }
    @FXML
    private void addRecipe()
    {
        clearErrorStyle();
        if (RecipeTitle.getText().isBlank() || Ingredients.getText().isBlank() || Directions.getText().isBlank()
                || CookTimeH.getText().isBlank()|| CookTimeM.getText().isBlank() || TotalTimeH.getText().isBlank() || TotalTimeM.getText().isBlank()
                || PreparationTimeH.getText().isBlank()|| PreparationTimeM.getText().isBlank()) {
            invalidRecipe.setText("All recipe fields are required!");
            invalidRecipe.setStyle(errorMessage);

            if (RecipeTitle.getText().isBlank()) {
                RecipeTitle.setStyle(errorStyle);
            } else if (PreparationTimeH.getText().isBlank()) {
                PreparationTimeH.requestFocus();
                PreparationTimeH.setStyle(errorStyle);
            } else if (PreparationTimeM.getText().isBlank()) {
                PreparationTimeM.requestFocus();
                PreparationTimeM.setStyle(errorStyle);
            } else if (CookTimeH.getText().isBlank()) {
                CookTimeH.requestFocus();
                CookTimeH.setStyle(errorStyle);
            }else if (CookTimeM.getText().isBlank()) {
                CookTimeM.requestFocus();
                CookTimeM.setStyle(errorStyle);
            } else if (TotalTimeH.getText().isBlank()){
                TotalTimeH.requestFocus();
                TotalTimeH.setStyle(errorStyle);
            } else if (TotalTimeM.getText().isBlank()){
                TotalTimeM.requestFocus();
                TotalTimeM.setStyle(errorStyle);
            }else if (Ingredients.getText().isBlank()) {
                Ingredients.requestFocus();
                Ingredients.setStyle(errorStyle);
            } else if (Directions.getText().isBlank()) {
                Directions.requestFocus();
                Directions.setStyle(errorStyle);
            }
        }
        else{
            RecipeDTO new_recipe = new RecipeDTO(
                        RecipeTitle.getText(),
                        "", //add id
                        0, // at the beginning it has no reviews
                        Application.authenticatedUser.getUsername(),
                        PreparationTimeH.getText()+" h "+PreparationTimeM.getText()+" m",
                        CookTimeH.getText()+" h "+CookTimeH.getText()+" m",
                        TotalTimeH.getText()+" h "+TotalTimeM.getText()+" m",
                        Ingredients.getText(),
                        Directions.getText(),
                        return_list_of_ingredient(Ingredients.getText()),
                        null
                );
                String result = RecipeDao.addRecipe(new_recipe);
                if (result.equals("error")) {
                    invalidRecipe.setText("Recipe adding is failed! retry later");
                    invalidRecipe.setStyle(errorMessage);
                } else {
                    invalidRecipe.setText("Add Successful!");
                    invalidRecipe.setStyle(successMessage);
                }
            }
    }

    private void clearErrorStyle() {
        RecipeTitle.setStyle(successStyle);
        PreparationTimeM.setStyle(successStyle);
        PreparationTimeH.setStyle(successStyle);
        CookTimeH.setStyle(successStyle);
        CookTimeM.setStyle(successStyle);
        TotalTimeH.setStyle(successStyle);
        TotalTimeM.setStyle(successStyle);
        Ingredients.setStyle(successStyle);
        Directions.setStyle(successStyle);
        invalidRecipe.setText("");
    }

    private String[] return_list_of_ingredient(String list){

        String array[] = list.split(",");
        String[] return_list_of_ingredient = new String[array.length];
        for(int i=0; i<array.length;i++){
            String supporto[] = array[i].split(":");
            return_list_of_ingredient[i] = supporto[0];
        }
        return return_list_of_ingredient;
    }

    @FXML
    private void goback() throws IOException, InterruptedException {
        Application.changeTab(1);
    }


    public void setOver(MouseEvent mouseEvent) {
        Utils.setOver(mouseEvent);
    }

    public void unsetOver(MouseEvent mouseEvent) {
        Utils.unsetOver(mouseEvent);
    }

    public void setClick(MouseEvent mouseEvent) {
        Utils.setClick(mouseEvent);
    }

    public void unsetClick(MouseEvent mouseEvent) {
        Utils.unsetClick(mouseEvent);
    }
}


