package com.server.Models;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    var bulletLists = new HashMap<Integer, ArrayList<Bullet>>();
    bulletLists.put(player.getId(), new ArrayList<Bullet>());
    bulletLists.put(Constants.AI_INDEX, new ArrayList<Bullet>());

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

  public GameStateJSON step(int playerId, List<Action> actions) {
    Map<Integer, List<Action>> actionList = new HashMap<>();
    actionList.put(playerId, actions);

//    System.out.println(actions);

    // Add the AI actions here too
    this.gameState.stepMany(actionList, 1 / 30.0, 3);

    return this.gameState.toJson();
  }
}