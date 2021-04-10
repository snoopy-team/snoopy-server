package com.server.Models;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.server.Configuration.Constants;
import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;
import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.JSON.GameStateJSON;
import com.server.Models.GameModel.Player;
import com.server.Models.GameModel.Bullet;
import com.server.Models.GameModel.GameConfig;
import com.server.Models.GameModel.PhysicsModel;
import com.server.Models.GameModel.MatchSetup;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
  private final GameState gameState;

  public Game(Player player) {
    // We'll initialize the AI around here
    var players = new ArrayList<Player>();
    players.add(player);

    var bulletLists = new ArrayList<ArrayList<Bullet>>();
    bulletLists.add(new ArrayList<>());

    this.gameState =
        new GameState(
        players,
        bulletLists,
        0,
        new GameConfig(Constants.TURN_SPEED, Constants.BULLET_RADIUS, Constants.BULLET_SPEED,
                Constants.PLAYER_RADIUS, Constants.BULLET_COOLDOWN),
        new PhysicsModel(Constants.THRUST_POWER, Constants.GRAVITY_STRENGTH, Constants.DRAG_FACTOR),
        new MatchSetup(Constants.WIDTH, Constants.HEIGHT)
      );
  }

  public GameStateJSON step(List<Action> actions) {
    List<List<Action>> actionList = new ArrayList<>();
    actionList.add(actions);
    // Add the AI actions here too
    this.gameState.stepMany(actionList, 1, 1);

    return this.gameState.toJson();
  }
}