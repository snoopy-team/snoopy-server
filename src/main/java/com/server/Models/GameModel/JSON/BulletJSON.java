package com.server.Models.GameModel.JSON;

public class BulletJSON {
  final double[] position;
  final double[] velocity;

  public BulletJSON(double[] position, double[] velocity) {
    this.position = position;
    this.velocity = velocity;
  }

  public double[] getPosition() {
    return position;
  }

  public double[] getVelocity() {
    return velocity;
  }
}
