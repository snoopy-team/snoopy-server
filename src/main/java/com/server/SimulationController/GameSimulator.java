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
        // Evaluator newerQ = new QEvaluator(0.33168439,  2.48415166, 1.51821935, 2.0);
        Evaluator handQ1 = new QEvaluator(4.0, 0.5, 2.0, 0.3);
        Evaluator handQ2 = new QEvaluator(4.0,  1.0, 2.0, 0.0);
        Evaluator newQ = new QEvaluator(0.4701161 ,  0.27848007,  3.06952571, 3.0);
        Evaluator newQ2 = new QEvaluator(-0.4701161 ,  0.27848007,  3.06952571, 4.0);
        // Evaluator qEval = new QEvaluator(-1.16, 0.49, 5.86, 2.0);
        IBaron agent1 = new SimpleSearchBaron(basicEval2, 3);
        IBaron agent2 = new SimpleSearchBaron(newQ, 1);

        // play N_GAMES pairs of games with flipped starting positions
        int N_GAMES = 25 * 2;
        double MAX_TIME = 50;

        try {
            int agent1Wins = 0;
            for (int i = 1; i < N_GAMES; i += 2) {
                long seed = new Random().nextLong();
                System.out.println("Playing game " + i + "...");
                FileWriter out1 = new FileWriter("replays/game" + i + ".snoopy");
                int result1 = SimulationController.writeFullGame(out1, agent1, agent2, MAX_TIME, seed);
                System.out.println("Finished game " + i + ". Result: " + result1);
                agent1Wins += result1;
                out1.close();
                System.out.println("Playing game " + (i + 1) + "...");
                FileWriter out2 = new FileWriter("replays/game" + (i + 1) + ".snoopy");
                int result2 = SimulationController.writeFullGame(out2, agent2, agent1, MAX_TIME, seed);
                agent1Wins -= result2;
                System.out.println("Finished game " + (i + 1) + ". Result: " + (-result2));
                out2.close();
            }
            System.out.println("Number of wins for agent 1: " + agent1Wins);
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
}
