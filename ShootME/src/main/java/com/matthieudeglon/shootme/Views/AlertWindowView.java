package com.matthieudeglon.shootme.Views;

import com.matthieudeglon.shootme.Menu.Menu;
import com.matthieudeglon.shootme.Customs.CustomCheckedException;
import com.matthieudeglon.shootme.Customs.CustomSettings;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class AlertWindowView extends Menu {
    double _candidateWidth;
    double _candidateHeight;
    String _candidateColorMode;

    AlertWindowView(Menu otherMenu, double candidateWidth, double candidateHeight, String candidateColorMode) {
        super(otherMenu);
        _candidateWidth = candidateWidth;
        _candidateHeight = candidateHeight;
        _candidateColorMode = candidateColorMode;
    }

    @Override
    public void createContent() throws CustomCheckedException.MissingMenuComponentException {
        ImageView alertImage;
        try {
            alertImage = Menu.retrieveImage(CustomSettings.URL_WARNING_ICON, 1, 1);
        } catch (CustomCheckedException.FileManagementException e) {
            System.out.println(e.toString() + " Alert image not found. Using alternative one. Continuing");
            alertImage = new ImageView(new Rectangle(10, 10).snapshot(null, null));
        }

        addCentralImageView(alertImage, 0.7, 0.7);
        addSecondaryTitle("CAUTION!");
        addFreeItem("BACK", 0.05, 0.2);
        addFreeItem("CONTINUE", 0.76, 0.2);
        addFlashDisclaimer("Game will be reset. Do you want to confirm?");

        getItem("BACK").setOnMouseReleased(event -> {
            OptionsMenu optionsMenu = new OptionsMenu(this);
            tryToStart(optionsMenu);
        });

        getItem("CONTINUE").setOnMouseReleased(event -> {
            setStageDimensions(_candidateWidth, _candidateHeight);
            setColorMode(_candidateColorMode);
            tryToWrite();
            OptionsMenu optionsMenu = new OptionsMenu(this);
            tryToStart(optionsMenu);
        });
    }

    private void tryToWrite() {
        try {
            writeSettings();
        } catch (CustomCheckedException.FileManagementException e) {
            System.err.println(e.toString() + " Writing was wrong. Continuing.");
        }
    }

}

