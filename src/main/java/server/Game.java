package main.java.server;

import main.java.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
    private GameState gameState;
    private List<GamePlayer> players;
    private int newPlayerId;

    // Constants; should probably change these to read from a file or something
    final static double THRUST_POWER = 0;
    final static double GRAVITY_STRENGTH = 0;
    final static double DRAG_FACTOR = 0;

    final static double TURN_SPPED = 0;
    final static double BULLET_RADIUS = 0;
    final static double BULLET_SPEED = 0;
    final static double PLAYER_RADIUS = 0;
    final static double BULLET_COOLDOWN = 0;

    final GamePosn STARTING_POSITION = new GamePosn(0, 0); // Potentially we wanna randomize this
    final double STARTING_ORIENTATION = 0;

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    public Game() {
        this.gameState =
                new GameState(
                        new ArrayList<Player>(),
                        new ArrayList<ArrayList<Bullet>>(),
                        0,
                        new GameConfig(TURN_SPPED, BULLET_RADIUS, BULLET_SPEED, PLAYER_RADIUS, BULLET_COOLDOWN),
                        new PhysicsModel(THRUST_POWER, GRAVITY_STRENGTH, DRAG_FACTOR),
                        new MatchSetup(WIDTH, HEIGHT)
                );
        this.players = new ArrayList<GamePlayer>();
        this.newPlayerId = 0;
    }

    public GamePlayer addPLayer(SocketWrapper socket) {
        Player newPlayer = new Player(STARTING_POSITION, STARTING_ORIENTATION, this.newPlayerId);
        this.newPlayerId++;
        GamePlayer newGamePlayer = new GamePlayer(socket, newPlayer);

        this.players.add(newGamePlayer);
        this.gameState.addPlayer(newPlayer);

        return newGamePlayer;
    }

    private class GamePlayer implements Runnable{
        SocketWrapper socket;
        Scanner input;
        PrintWriter output;
        Player player;

        public GamePlayer(SocketWrapper socket, Player player) {
            this.socket = socket;
            this.player = player;
        }

        @Override
        public void run() {
            try {
                setup();
                processRequests();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        }

        private void processRequests() {
            while (input.hasNextLine()) {
                var request = input.nextLine(); // Either wait for the input from the user, or automatically generate an input that means "no input from the user." One of the two
                // Put the request into some kind of shared list of requests
                // I probably need to implement some kind of lock over here
                // If it happens that we've gotten the movements for all players, update the GameState
                // Release the lock
            }
        }
    }
}