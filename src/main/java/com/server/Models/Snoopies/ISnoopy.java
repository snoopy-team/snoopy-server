package com.server.Models.Snoopies;

import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;

/**
 * Represents an artificial intelligence agent for the game.
 */
public interface ISnoopy {
  /**
   * Generates a list of actions the AI will take given a gamestate.
   *
   * @param state The current state of the game.
   * @return List of actions for the gamestate to take.
   */
  Iterable<Action> getActions(GameState state);
}
