package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.RecipeDTO;
import com.example.SmartFridge.DTO.ReviewDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllCommentsController {
    @FXML
    private ScrollPane Box;
    @FXML
    private VBox content;
    private Integer called_times_reviews = 0;
    private final Integer how_much_comments = 20;
    public static RecipeDTO Recipe;
    @FXML
    private Button showmore;
    private ReviewDTO[] review;
    Integer page = 0;

    public void printSingleComment(VBox content, ReviewDTO[] r, Integer i)
    {
        String name = "Username: " + r[i].getProfile() + "\t \t \t" + "Rate: " + Integer.toString(r[i].getRate());
        Label profile = new Label(name);
        profile.setStyle("-fx-font-size: 15;");
        content.setPrefHeight(content.getPrefHeight() + profile.getPrefHeight());
        content.getChildren().add(profile);

        if(Application.authenticatedUser.getUsername().equals("admin"))
        {
            Button button = new Button("Delete");
            button.setOnAction(event->{

                int index = content.getChildren().indexOf(event.getSource());

                //the following three lines to get the username of the user who commented the recipe
                String my_str = String.valueOf(content.getChildren().get(index-1)); //return 'Username: "some_user_name"\t\t\tRate:"some_number"'
                my_str = my_str.replace("\t", ":").replace(": ", ":");
                String username = my_str.split(":")[1];
                int rate = Integer.parseInt(my_str.split(":")[5].replace("'", ""));

                content.getChildren().remove(index-1);
                content.getChildren().remove(index-1);
                content.getChildren().remove(index-1);

                 List<ReviewDTO> reviews_list = Arrays.asList(Recipe.getReviews());
                ArrayList<ReviewDTO> list = new ArrayList<>(reviews_list);

                for(int j=0; j<list.size();j++) {
                    if (list.get(j).getProfile().equals(username)) {
                        list.remove(j);
                        break;
                    }
                }

                Recipe.setReviews(list.toArray(new ReviewDTO[list.size()]));

                RecipeDao.removeReviews(Recipe,username,rate);

            });
            content.getChildren().add(button);
        }

        TextArea field = new TextArea(r[i].getComment());
        field.setEditable(false);
        field.setMinHeight(Double.parseDouble("100"));

        Integer charachters = r[i].getComment().length();
        Integer division = (charachters > 500)? 5 : (charachters > 300)? 3 : (charachters < 70) ? 1 : 2;

        field.setMinWidth(Double.parseDouble("400"));
        field.setStyle("-fx-font-size: 14;");
        field.setCursor(Cursor.HAND);


        field.setWrapText(true);
        content.getChildren().add(field);
        content.setFillWidth(true);
        content.setSpacing(10);
        content.setPadding(new Insets(0,210,0,210));
    }

    public void show_more(ReviewDTO[] r)
    {
        try {
            Integer i = how_much_comments * page;
            if (i >= r.length) {
                return;
            }
            while (i < r.length && (i - page * how_much_comments) < how_much_comments) {
                printSingleComment(content, r, i);
                i++;
            }
            page++;
        }
         catch (Exception e){
            System.out.println(e);
         }
    }

    public void create_button(String name, String id, ReviewDTO[] r)
    {
        Button button = new Button(name);
        button.setId(id);

        button.setOnAction(event -> {
            if(name.equals("Back"))
                goBack();
            else if(name.equals("Show more"))
                show_more(r);
        });

        content.getChildren().add(button);
    }

    public void printComments(ReviewDTO[] r) {
        //create_button("Back", "Back", r);
        //create_button("Show more", "Show_more",r );
        show_more(r);
    }

    public void initialize(){
        page=0;
        review = Recipe.getReviews();

        content = new VBox();
        Box.setContent(content);

        //printComments(review);
        show_more(review);

    }

    public void goBack()
    {
        page=0;
        try {
            ViewRecipeController.Recipe = Recipe;
            Recipe = null;
            if(Application.authenticatedUser.getUsername().equals("admin"))
                Application.changeScene("AllRecipes");
            else
                Application.changeScene("MainTable");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showclick(ActionEvent actionEvent) {
        show_more(review);
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
}
