package com.server.Models.GameModel.JSON;

import java.util.Map;

public class GameStateJSON {
  private String status;
  private double t;
  private Map<Integer, PlayerJSON> players;
  private Map<Integer, BulletJSON[]> bullets;

  public GameStateJSON(String status, double t, Map<Integer, PlayerJSON> players, Map<Integer,
          BulletJSON[]> bullets) {
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

  public Map<Integer, PlayerJSON> getPlayers() {
    return players;
  }

  public Map<Integer, BulletJSON[]> getBullets() {
    return bullets;
  }
}