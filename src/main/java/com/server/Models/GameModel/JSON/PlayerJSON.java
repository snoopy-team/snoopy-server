package com.server.Models.GameModel.JSON;

public class PlayerJSON {
  double[] position;
  double[] velocity;
  double[] acceleration;
  double orientation;
  double cooldown;

  public PlayerJSON(double[] position, double[] velocity, double[] acceleration, double orientation,
                    double cooldown) {
    this.position = position;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.orientation = orientation;
    this.cooldown = cooldown;
  }

  public double[] getPosition() {
    return position;
  }

  public double[] getVelocity() {
    return velocity;
  }

  public double[] getAcceleration() {
    return acceleration;
  }

  public double getOrientation() {
    return Rounder.round(orientation);
  }

  public double getCooldown() {
    return Rounder.round(cooldown, 5);
  }
}
