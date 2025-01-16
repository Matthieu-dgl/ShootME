package com.matthieudeglon.shootme.Models;

import com.matthieudeglon.shootme.Constants.Constants;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

public class Bonus extends PicturedObject {

    private final Timeline _waitingTime;
    private final GameMapModel _map;

    /* Constructor */
    public Bonus(Pane simulationRoot, GameMapModel map, String url, int nRows, int nCols, int numberOfFrames, Pair<Double, Double> scalingFactor) {
        super(scalingFactor, url, nRows, nCols);
        _map = map;

        applyCustomScaleToObject(Constants.HEART_SCALE);


        _waitingTime = waitSomeTimeBeforeDisplayingBonusAgain();
        createAndStartAnimation(numberOfFrames, nCols);

        generate();

        simulationRoot.getChildren().add(this);

    }

    /* Movement & action management */
    private void pushInsideBorder() {
        positionTo(new CoordinatesModel(getInMapXPosition(), getInMapYPosition()));
    }

    private double getInMapXPosition() {
        return getCurrentXPosition() - getScaledWidth();
    }

    private double getInMapYPosition() {
        return getCurrentYPosition() - getScaledHeight();
    }

    @Override
    public void action(Character S) {
        if (intersect(S)) bonus_effect(S);
    }

    private void bonus_effect(Character S) {
        emptyPaneFromImageView(this);
        S.getHBar().restoreLife();
        generate();
    }

    public void generate() {
        positionTo(_map.getRandomLocation());
        if (getHitbox().isOutOfMap(_map)) pushInsideBorder();
        _waitingTime.play();

    }

    /* Utils */
    private void emptyPaneFromImageView(Pane P) {
        P.getChildren().removeIf(i -> i instanceof ImageView);
    }

    /* Animations */
    private void createAndStartAnimation(int frames, int n_cols) {
        ObjectAnimation anim = new ObjectAnimation((ImageView) getPicture(), Duration.seconds(1), frames, n_cols, 0, 0, get_width(), get_height());
        anim.setCycleCount(Animation.INDEFINITE);
        anim.play();
    }

    private Timeline waitSomeTimeBeforeDisplayingBonusAgain() {
        return new Timeline(new KeyFrame(Duration.seconds(Constants.BONUS_COOLDOWN),
                event -> {
                    emptyPaneFromImageView(this);
                    addNodes(getPicture());
                }));
    }

}

