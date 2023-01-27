package com.example.SmartFridge;

import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    MongoDbDriver mongodb;
    Neo4jDriver neo4j;
    private Stage primaryStage;
    private static Scene primaryScene;

    public static RegisteredUser authenticatedUser;

    @Override
    public void start(Stage stage) throws IOException {

        mongodb = MongoDbDriver.getInstance();
        neo4j = Neo4jDriver.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000 , 700);
        stage.setTitle("Login or Sign up");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        setPrimaryStage(stage);
        setPrimaryScene(scene);
        stage.setOnCloseRequest(windowEvent -> {closing();});
    }

    private void closing(){
        System.out.println("Closing");
        mongodb.close();
        neo4j.close();
    }
    public static void main(String[] args) {
        launch();
    }

    public static void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxml + ".fxml"));
        primaryScene.setRoot(fxmlLoader.load());
    }

    public static void changeTab(int i) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("MainTable.fxml"));
        primaryScene.setRoot(fxmlLoader.load());

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setPrimaryStage(Stage stage)
    {
        this.primaryStage = stage;
    }

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void setPrimaryScene(Scene primaryScene) {
        Application.primaryScene = primaryScene;
    }
    public static void
    setMousePointer(){
        Application.primaryScene.setCursor(Cursor.HAND);
    }
    public static void unSetMousePointer(){
        Application.primaryScene.setCursor(Cursor.DEFAULT);
    }
}