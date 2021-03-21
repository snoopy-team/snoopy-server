package main.java.server;

import java.net.ServerSocket;
import java.util.concurrent.Executors;

/**
 * A server for our snoopy dogfighting game, that handles two players at a time,
 */
public class SnoopyServer {
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Snoopy Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            Game game = new Game();
            while (true) {
                pool.execute(game.addPLayer(new ServerSocketWrapper(listener.accept())));
            }
        }
    }
}
