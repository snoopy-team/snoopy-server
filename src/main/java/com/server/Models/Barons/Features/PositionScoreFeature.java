package com.server.Models.Barons.Features;

import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;

/**
 * Feature that scores based on agents being roughly in the middle, horizontally, and being away from the bottom
 * (with a cap so it doesn't hug the ceiling). Higher is better.
 */
public class PositionScoreFeature implements Feature<Double> {
    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID. Higher is better.
     *
     * @param state the game state
     * @param id    the ID of the player to calculate the feature for
     * @return the feature
     */
    @Override
    public Double extract(GameState state, int id) {
        GamePosn posnScaled = new PositionFeature().extract(state, id);
        // ideal x-value in the middle: take difference, square, and get negative for correct sign
        // now between 0 and 1
        double xComp = 1 - 4 * Math.pow(posnScaled.getX() - 0.5, 2.0);

        // ideal y-value is high, with sharp drop-off near bottom, and ends at 2/3s the max height
        // worst possible value is log 0.01 = -4.6
        double yComp = Math.log(2 / 3.0 - Math.abs(posnScaled.getY() - 2 / 3.0) + 0.001);

        return xComp + yComp;
    }
}
