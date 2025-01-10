package com.matthieudeglon.shooter2d.Models;

public class CoordinatesModel {
    private final double x;
    private final double y;

    public CoordinatesModel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() { return "Coordinates{" + "x=" + x + ", y=" + y + '}'; }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

}
