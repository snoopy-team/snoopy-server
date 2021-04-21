package com.server.Models.Barons;

import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;
import com.server.Models.GameModel.Player;

import java.util.ArrayList;

/**
 * Baron that "stalls" a third of the height above the ground.
 */
public class StallBaron implements IBaron {
    /**
     * Generates a list of actions the AI will take given a gamestate.
     *
     * @param state The current state of the game.
     * @param id    The id of the player being controlled by the AI.
     * @return List of actions for the gamestate to take.
     */
    @Override
    public Iterable<Action> getActions(GameState state, int id) {
        Player player = state.getPlayers().get(id);
        double bottomLimit = state.getMatch().getHeight() / 3.0;
        ArrayList<Action> actions = new ArrayList<>();
        double eps = 0.01;
        double angle = player.getOrientation();
        if (Math.abs(angle - Math.PI / 2) < eps) {
            // add nothing
        } else if (angle < Math.PI / 2 || angle > 3 * Math.PI / 2) {
            actions.add(Action.BankLeft);
        } else {
            actions.add(Action.BankRight);
        }

        // if below threshold, thrust
        if (player.getPosn().getY() < bottomLimit) {
            actions.add(Action.Thrust);
        }

        return actions;
    }
}
