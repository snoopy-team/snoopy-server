package main.java;

import com.google.gson.Gson;

import java.io.Reader;

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

    public GameConfig(double turnSpeed, double bulletRadius, double bulletSpeed, double playerRadius,
                      double bulletCooldown) {
        this.turnSpeed = turnSpeed;
        this.bulletRadius = bulletRadius;
        this.bulletSpeed = bulletSpeed;
        this.playerRadius = playerRadius;
        this.bulletCooldown = bulletCooldown;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public double getBulletRadius() {
        return bulletRadius;
    }

    public double getPlayerRadius() {
        return playerRadius;
    }

    public double getBulletCooldown() {
        return bulletCooldown;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    /**
     * Reads in config values from the JSON file.
     * @param input the input reader
     * @return a GameConfig initialized from the values
     * @throws IllegalArgumentException if the JSON given is bad or missing values
     */
    public static GameConfig fromJson(Reader input) throws IllegalArgumentException {
        Gson gson = new Gson();
        return gson.fromJson(input, GameConfig.class);
    }
}
