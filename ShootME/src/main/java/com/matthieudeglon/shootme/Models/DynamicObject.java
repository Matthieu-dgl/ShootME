package com.matthieudeglon.shootme.Models;

import com.matthieudeglon.shootme.Direction;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

public abstract class DynamicObject extends PicturedObject {

    private final ObjectProperty<Direction> _currentDirection = new SimpleObjectProperty<>();
    private double _speed;
    private double _deltaX, _deltaY;


    public DynamicObject(Pair<Double, Double> scalingFactors, String url) {
        super(scalingFactors, url);
    }

    public DynamicObject(Pair<Double, Double> scalingFactors, String url, int n_rows, int n_cols) {
        super(scalingFactors, url, n_rows, n_cols);
    }

    protected void setSpeed(double speed) {
        _speed = speed;
    }

    protected final double getFutureX() {
        return getCurrentXPosition() + getDeltaX() * getResolutionScalingFactors().getKey();
    }

    protected final double getFutureY() {
        return getCurrentYPosition() + getDeltaY() * getResolutionScalingFactors().getValue();
    }

    protected final CoordinatesModel getFutureCoordinates() {
        return new CoordinatesModel(getFutureX(), getFutureY());
    }


    public abstract boolean checkIfPassable(Tile t);

    protected final HitBox getDefaultMoveBox() {
        return new HitBox(getFutureY(), getFutureX(), getScaledWidth(), getScaledHeight());
    }

    protected HitBox getMoveBox() {
        return getDefaultMoveBox();
    }


    protected abstract void defaultMovement(GameMapModel M);

    protected final boolean illegalMove(GameMapModel M) {

        if (getDefaultMoveBox().isOutOfMap(M)) return true;

        var collision_box = getMoveBox();
        collision_box.compute_tiles_bounds(M);

        return collision_box.checkIfObjectCanMoveOnNeighboursTiles(M, this);

    }

    public final void setDeltaX(double deltaX) {
        _deltaX = deltaX;
    }

    public final void setDeltaY(double deltaY) {
        _deltaY = deltaY;
    }

    public final void set_currentDirection(Direction currentDirection) {
        _currentDirection.set(currentDirection);
    }

    public final double getSpeed() {
        return _speed;
    }

    public final double getDeltaX() {
        return _deltaX;
    }

    public final double getDeltaY() {
        return _deltaY;
    }

    public final Direction getCurrentDirection() {
        return _currentDirection.get();
    }

    public final ObjectProperty<Direction> getCurrentDirectionProperty() {
        return _currentDirection;
    }


}
