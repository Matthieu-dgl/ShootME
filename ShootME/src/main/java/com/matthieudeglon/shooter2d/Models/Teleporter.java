package com.matthieudeglon.shooter2d.Models;

import com.matthieudeglon.shooter2d.Constants.Constants;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

public class Teleporter extends PicturedObject {
    private CoordinatesModel destination;

    public Teleporter(Pane simulationRoot, String url, GameMapModel M, Pair<Double, Double> scalingFactor, String ID) {
        super(scalingFactor, url);

        applyCustomScaleToObject(Constants.TELEPORT_SCALE);

        addNodes(getPicture());

        rotationAnimation();

        positionTo(M.get_position_of(ID));

        simulationRoot.getChildren().add(this);
    }


    /* Collisions & action management */
    @Override
    public HitBox getHitbox() {
        return new HitBox(getCurrentYPosition() + (getScaledHeight() * .25), getCurrentXPosition() + (getScaledWidth() * .25),
                getScaledWidth() * .5, getScaledHeight() * .5);
    }

    @Override
    public void action(Character S) {
        if (intersect(S)) S.positionTo(destination);
    }

    protected final void setDestination(Teleporter T) {
        destination = new CoordinatesModel(T.getCurrentXPosition() + getScaledWidth() / 4.0, T.getCurrentYPosition() + getScaledHeight());
    }


    /* Animations */
    private void rotationAnimation() {
        var rotation = new RotateTransition(Duration.millis(Constants.TELEPORT_TIME_TO_ROTATE), this);
        rotation.setByAngle(360);
        rotation.setInterpolator(Interpolator.LINEAR);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.play();
    }


}
