package com.server.Models.Barons;

import com.server.Models.GameModel.GameState;

/**
 * Describes a value function that takes in a game state and outputs a score, where higher is better.
 */
public interface Evaluator {
    /**
     * Evaluates the game state, returning a value where positive is good.
     *
     * @param state the game state
     * @param id the player to evaluate from the perspective of
     * @return the value
     */
    double evaluate(GameState state, int id);
}
