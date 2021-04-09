package com.server.Models.GameModel;

/**
 * A simple circle.
 */
public class Circle extends ABody {
    /**
     * The center;
     */
    GamePosn center;

    /**
     * The radius.
     */
    double r;

    public Circle(GamePosn center, double r) {
        this.center = center;
        this.r = r;
    }

    public Circle(double x, double y, double r) {
        new Circle(new GamePosn(x, y), r);
    }

    @Override
    boolean collidesWithCircle(Circle other) {
        return this.center.distance(other.center) <= (this.r + other.r);
    }

    @Override
    boolean collidesWithRect(Rect other) {
        // http://www.jeffreythompson.org/collision-detection/circle-rect.php
        double cx = this.center.getX();
        double cy = this.center.getY();
        double rx = other.lowerLeft.getX();
        double ry = other.lowerLeft.getY();

        double testX = cx;
        double testY = cy;
        if (this.center.getX() < rx) {
            testX = rx;
        } else if (this.center.getX() > rx + other.w) {
            testX = rx + other.w;
        }

        if (this.center.getY() < ry) {
            testY = ry;
        } else if (this.center.getY() > ry + other.h) {
            testY = ry + other.h;
        }

        GamePosn testCenter = new GamePosn(testX, testY);
        return this.center.distance(testCenter) <= this.r;
    }

    @Override
    public boolean collidesWith(ABody other) {
        return other.collidesWithCircle(this);
    }

    @Override
    public Rect boundingBox() {
        // lower left offset from center
        GameVector offset = new GameVector(-1, -1).scale(this.r);
        return new Rect(this.center.addVector(offset), 2 * this.r, 2 * this.r);
    }

    @Override
    public ABody moveBy(GameVector vec) {
        return new Circle(this.center.addVector(vec), this.r);
    }

    @Override
    public GamePosn getCenter() {
        return this.center;
    }
}
