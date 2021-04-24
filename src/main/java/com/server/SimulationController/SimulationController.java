package com.server.SimulationController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.Models.Barons.IBaron;
import com.server.Models.GameModel.Action;
import com.server.Models.GameModel.GameState;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is similar to the GameController or Game class, but doesn't concern itself with producing frontend
 * output: instead, it logs the game state over time and keeps track of the winner. This means that it can only be
 * used with AI players: it runs the game as fast as it can.
 */
public class SimulationController {
    /**
     * The game state.
     */
    private final GameState gameState;
    /**
     * The list of Barons used to control each respective player.
     */
    private final List<IBaron> agents;
    /**
     * The game seconds per frame used for the simulation.
     */
    private final double dt;
    /**
     * The number of sub-steps used for physics interpolation between each input.
     */
    private final int numSubsteps;

    private static final ObjectMapper jsonFormatter = new ObjectMapper();

    /**
     * Constructor.
     *
     * @param gameState game state
     * @param agents the list of agents: the index of an agent in this list must correspond to its ID in the game
     * @param dt the time to step each input
     * @param numSubsteps the number of physics ticks per set of inputs
     */
    public SimulationController(GameState gameState, List<IBaron> agents, double dt, int numSubsteps) {
        this.gameState = gameState;
        this.agents = agents;
        this.dt = dt;
        this.numSubsteps = numSubsteps;
    }

    /**
     * Constructor that initializes sensible defaults for game state (the default one), dt (1 / 16), and numSteps (2,
     * to keep an internal 32 FPS that is like 30 FPS but without any rounding errors).
     * @param agents the AI agents to use
     */
    public SimulationController(List<IBaron> agents) {
        this.agents = agents;
        this.gameState = new GameState();
        this.dt = 1 / 16.0;
        this.numSubsteps = 2;
    }

    /**
     * Writes the actions taken on this frame and the game state after those actions have been processed to separate
     * output streams as JSON: a new line is used for each set of inputs.
     *
     * @throws IOException if the buffers can't be written to
     */
    public void step(Appendable inputsBuf, Appendable stateBuf) throws IOException {
        var actions = new HashMap<Integer, Iterable<Action>>();
        for (int i = 0; i < this.agents.size(); i++) {
            actions.put(i, agents.get(i).getActions(this.gameState, i));
        }

        try {
            inputsBuf.append(serializeActions(actions));
            inputsBuf.append('\n');
            this.gameState.stepMany(actions, this.dt, this.numSubsteps);
            stateBuf.append(jsonFormatter.writeValueAsString(gameState.toJson()));
            stateBuf.append('\n');
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error serializing game as JSON");
        }
    }

    /**
     * Writes the actions in a space-efficient way: separating each player with a space and listing the one-letter
     * actions.
     * @param actions the actions to serialize: map keys must be 0-n
     * @return the serialized output
     */
    private static CharSequence serializeActions(Map<Integer, Iterable<Action>> actions) {
        var sb = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            var playerActions = actions.get(i);
            for (Action action : playerActions) {
                sb.append(action.toChar());
            }
            sb.append(' ');
        }
        return sb;
    }

    /**
     * Gets the result of the game.
     *
     * @return -1 if p1 lost, 0 if ongoing, 1 if p1 won
     */
    public int result() {
        if (!this.gameState.isOver()) {
            return 0;
        } else {
            return this.gameState.getWinningPlayer() == 0 ? 1 : -1;
        }
    }

    /**
     * Plays the game until completion, writing the inputs and states to the given buffers. Returns 1 if player 1 won,
     * -1 if player 1 lost, and 0 otherwise (which can only happen if there is a timeout.)
     *
     * @throws IOException if the buffers can't be written to
     */
    public int playOut(Appendable inputsBuf, Appendable stateBuf) throws IOException {
        return this.playOut(inputsBuf, stateBuf, Double.POSITIVE_INFINITY);
    }

    /**
     * Plays the game until completion, writing the inputs and states to the given buffers. Stops the game once the game
     * time reaches the given maximum. Returns 1 if player 1 won, -1 if player 1 lost, and 0 otherwise (which can only
     * happen if there is a timeout.)
     *
     * @throws IOException if the buffers can't be written to
     */
    public int playOut(Appendable inputsBuf, Appendable stateBuf, double maxTime) throws IOException {
        while (this.result() == 0 && this.gameState.getT() < maxTime) {
            this.step(inputsBuf, stateBuf);
        }
        return this.result();
    }

    /**
     * Plays a full game using the two agents, logging the output to the given stream and returning the result.
     * @param out the stream to write to: writes the inputs, then a blank line, then the states, then a blank line,
     *            then the result
     * @param agent1 the first AI
     * @param agent2 the second AI
     * @param maxTime the maximum game time before the game is cut short
     * @return the result
     * @throws IOException if the buffer can't be written to
     */
    public static int writeFullGame(Appendable out, IBaron agent1, IBaron agent2, double maxTime) throws IOException {
        var inputs = new StringBuffer();
        var states = new StringBuffer();
        var controller = new SimulationController(List.of(agent1, agent2));
        int result = controller.playOut(inputs, states, maxTime);

        out.append(inputs)
                .append('\n')
                .append(states)
                .append('\n')
                .append(Integer.toString(result));
        return result;
    }

    /**
     * Plays a full game using the two agents, logging the output to the given stream and returning the result.
     * @param out the stream to write to: writes the inputs, then a blank line, then the states, then a blank line,
     *            then the result
     * @param agent1 the first AI
     * @param agent2 the second AI
     * @return the result
     * @throws IOException if the buffer can't be written to
     */
    public static int writeFullGame(Appendable out, IBaron agent1, IBaron agent2) throws IOException {
        return writeFullGame(out, agent1, agent2, Double.POSITIVE_INFINITY);
    }
}
