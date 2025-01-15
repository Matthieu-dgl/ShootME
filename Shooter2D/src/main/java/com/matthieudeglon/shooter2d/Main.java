package com.matthieudeglon.shooter2d;

import com.matthieudeglon.shooter2d.Views.GameMenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception{

        stage.initStyle(StageStyle.UNDECORATED);
        GameMenuView M = new GameMenuView();

        M.readProperties();
        M.start(stage);




    }
}