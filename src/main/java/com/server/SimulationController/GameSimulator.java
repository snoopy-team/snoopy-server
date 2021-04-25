package com.server.SimulationController;

import com.server.Models.Barons.*;
import com.server.Models.Barons.Features.NearestBulletFeature;
import com.server.Models.Barons.Features.OrientationFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Wrapper application class to easily play out a game between two AIs and log the results to a file.
 */
public class GameSimulator {
    public static void main(String[] args) {
        Evaluator basicEval = new LinearEvaluator(
                List.of(new NearestBulletFeature(), new PositionScoreFeature()),
                List.of(1.0, 20.0));
        Evaluator basicEval2 = new LinearEvaluator(
                List.of(new NearestBulletFeature(), new PositionScoreFeature(), new OrientationFeature()),
                List.of(3.0, 20.0, 10.0));
        IBaron agent1 = new SimpleSearchBaron(basicEval, 3);
        IBaron agent2 = new SimpleSearchBaron(basicEval2, 3);

        int N_GAMES = 10;
        double MAX_TIME = 500;

        try {
            for (int i = 1; i <= N_GAMES; i++) {
                System.out.println("Playing game " + i + "...");
                FileWriter out = new FileWriter("replays/game" + i + ".snoopy");
                int result = SimulationController.writeFullGame(out, agent1, agent2, MAX_TIME);
                System.out.println("Finished game " + i + ". Result: " + result);
                out.close();
            }
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
}
