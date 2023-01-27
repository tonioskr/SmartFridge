package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.aggregationsMongo;
import com.example.SmartFridge.DTO.AggregationTransportDTO;
import com.example.SmartFridge.DTO.IngredientDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class AnalyticsController {
    @FXML
    private GridPane left3;
    @FXML
    private GridPane left2;
    @FXML
    private PieChart piechart;
    final private ObservableList<PieChart.Data> datapie= FXCollections.observableArrayList();
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
    public void initialize(){
        piechart.setData(datapie);
        ingredientsByCountry();
    }

    public void show_most_10_ingredients() {

        ArrayList<AggregationTransportDTO> result = aggregationsMongo.top10Ingredients();
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left2.getChildren().clear();
        left3.getChildren().clear();
        printAnalytics("INGREDIENT","TOTAL USAGE",0);
        int i=1;
        datapie.clear();
        piechart.setTitle("Top 10 most ingredients:");
        for(AggregationTransportDTO  r : result){
            if(i < 10)
                datapie.add(new PieChart.Data(r.getField1(),r.getField3()));
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }
    public void show_most_10_recipes() {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.top10votedrecipe();
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left2.getChildren().clear();
        left3.getChildren().clear();
        printAnalytics("RECIPE","AVERAGE RATE",0);
        int i=1;
        datapie.clear();
        piechart.setTitle("Top 5 recipes:");
        for(AggregationTransportDTO  r : result){
            if(i < 6)
                datapie.add(new PieChart.Data(r.getField1(),r.getField3()));
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    public void show_userMostCommented() {

        ArrayList<AggregationTransportDTO> result = aggregationsMongo.userMostCommented();
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left2.getChildren().clear();
        left3.getChildren().clear();
        printAnalytics("USERNAME","TOTAL COMMENTS",0);
        int i=1;
        datapie.clear();
        piechart.setTitle("Top 10 most active user:");
        for(AggregationTransportDTO  r : result){
            if(i < 10)
                datapie.add(new PieChart.Data(r.getField1(),r.getField3()));
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    public void ingredientsByCountry() {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.ingredientsByCountry();
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left2.getChildren().clear();
        left3.getChildren().clear();
        printAnalytics_2("COUNTRY","INGREDIENT","QUANTITY",0);
        int i=1;
        //result.sort(result);

        datapie.clear();
        piechart.setTitle("");
        for(AggregationTransportDTO  r : result){
            if(i<6)
                datapie.add(new PieChart.Data(r.getField1(),r.getField3()));
            printAnalytics_2(r.getField1(),r.getField2(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    private void printAnalytics_2(String field1, String field2,String field3, int index) {
        final Label label = new Label(field1);
        Label labelText = new Label(field2);
        Label label2 = new Label(field3);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 16;");
        labelText.setStyle("-fx-font-size: 15;");

        GridPane.setRowIndex(label, index);

        GridPane.setRowIndex(labelText, index);
        GridPane.setColumnIndex(labelText, 1);

        GridPane.setRowIndex(label2, index);
        GridPane.setColumnIndex(label2, 2);


        left3.getChildren().add(label);
        left3.getChildren().add(labelText);
        left3.getChildren().add(label2);
        left3.setHalignment(label, HPos.LEFT);
        left3.setHalignment(labelText, HPos.LEFT);
        left3.setHalignment(label2, HPos.LEFT);
    }

    public void printAnalytics(String field1, String field2, int index){
            final Label label = new Label(field1);
            Label labelText = new Label(field2);
            label.setStyle("-fx-font-weight: bold;-fx-font-size: 16;");
            labelText.setStyle("-fx-font-size: 15;");

            GridPane.setRowIndex(label, index);

            GridPane.setRowIndex(labelText, index);
            GridPane.setColumnIndex(labelText, 1);


            left2.getChildren().add(label);
            left2.getChildren().add(labelText);
            left2.setHalignment(label, HPos.LEFT);
            left2.setHalignment(labelText, HPos.LEFT);
    }

    @FXML
    private void goBack() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

}
