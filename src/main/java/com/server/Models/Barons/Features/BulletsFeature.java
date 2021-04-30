package com.server.Models.Barons.Features;

import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;

import java.util.List;
import java.util.stream.Collectors;

// Gets enemy bullet positions as a list.
public class BulletsFeature implements Feature<List<GamePosn>> {

    /**
     * Extracts a feature from the given GameState from the perspective of the given player ID.
     *
     * @param state the game state
     * @param id    the ID of the player to calculate the feature for
     * @return the feature
     */
    @Override
    public List<GamePosn> extract(GameState state, int id) {
        // Java is cooler than it used to be!
        return state.getPlayerBullets().entrySet().stream()
                .filter(entry -> entry.getKey() != id)
                .flatMap(entry -> entry.getValue().stream())
                .map(bullet -> bullet.getBody().getCenter())
                .collect(Collectors.toList());
    }
}
