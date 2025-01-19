package com.matthieudeglon.shootme.Views;

import com.matthieudeglon.shootme.Database.Database;
import com.matthieudeglon.shootme.Menu.Menu;
import com.matthieudeglon.shootme.Customs.CustomCheckedException;
import com.matthieudeglon.shootme.Customs.CustomSettings;
import com.matthieudeglon.shootme.Models.Character;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import java.util.Timer;
import java.util.TimerTask;

public class WinnerWindow extends Menu {
    private final Character _player;
    Timer timer;

    public WinnerWindow(Character player) {
        super();
        _player = player;
        timer = new Timer();
    }

    @Override
    public void createContent() {
        ImageView winnerImage, fireworks;
        winnerImage = tryToRetrievePlayerImage();
        fireworks = tryToRetrieveFireworksImage();

        addCentralImageView(fireworks, 0.9, 0.9);
        addCentralImageView(winnerImage, 0.9, 0.9);

        addSecondaryTitle("The winner is " + _player.getPlayerName());
        addFlashDisclaimer("<press any key to continue>");

        Database.incrementVictories(_player.getPlayerName());

        waitAndPressToContinue(1);
    }

    private ImageView tryToRetrieveFireworksImage() {
        ImageView fireworks;
        try {
            fireworks = Menu.retrieveImage(CustomSettings.URL_FIREWORKS, 1, 1);
        } catch (CustomCheckedException.FileManagementException e) {
            System.err.println(e.toString() + " Fireworks image not found. Using alternative one. Continuing");
            fireworks = new ImageView(new Rectangle(10, 10).snapshot(null, null));
        }
        return fireworks;
    }

    private ImageView tryToRetrievePlayerImage() {
        ImageView winnerImage;
        try {
            winnerImage = Menu.retrieveImage(_player.getPicture().getImage().getUrl(), 4, 1);
        } catch (CustomCheckedException.FileManagementException e) {
            System.err.println(e.toString() + " Winner sprite image not found. Using alternative one. Continuing");
            winnerImage = new ImageView(new Rectangle(10, 10).snapshot(null, null));
        }
        return winnerImage;
    }

    private void waitAndPressToContinue(double seconds) {
        TimerTask task2 = new TimerTask() {
            public void run() {
                getSceneFromStage().addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
                    GameMenu new_menu = new GameMenu();
                    try {
                        new_menu.start(getStage());
                        timer.cancel();
                    } catch (CustomCheckedException.MissingMenuComponentException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };

        timer.schedule(task2, (long) (1000 * seconds));
    }
}

