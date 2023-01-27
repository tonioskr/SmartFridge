package com.example.SmartFridge.Utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Error {

    public Error(Exception me) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button e = new Button("EXIT");
        Text t = new Text(("CRITICAL ERROR: " + me.getMessage()));
        t.setWrappingWidth(200);
        VBox vbox = new VBox(t,e);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        dialogStage.setWidth(300);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
        e.setOnAction(w->{
            System.exit(1);
        });
        dialogStage.setOnCloseRequest(w->{
            System.exit(1);
        });
    }
}
