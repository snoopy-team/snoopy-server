package com.server.Models.Barons;

import com.server.Models.Barons.Features.Feature;
import com.server.Models.GameModel.GameState;

import java.util.List;

/**
 * Simple linear valuation function: takes a list of features and a list of weights and does a dot product.
 */
public class LinearEvaluator implements Evaluator {
    List<Feature<Double>> features;
    List<Double> weights;

    public LinearEvaluator(List<Feature<Double>> features, List<Double> weights) {
        this.features = features;
        this.weights = weights;
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
        double score = 0;
        for (int i = 0; i < this.features.size(); i++) {
            Feature<Double> feat = this.features.get(i);
            double weight = this.weights.get(i);

            score += weight * feat.extract(state, id);
        }
        return score;
    }
}
