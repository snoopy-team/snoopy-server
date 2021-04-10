package com.server.Models;

import com.google.gson.JsonElement;

import java.lang.module.Configuration;
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
import com.server.Models.Snoopies.ISnoopy;
import com.server.Models.Snoopies.Snoopy;

import org.apache.tomcat.util.bcel.Const;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
  private final GameState gameState;
  private final ISnoopy snoopy;

  public Game(Player player) {
    this.snoopy = new Snoopy();

    var players = new ArrayList<Player>();
    players.add(new Player(Constants.AI_STARTING_POSITION, Constants.AI_STARTING_ORIENTATION,
            Constants.AI_INDEX));
    players.add(player);

    var bulletLists = new ArrayList<ArrayList<Bullet>>();
    bulletLists.add(new ArrayList<Bullet>());
    bulletLists.add(new ArrayList<Bullet>());

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
    List<List<Action>> actionList = new ArrayList<List<Action>>();
    actionList.add(actions);

    // Add the AI actions here too
    this.gameState.step(actionList, 1);

    return this.gameState.toJson();
  }
}