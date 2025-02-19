package com.matthieudeglon.shootme.Models;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

public abstract class PicturedObject extends MapObject {

    private final ImageView _picture;
    private double _customScale;

    private final BooleanProperty _toBeRemoved = new SimpleBooleanProperty(false);

    public PicturedObject(Pair<Double, Double> scalingFactors, String url) {
        super(scalingFactors);

        Image image = retrieveImage(url);
        setDimensions(image.getWidth(), image.getHeight());
        _picture = new ImageView(image);
    }

    public PicturedObject(Pair<Double, Double> resolutionScalingFactors, String url, int n_rows, int n_cols) {
        this(resolutionScalingFactors, url);
        setDimensions(get_width() / n_cols, get_height() / n_rows);
    }

    protected static Image retrieveImage(String URL) {
        try {
            return new Image(URL);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("ERROR: Image of PicturedObject was not found. " + e);
            return new Image(URL);
        }
    }


    protected final void scalePicture() {
        this._picture.setFitWidth(_customScale * getResolutionScalingFactors().getKey() * get_width());
        this._picture.setFitHeight(_customScale * getResolutionScalingFactors().getValue() * get_height());
        this._picture.setPreserveRatio(false);
    }

    protected final double getScaledHeight() {
        return _picture.getFitHeight();
    }

    protected final double getScaledWidth() {
        return _picture.getFitWidth();
    }


    protected HitBox getHitbox() {
        return new HitBox(getCurrentYPosition(), getCurrentXPosition(), getScaledWidth(), getScaledHeight());
    }

    protected final boolean intersect(PicturedObject P2) {
        return getHitbox().intersect(P2.getHitbox());
    }

    protected abstract void action(Character S);

    public final ImageView getPicture() {
        return _picture;
    }

    public final boolean hasToBeRemoved() {
        return _toBeRemoved.get();
    }

    public final BooleanProperty geToBeRemovedProperty() {
        return _toBeRemoved;
    }

    public final void applyCustomScaleToObject(double scale) {
        _customScale = scale;
        scalePicture();
    }

    public final void getRemoveProperty(boolean toRemove) {
        _toBeRemoved.set(toRemove);
    }

}
