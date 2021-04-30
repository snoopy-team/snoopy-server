package com.server.Models.Barons;

import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A Baron that simply tries to maximize a given evaluation function with its play. Called "simple" because it
 * ignores the opponent: this halves the branching factor and vastly decreases the complexity. Always fires.
 */
public class SimpleSearchBaron implements IBaron {
    /**
     * The value function.
     */
    Evaluator valueFunc;

    /**
     * The maximum depth to search. 1 just uses the eval function every frame, the minimum value.
     */
    int maxDepth;

    public SimpleSearchBaron(Evaluator valueFunc, int maxDepth) {
        this.valueFunc = valueFunc;
        this.maxDepth = maxDepth;
    }

    private static ArrayList<ArrayList<Action>> getPossibleActions() {
        ArrayList<ArrayList<Action>> possibleActions = new ArrayList<>();
        for (int turn = -1; turn <= 1; turn++) {
            for (int thrust = 0; thrust <= 1; thrust++) {
                ArrayList<Action> actions = new ArrayList<>();
                if (turn == -1) {
                    actions.add(Action.BankLeft);
                } else if (turn == 0) {
                    // do nothing
                } else {
                    actions.add(Action.BankRight);
                }
                if (thrust == 1) {
                    actions.add(Action.Thrust);
                }
                // always fire
                actions.add(Action.Fire);

                possibleActions.add(actions);
            }
        }
        return possibleActions;
    }

    /**
     * Steps forward, only using this player's actions and assuming nothing for anyone else.
     * @param state the state
     * @param id the ID of us
     * @param actions the actions to take
     * @return the new state
     */
    private GameState stepAI(GameState state, int id, ArrayList<Action> actions) {
        Map<Integer, ArrayList<Action>> inputs = new HashMap<>();
        inputs.put(id, actions);
        for (int i = 0; i < state.getPlayers().size(); i++) {
            if (i != id) {
                inputs.put(i, new ArrayList<>());
            }
        }
        GameState newState = new GameState(state);
        newState.stepMany(inputs, 3 / 32.0, 3);
        return newState;
    }

    /**
     * Generates a list of actions the AI will take given a gamestate.
     *
     * @param state The current state of the game.
     * @param id    The id of the player being controlled by the AI.
     * @return List of actions for the gamestate to take.
     */
    @Override
    public Iterable<Action> getActions(GameState state, int id) {
        return getPossibleActions()
                .stream()
                .max(Comparator.comparingDouble(actions -> this.getBestValue(this.stepAI(state, id, actions), id,
                        maxDepth)))
                .orElseThrow();
    }

    private double getBestValue(GameState state, int id, int maxDepth) {
        if (state.isOver()) {
            int WIN_VAL = 1000;
            return (state.getWinningPlayer() == id) ? WIN_VAL : -3 * WIN_VAL;
        } else if (maxDepth == 0) {
            // reached end of tree
            return this.valueFunc.evaluate(state, id);
        } else {
            return getPossibleActions()
                    .stream()
                    .map(actions -> this.getBestValue(this.stepAI(state, id, actions), id, maxDepth - 1))
                    .max(Comparator.naturalOrder())
                    .orElseThrow();
        }
    }
}
