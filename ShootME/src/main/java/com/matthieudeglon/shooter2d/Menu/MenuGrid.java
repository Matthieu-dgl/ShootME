package com.matthieudeglon.shooter2d.Menu;

import com.matthieudeglon.shooter2d.Customs.CustomCheckedException;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

import java.util.Map;

class MenuGrid extends GridPane {

    public MenuGrid() {
        final double scaleWidth = 0.8;
        final double scaleHeight = 0.95;

        setMaxSize(scaleWidth * Menu.getMenuWidth(), scaleHeight * Menu.getMenuHeight());
        setMinSize(scaleWidth * Menu.getMenuWidth(), scaleHeight * Menu.getMenuHeight());
        setTranslateX((1 - scaleWidth) / 2. * Menu.getMenuWidth());
        setTranslateY((1 - scaleHeight) / 2. * Menu.getMenuHeight());
        setHgap(0.3 * Menu.getMenuHeight());
        setVgap(0.05 * Menu.getMenuHeight());
        setAlignment(Pos.CENTER);
    }

    public void addChoiceBox(String name, int rowWithinGrid, int columnWithinGrid, Map<String, String> mapImageToUrl, double imageScale, int numberOfRowsSpritesheet) {
        final ChoiceboxView choice_box = new ChoiceboxView(name, mapImageToUrl, numberOfRowsSpritesheet, imageScale);
        add(choice_box, columnWithinGrid, rowWithinGrid);
    }

    public void addChoiceBox(String name, int rowWithinGrid, int columnWithinGrid, Map<String, String> mapImageToUrl, double imageScale, int numberOfRowsSpritesheet, int defaultIndex) throws CustomCheckedException.IndexOutOfRangeException {
        final ChoiceboxView choiceBox = new ChoiceboxView(name, mapImageToUrl, numberOfRowsSpritesheet, imageScale, defaultIndex);
        add(choiceBox, columnWithinGrid, rowWithinGrid);
    }

    public ChoiceBox getChoiceBox(String name) throws CustomCheckedException.MissingMenuComponentException {
        final var choiceBoxObject = (ChoiceBox) getChildren().parallelStream()
                .filter(e -> e instanceof ChoiceBox)
                .filter(e -> ((ChoiceBox) e).getValue().equals(name))
                .findFirst()
                .orElse(null);

        if (choiceBoxObject == null) {
            throw new CustomCheckedException.MissingMenuComponentException(" Choice box with name \"" + name + "\".", ChoiceBox.class);
        } else {
            return choiceBoxObject;
        }
    }

    public void addTextBox(String name, int row, int col, String commandsUrl, int numberOfRowsSpritesheet, double scaleImage, String defaultMessage, String defaultContent) {
        final TextBox textBox = new TextBox(name, commandsUrl, numberOfRowsSpritesheet, scaleImage, defaultMessage, defaultContent);
        add(textBox, col, row);
    }

    public TextBox getTextBox(String name) throws CustomCheckedException.MissingMenuComponentException {
        final var textBoxObject = (TextBox) getChildren().parallelStream()
                .filter(e -> e instanceof TextBox)
                .filter(e -> ((TextBox) e).getName().equals(name))
                .findFirst()
                .orElse(null);

        if (textBoxObject == null) {
            throw new CustomCheckedException.MissingMenuComponentException(" Text box with name \"" + name + "\".", TextBox.class);
        } else {
            return textBoxObject;
        }

    }
}