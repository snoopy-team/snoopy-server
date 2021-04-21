package com.server.Models.Barons.Features;

import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;
import com.server.Models.GameModel.Player;

/**
 * Feature that extracts a score based on the position, on a scale from (0, 1) to (0, 1) with respect to the canvas.
 */
public class PositionFeature implements Feature<GamePosn> {
    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID.
     *
     * @param state the game state
     * @param id    the ID of the player to calculate the feature for
     * @return the feature
     */
    @Override
    public GamePosn extract(GameState state, int id) {
        Player player = state.getPlayers().get(id);
        double x = player.getPosn().getX() / state.getMatch().getWidth();
        double y = player.getPosn().getY() / state.getMatch().getHeight();
        return new GamePosn(x, y);
    }
}
