package com.server.Models.Barons.Features;

import com.server.Models.GameModel.GameState;

/**
 * A Feature is a function from a GameState to some value, used as part of an AI.
 */
public interface Feature<T> {
    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID.
     * @param state the game state
     * @param id the ID of the player to calculate the feature for
     * @return the feature
     */
    T extract(GameState state, int id);
}
