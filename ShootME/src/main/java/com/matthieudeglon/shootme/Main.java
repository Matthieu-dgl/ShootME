package com.matthieudeglon.shootme;

import com.matthieudeglon.shootme.Database.Database;
import com.matthieudeglon.shootme.Views.GameMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Database.createNewDatabase();
        Database.createTable();

        stage.initStyle(StageStyle.UNDECORATED);
        GameMenu M = new GameMenu();

        M.readProperties();
        M.start(stage);
    }
}