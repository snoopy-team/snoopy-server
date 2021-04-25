package com.server.Models.GameModel.JSON;

import java.util.Map;

public class GameStateJSON {
  private final String status;
  private final double t;
  private final Map<Integer, PlayerJSON> players;
  private final Map<Integer, BulletJSON[]> bullets;
  private final Integer winner;
  private final boolean isOver;

  // TODO rename it so that this doesn't say JSON because it isn't
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
    // 7 -> round to nearest 128th for more precision than other doubles
    return Rounder.round(t, 7);
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