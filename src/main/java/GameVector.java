package main.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * A class to represent vectors such as velocity and acceleration. Contains two signed real numbers in Cartesian
 * coordinates that match the game physics.
 */
public class GameVector {
    /**
     * The x-value.
     */
    double x;

    /**
     * The y-value.
     */
    double y;

    /**
     * The constructor.
     * @param x the x-value
     * @param y the y-value
     */
    public GameVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static GameVector fromPolar(double r, double theta) {
        return new GameVector(r * Math.cos(theta), r * Math.sin(theta));
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

    /**
     * Gets the origin.
     * @return a new copy of the origin, <0, 0>.
     */
    public static GameVector origin() {
        return new GameVector(0, 0);
    }

    /**
     * Scales the vector by the given factor, returning a new scaled version.
     * @param c the constant to scale by
     * @return this vector multiplied by c
     */
    public GameVector scale(double c) {
        return new GameVector(this.x * c, this.y * c);
    }

    /**
     * Adds the other vector using simple vector addition.
     */
    public GameVector add(GameVector other) {
        return new GameVector(this.x + other.x, this.y + other.y);
    }

    /**
     * Gets the norm of the vector.
     */
    public double norm() {
        return Math.hypot(this.x, this.y);
    }

    /**
     * Serializes the vector as an array [x, y].
     * @return the JSON output
     */
    public JsonElement toJson() {
        JsonArray json = new JsonArray();
        json.add(this.x);
        json.add(this.y);
        return json;
    }
}
