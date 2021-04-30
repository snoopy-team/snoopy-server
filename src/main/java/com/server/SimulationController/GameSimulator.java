package com.server.SimulationController;

import com.server.Models.Barons.*;
import com.server.Models.Barons.Features.NearestBulletFeature;
import com.server.Models.Barons.Features.OrientationFeature;
import com.server.Models.Barons.Features.PositionScoreFeature;

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
                List.of(15.0, 20.0, 10.0));
        Evaluator newQ = new QEvaluator(-0.4701161 ,  0.27848007,  3.06952571, 2.0);
        Evaluator qEval = new QEvaluator(-1.16, 0.49, 5.86, 2.0);
        IBaron agent1 = new SimpleSearchBaron(newQ, 4);
        IBaron agent2 = new SimpleSearchBaron(newQ, 3);

        // play N_GAMES pairs ofx` games with flipped starting positions
        int N_GAMES = 50 * 2;
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
