package com.server.Models.GameModel;

import java.util.stream.Stream;

/**
 * An axis-aligned rectangle with a lower-left corner, width, and height.
 */
public class Rect extends ABody {
    /**
     * The lower-left corner's location.
     */
    GamePosn lowerLeft;

    /**
     * The width.
     */
    double w;

    /**
     * The height.
     */
    double h;

    public Rect(GamePosn lowerLeft, double w, double h) {
        this.lowerLeft = lowerLeft;
        this.w = w;
        this.h = h;
    }

    public Rect(double x, double y, double w, double h) {
        new Rect(new GamePosn(x, y), w, h);
    }

    @Override
    boolean collidesWithCircle(Circle other) {
        return other.collidesWithRect(this);
    }

    @Override
    boolean collidesWithRect(Rect other) {
        // http://www.jeffreythompson.org/collision-detection/rect-rect.php
        return ((this.lowerLeft.getX() + this.w >= other.lowerLeft.getX()) ||
                (this.lowerLeft.getX() <= other.lowerLeft.getX() + other.w) ||
                (this.lowerLeft.getY() + this.h >= other.lowerLeft.getY()) ||
                (this.lowerLeft.getY() <= other.lowerLeft.getY() + other.h));
    }

    @Override
    public boolean collidesWith(ABody other) {
        return other.collidesWithRect(this);
    }

    @Override
    public Rect boundingBox() {
        return this;
    }

    /**
     * Returns an iterator containing each of the four corners.
     * @return the corners as an iterator
     */
    public Iterable<GamePosn> getCorners() {
        return () -> Stream.of(this.lowerLeft,
                this.lowerLeft.addVector(new GameVector(this.w, 0)),
                this.lowerLeft.addVector(new GameVector(0, this.h)),
                this.lowerLeft.addVector(new GameVector(this.w, this.h))).iterator();
    }

    @Override
    public ABody moveBy(GameVector vec) {
        return new Rect(this.lowerLeft.addVector(vec), this.w, this.h);
    }

    @Override
    public GamePosn getCenter() {
        return this.lowerLeft.addVector(new GameVector(this.w / 2, this.h / 2));
    }
}
