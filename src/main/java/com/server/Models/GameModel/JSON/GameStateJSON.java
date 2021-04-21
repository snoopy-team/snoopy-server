package com.server.Models.GameModel.JSON;

import java.util.Map;

public class GameStateJSON {
  private String status;
  private double t;
  private Map<Integer, PlayerJSON> players;
  private Map<Integer, BulletJSON[]> bullets;
  private Integer winner;
  private boolean isOver;

  public GameStateJSON(String status, double t, Map<Integer, PlayerJSON> players, Map<Integer,
          BulletJSON[]> bullets, Integer winner, boolean isOver) {
    this.status = status;
    this.t = t;
    this.players = players;
    this.bullets = bullets;
    this.winner = winner;
    this.isOver = isOver;
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

  public Integer getWinner() { return winner; }

  public boolean isOver() { return this.isOver; }
}