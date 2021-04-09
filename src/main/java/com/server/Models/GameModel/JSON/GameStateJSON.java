package com.server.Models.GameModel.JSON;

public class GameStateJSON {
  private String status;
  private double t;
  private PlayerJSON[] players;
  private BulletJSON[][] bullets;

  public GameStateJSON(String status, double t, PlayerJSON[] players, BulletJSON[][] bullets) {
    this.status = status;
    this.t = t;
    this.players = players;
    this.bullets = bullets;
  }

  public String getStatus() {
    return status;
  }

  public double getT() {
    return t;
  }

  public PlayerJSON[] getPlayers() {
    return players;
  }

  public BulletJSON[][] getBullets() {
    return bullets;
  }
}