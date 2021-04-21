package com.server.Models.GameModel;

/**
 * Class for storing global game variables not related to the physics engine, such as the sizes of game objects, the
 * turning speed, and weapons specifications.
 */
public class GameConfig {
    /**
     * The turning speed, in radians per game second.
     */
    final double turnSpeed;

    /**
     * The bullet radius in game units.
     */
    final double bulletRadius;

    /**
     * The bullet speed in game units per second.
     */
    final double bulletSpeed;

    /**
     * The player radius in game units.
     */
    final double playerRadius;

    /**
     * The cooldown between bullet firing, in game seconds.
     */
    final double bulletCooldown;

    final int numBullets;
    final double bulletSpread;

    public GameConfig(double turnSpeed, double bulletRadius, double bulletSpeed, double playerRadius,
                      double bulletCooldown, int numBullets, double bulletSpread) {
        this.turnSpeed = turnSpeed;
        this.bulletRadius = bulletRadius;
        this.bulletSpeed = bulletSpeed;
        this.playerRadius = playerRadius;
        this.bulletCooldown = bulletCooldown;
        this.numBullets = numBullets;
        this.bulletSpread = bulletSpread;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public double getBulletRadius() {
        return bulletRadius;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public double getPlayerRadius() {
        return playerRadius;
    }

    public double getBulletCooldown() {
        return bulletCooldown;
    }

    public int getNumBullets() {
        return numBullets;
    }

    public double getBulletSpread() {
        return bulletSpread;
    }
}
