package main.java.server;

import java.net.ServerSocket;
import java.util.concurrent.Executors;

/**
 * A server for our snoopy dogfighting game, that can handle up to 200 players. Players connect at will, not necessarily just at the start of the game.
 */
public class SnoopyServer {
    private final static int NUM_PLAYERS = 200;

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Snoopy Server is Running...");
            var pool = Executors.newFixedThreadPool(NUM_PLAYERS);
            Game game = new Game(NUM_PLAYERS);
            while (true) {
                pool.execute(game.addPLayer(new ServerSocketWrapper(listener.accept())));
            }
        }
    }
}
