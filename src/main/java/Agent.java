package main.java;

/**
 * Represents a mechanism for controlling a player.
 */
public interface Agent {
    /**
     * Computes the next action for the given player in the given game state.
     * @param state the game state
     * @param playerIndex the index in the players list of the given state that this agent is for
     * @return a list of actions to take in the next time interval
     */
    Iterable<Action> computeNextActions(GameState state, int playerIndex);
}
