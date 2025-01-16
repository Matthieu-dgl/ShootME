package com.matthieudeglon.shootme.Models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class Tile extends ImageView {

    private final CoordinatesModel tilePixelPosition;

    private final boolean passableForPlayer;
    private final boolean passableForProjectile;

    Tile(double posX, double posY, double width, double height, boolean PassableForPlayers, boolean notPassableForProjectile, Image tileSet, Rectangle2D portionOfTileSet) {
        relocate(posX, posY);

        setFitWidth(width);
        setFitHeight(height);

        tilePixelPosition = new CoordinatesModel(posX, posY);

        passableForPlayer = PassableForPlayers;
        passableForProjectile = !notPassableForProjectile;

        setImage(tileSet);
        setViewport(portionOfTileSet);
        setPreserveRatio(false);

    }

    public CoordinatesModel getPixelPositionOfTheTile() {
        return this.tilePixelPosition;
    }

    public boolean isPassableForPlayer() {
        return passableForPlayer;
    }

    public boolean isPassableForProjectile() {
        return passableForProjectile;
    }

}