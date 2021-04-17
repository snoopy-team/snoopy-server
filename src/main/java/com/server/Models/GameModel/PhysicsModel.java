package com.server.Models.GameModel;

import java.io.Reader;

/**
 * Class for storing the physics constants and using them to do calculations.
 */
public class PhysicsModel {
    /**
     * The thrust acceleration in units per game second squared.
     */
    final double thrustPower;

    /**
     * The force of gravity, in units per game second squared. Multiply by this by an object's mass to scale it.
     */
    final double gravityStrength;

    /**
     * A constant to scale the effect of drag, akin to the density of air.
     */
    final double dragFactor;

    /**
     * Constructor.
     * @param thrustPower
     * @param gravityStrength
     * @param dragFactor
     */

    public PhysicsModel(double thrustPower, double gravityStrength, double dragFactor) {
        this.thrustPower = thrustPower;
        this.gravityStrength = gravityStrength;
        this.dragFactor = dragFactor;
    }

    public GameVector getGravity() {
        return new GameVector(0, -this.gravityStrength);
    }

    public GameVector computeDrag(GameVector velocity) {
        double speed = velocity.norm();
        // scale drag by square of speed and invert so it opposes the velocity
        return velocity.scale(-this.dragFactor * speed);
    }

    public GameVector computeThrust(double orientation) {
        return GameVector.fromPolar(this.thrustPower, orientation);
    }
}
