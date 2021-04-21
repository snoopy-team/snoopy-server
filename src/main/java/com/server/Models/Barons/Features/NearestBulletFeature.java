package com.server.Models.Barons.Features;

import com.server.Models.GameModel.Bullet;
import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;

import java.util.List;
import java.util.Map;

/**
 * Feature that extracts the distance to the nearest bullet that can harm the player.
 */
public class NearestBulletFeature implements Feature<Double> {

    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID.
     * If there is no bullet on the screen, returns the diagonal length of the canvas (i.e., the farthest possible
     * distance)
     *
     * @param state the game state
     * @param id    the ID of the player to calculate the feature for
     * @return the feature
     */
    @Override
    public Double extract(GameState state, int id) {
        GamePosn posn = state.getPlayers().get(id).getPosn();
        double minDist = Math.hypot(state.getMatch().getWidth(), state.getMatch().getHeight());
        for (Map.Entry<Integer, ? extends List<Bullet>> entry : state.getPlayerBullets().entrySet()) {
            if (entry.getKey() != id) {
                for (Bullet bullet : entry.getValue()) {
                    double dist = bullet.getBody().getCenter().distance(posn);
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
            }
        }
        return minDist;
    }
}
