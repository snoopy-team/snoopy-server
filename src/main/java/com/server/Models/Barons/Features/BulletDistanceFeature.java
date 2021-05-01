package com.server.Models.Barons.Features;

import com.server.Configuration.Constants;
import com.server.Models.GameModel.GamePosn;
import com.server.Models.GameModel.GameState;

import java.util.List;
import java.util.stream.Collectors;

public class BulletDistanceFeature implements Feature<List<Double>> {
  /**
   * Extracts a feature from the given GameState from the perspective of the given player ID.
   *
   * @param state the game state
   * @param id    the ID of the player to calculate the feature for
   * @return the feature
   */
  @Override
  public List<Double> extract(GameState state, int id) {
    GamePosn playerPos = new PositionFeature().extract(state, id);
    // the player radius + bullet radius as a fraction of the larger dimension: the smallest distance that can
    // cause a problem
    double leeway = (Constants.BULLET_RADIUS + Constants.PLAYER_RADIUS) / Math.max(Constants.WIDTH,
            Constants.HEIGHT);
    return (new BulletsFeature().extract(state, id)).stream()
            .map(posn -> new GamePosn(posn.getX() / state.getMatch().getWidth(),
                    posn.getY() / state.getMatch().getHeight()))
            .map(posn -> posn.distance(playerPos) - leeway)
            .collect(Collectors.toList());
  }
}