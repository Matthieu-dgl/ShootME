package com.matthieudeglon.shooter2d;

import com.matthieudeglon.shooter2d.Views.GameMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception{

        stage.initStyle(StageStyle.UNDECORATED);
        GameMenu M = new GameMenu();

        M.readProperties();
        M.start(stage);




    }
}