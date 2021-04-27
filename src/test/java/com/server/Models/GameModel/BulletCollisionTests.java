package com.server.Models.GameModel;

import com.server.Models.Game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BulletCollisionTests {

  Player player1;
  Player player2;

  GameState gameState;

  Map<Integer, List<Action>> actions;

  private void initializeParameters()
  {
    player1 = new Player(new GamePosn(20, 20), 1, 0);
    player2 = new Player(new GamePosn(80, 80), 1 + Math.PI, 1);
    gameState = new GameState();
    gameState.players = new ArrayList<>();
    gameState.players.add(player1);
    gameState.players.add(player2);
    actions = new HashMap<Integer, List<Action>>();
    actions.put(0, new ArrayList<>());
    actions.put(1, new ArrayList<>());
  }

  /**
   * Tests what happens when a player fires a bullet at another player in a way that should hit
   */
//  @Test
//  public void playerFireTest() {
//    player1 = new Player(new GamePosn(426.625, 600.125), 3, 0);
//    player2 = new Player(new GamePosn(444.375, 760.125), 0, 1);
//    player1.velocity = new GameVector(426.625,600.125);
//    player2.velocity = new GameVector(444.375,760.125);
//
//    gameState = new GameState();
//    gameState.players = new ArrayList<>();
//    gameState.players.add(player1);
//    gameState.players.add(player2);
//
//    actions = new HashMap<Integer, List<Action>>();
//    actions.put(0, new ArrayList<>());
//    actions.put(1, new ArrayList<>());
//
//    ArrayList<Bullet> bullets;
//    bullets = new ArrayList();
//    bullets.add(new Bullet(new Circle(new GamePosn(349.75, 349.75), 0), new GameVector(-415.25, 278.625)));
//    bullets.add(new Bullet(new Circle(new GamePosn(395.125, 395.125), 0), new GameVector(-277, 416.25)));
//    bullets.add(new Bullet(new Circle(new GamePosn(454.25, 454.25), 0), new GameVector(-96.625, 490.625)));
//    bullets.add(new Bullet(new Circle(new GamePosn(518.25, 518.25), 0), new GameVector(98.5, 490.25)));
//    bullets.add(new Bullet(new Circle(new GamePosn(577.375, 577.375), 0), new GameVector(278.625, 415.25)));
//
//    gameState.playerBullets.put(0, bullets);
//    gameState.playerBullets.put(1, bullets);
//    gameState.stepMany(actions, 1/30.0, 2);
//    System.out.println(0);
//    assertTrue(gameState.losingPlayer == player2.id);
//  }

  /**
   * Tests what happens when we put a bullet directly on top of another player
   */
  @Test
  public void bulletInPlayerTests() {
    initializeParameters();

    ArrayList<Bullet> bullets = new ArrayList();
    bullets.add(new Bullet(new Circle(new GamePosn(20, 20), 1), new GameVector(0, 0)));
    gameState.playerBullets.put(0, bullets);
    gameState.playerBullets.put(1, bullets);
    gameState.step(actions, 1/5.0);
    assertTrue(gameState.losingPlayer == player1.id);

    initializeParameters();
    bullets = new ArrayList();
    bullets.add(new Bullet(new Circle(new GamePosn(20 + gameState.getConfig().playerRadius,
            20), 1),
            new GameVector(0, 0)));
    gameState.playerBullets.put(0, bullets);
    gameState.playerBullets.put(1, bullets);
    gameState.step(actions, 1/5.0);
    assertTrue(gameState.losingPlayer == player1.id);

    initializeParameters();
    bullets = new ArrayList();
    bullets.add(new Bullet(new Circle(new GamePosn(20,
            20 + gameState.getConfig().playerRadius), 1),
            new GameVector(0, 0)));
    gameState.playerBullets.put(0, bullets);
    gameState.playerBullets.put(1, bullets);
    gameState.step(actions, 1/5.0);
    assertTrue(gameState.losingPlayer == player1.id);
  }
}
