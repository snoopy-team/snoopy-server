package com.server.Models.Barons;

import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;

/**
 * Represents an artificial intelligence agent for the game.
 */
public interface IBaron {
  /**
   * Generates a list of actions the AI will take given a gamestate.
   *
   * @param state The current state of the game.
   * @param id The id of the player being controlled by the AI.
   * @return List of actions for the gamestate to take.
   */
  Iterable<Action> getActions(GameState state, int id);
}
