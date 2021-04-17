package com.server.Models.GameModel;

/**
 * A class to represent coordinates in the game world. The game coordinate system is Cartesian, so the lower-left corner
 * is the origin.
 */
public class GamePosn {
    /**
     * The x-coordinate.
     */
    double x;

    /**
     * The y-coordinate.
     */
    double y;

    /**
     * Constructor:
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public GamePosn(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-value.
     * @return the x-value
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-value.
     * @return the y-value
     */
    public double getY() {
        return y;
    }

    public GamePosn addVector(GameVector vec) {
        return new GamePosn(
                this.x + vec.getX(),
                this.y + vec.getY());
    }

    public double distance(GamePosn other) {
        return Math.hypot(this.x - other.x, this.y - other.y);
    }

    public double[] toJson() {
        return new double[]{this.x, this.y};
    }
}
