package com.server.SimulationController;

import com.server.Models.Barons.Evaluator;
import com.server.Models.Barons.Features.NearestBulletFeature;
import com.server.Models.Barons.Features.OrientationFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;
import com.server.Models.Barons.IBaron;
import com.server.Models.Barons.LinearEvaluator;
import com.server.Models.Barons.SimpleSearchBaron;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Wrapper application class to easily play out a game between two AIs and log the results to a file.
 */
public class GameSimulator {
    public static void main(String[] args) {
        Evaluator basicEval2 = new LinearEvaluator(
                List.of(new NearestBulletFeature(), new PositionScoreFeature(), new OrientationFeature()),
                List.of(3.0, 20.0, 10.0));
        IBaron agent1 = new SimpleSearchBaron(basicEval2, 3);
        IBaron agent2 = new SimpleSearchBaron(basicEval2, 3);

        // play N_GAMES pairs of games with flipped starting positions
        int N_GAMES = 20 * 2;
        double MAX_TIME = 50;

        try {
            for (int i = 1; i < N_GAMES; i += 2) {
                long seed = new Random().nextLong();
                System.out.println("Playing game " + i + "...");
                FileWriter out1 = new FileWriter("replays/game" + i + ".snoopy");
                int result1 = SimulationController.writeFullGame(out1, agent1, agent2, MAX_TIME, seed);
                System.out.println("Finished game " + i + ". Result: " + result1);
                out1.close();
                System.out.println("Playing game " + (i + 1) + "...");
                FileWriter out2 = new FileWriter("replays/game" + (i + 1) + ".snoopy");
                int result2 = SimulationController.writeFullGame(out2, agent2, agent1, MAX_TIME, seed);
                System.out.println("Finished game " + (i + 1) + ". Result: " + (-result2));
                out2.close();
            }
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
}
