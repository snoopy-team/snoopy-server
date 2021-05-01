package com.server.Models;

import com.server.Configuration.Constants;
import com.server.Models.Barons.Evaluator;
import com.server.Models.Barons.Features.NearestBulletFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;
import com.server.Models.Barons.IBaron;
import com.server.Models.Barons.LinearEvaluator;
import com.server.Models.Barons.QEvaluator;
import com.server.Models.Barons.SimpleSearchBaron;
import com.server.Models.Barons.StallBaron;
import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;
import com.server.Models.GameModel.JSON.GameStateJSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
  private final GameState gameState;
  private final IBaron snoopy;

  public Game() {
    Evaluator newQ = new QEvaluator(-0.4701161 ,  0.27848007,  3.06952571, 2.0);
    this.snoopy = new SimpleSearchBaron(newQ, 2);
    // this.snoopy = new StallBaron();

    this.gameState = new GameState();
  }

  public GameStateJSON step(int playerId, List<Action> actions) {
    Map<Integer, Iterable<Action>> actionList = new HashMap<>();
    actionList.put(playerId, actions);
    actionList.put(Constants.AI_INDEX, this.snoopy.getActions(this.gameState, Constants.AI_INDEX));

    this.gameState.stepMany(actionList, 1 / 30.0, 3);

    GameStateJSON json = this.gameState.toJson();

    return this.gameState.toJson();
  }
}