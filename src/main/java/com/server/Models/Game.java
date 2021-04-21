package com.server.Models;

import com.server.Configuration.Constants;
import com.server.Models.Barons.Evaluator;
import com.server.Models.Barons.Features.NearestBulletFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;
import com.server.Models.Barons.IBaron;
import com.server.Models.Barons.LinearEvaluator;
import com.server.Models.Barons.SimpleSearchBaron;
import com.server.Models.GameModel.*;
import com.server.Models.GameModel.JSON.GameStateJSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
  private final GameState gameState;
  private final IBaron snoopy;

  public Game(Player player) {
    Evaluator basicEval = new LinearEvaluator(
            List.of(new NearestBulletFeature(), new PositionScoreFeature()),
            List.of(20.0, 1.0));
    this.snoopy = new SimpleSearchBaron(basicEval, 5);

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
                Constants.PLAYER_RADIUS, Constants.BULLET_COOLDOWN, Constants.NUM_BULLETS, Constants.BULLET_SPREAD),
        new PhysicsModel(Constants.THRUST_POWER, Constants.GRAVITY_STRENGTH, Constants.DRAG_FACTOR),
        new MatchSetup(Constants.WIDTH, Constants.HEIGHT)
      );
  }

  public GameStateJSON step(int playerId, List<Action> actions) {
    Map<Integer, Iterable<Action>> actionList = new HashMap<>();
    actionList.put(playerId, actions);
    actionList.put(Constants.AI_INDEX, this.snoopy.getActions(this.gameState, Constants.AI_INDEX));

//    System.out.println(actions);

    // Add the AI actions here too
    this.gameState.stepMany(actionList, 1 / 30.0, 3);

    return this.gameState.toJson();
  }
}