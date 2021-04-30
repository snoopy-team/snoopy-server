package com.server.Models.Barons;

import com.server.Models.Barons.Features.BulletDistanceFeature;
import com.server.Models.Barons.Features.OrientationFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;
import com.server.Models.GameModel.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluator used for Q learning.
 */
public class QEvaluator implements Evaluator {
    // The weight for bullet distance.
    public double bulletDist;
    // The weight for orientation.
    public double orientation;
    // The weight for position.
    public double position;
    // The weight for our positional score against the opponents: more is more reckless
    public double courage;

    public QEvaluator(double bulletDist, double orientation, double position, double courage) {
        this.bulletDist = bulletDist;
        this.orientation = orientation;
        this.position = position;
        this.courage = courage;
    }


    /**
     * Evaluates the game state, returning a value where positive is good.
     *
     * @param state the game state
     * @param id    the player to evaluate from the perspective of
     * @return the value
     */
    @Override
    public double evaluate(GameState state, int id) {
        ArrayList<Double> scores = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Double> bullets = new BulletDistanceFeature().extract(state, i);
            double angle = new OrientationFeature().extract(state, i);
            double positionScore = new PositionScoreFeature().extract(state, i);

            // take log of bullet distance to magnify the importance of ones closer to you
            double bulletScore = bullets.stream().mapToDouble(Math::log).sum();

            scores.add((bulletScore * this.bulletDist) + (angle * this.orientation) + positionScore * this.position);
        }
        return this.courage * scores.get(id) - scores.get(1 - id);
    }
}
