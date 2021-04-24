package com.server.Models.Barons.Features;

import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;

/**
 * Simple feature that wants to align the AI with their opponent.
 */
public class OrientationFeature implements Feature<Double> {
    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID.
     *
     * @param state the game state
     * @param id    the ID of the player to calculate the feature for
     * @return the feature
     */
    @Override
    public Double extract(GameState state, int id) {
        GamePosn posn = state.getPlayers().get(id).getPosn();;
        GamePosn other = state.getPlayers().get(1 - id).getPosn();
        double angle = state.getPlayers().get(id).getOrientation();

        double currAngle = other.addPosn(posn.scale(-1)).atan();
        return Math.PI * 2 - Math.abs(angle - currAngle);
    }
}
