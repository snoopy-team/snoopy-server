package main.java.server;

import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * A server for our snoopy dogfighting game, that can handle up to 200 players. Players connect at will, not necessarily just at the start of the game.
 */
public class SnoopyServer {
    private final static int NUM_PLAYERS = 200;

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Snoopy Server is Running...");
            var pool = Executors.newFixedThreadPool(NUM_PLAYERS);

            Phaser barrier = new Phaser(0);
            Game game = new Game(NUM_PLAYERS, barrier);

            while (true) {
                pool.execute(game.addPlayer(new ServerSocketWrapper(listener.accept())));
            }
        }
    }
}
