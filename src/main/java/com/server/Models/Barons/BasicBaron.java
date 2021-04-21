package com.server.Models.Barons;

import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;

import java.util.ArrayList;

/**
 * Default artificial intelligence agent for the game. Does nothing.
 */
public class BasicBaron implements IBaron {

  @Override
  public Iterable<Action> getActions(GameState state, int id) {
    return new ArrayList<>();
  }
}
