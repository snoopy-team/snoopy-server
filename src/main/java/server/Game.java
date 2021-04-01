package main.java.server;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import main.java.*;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A game of snoopy dogfight, which keeps track of the GameState and players.
 */
public class Game {
    private final GameState gameState;
    private final List<GamePlayer> players;
    private final LoAction[] actions;
    private final HashSet<Integer> availableIds;

    // Constants; should probably change these to read from a file or something
    final static double THRUST_POWER = 0;
    final static double GRAVITY_STRENGTH = 0;
    final static double DRAG_FACTOR = 0;

    final static double TURN_SPEED = 0;
    final static double BULLET_RADIUS = 0;
    final static double BULLET_SPEED = 0;
    final static double PLAYER_RADIUS = 0;
    final static double BULLET_COOLDOWN = 0;

    final GamePosn STARTING_POSITION = new GamePosn(0, 0); // Potentially we wanna randomize this
    final double STARTING_ORIENTATION = 0;

    // Constants mapping keys to actions
    final static String BANK_LEFT = "ArrowLeft";
    final static String BANK_RIGHT = "ArrowRight";
    final static String THRUST = "ArrowUp";
    final static String FIRE = " ";

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    // Tools for concurrency
    private final Phaser barrier;
    private final Lock lock;

    public Game(int maxPlayers, Phaser barrier) {
        this.gameState =
                new GameState(
                        new ArrayList<Player>(),
                        new ArrayList<ArrayList<Bullet>>(),
                        0,
                        new GameConfig(TURN_SPEED, BULLET_RADIUS, BULLET_SPEED, PLAYER_RADIUS, BULLET_COOLDOWN),
                        new PhysicsModel(THRUST_POWER, GRAVITY_STRENGTH, DRAG_FACTOR),
                        new MatchSetup(WIDTH, HEIGHT)
                );
        this.players = new ArrayList<GamePlayer>();
        this.actions = new LoAction[maxPlayers];
        Arrays.fill(actions, null);
        this.availableIds = new HashSet<Integer>();
        for (int i = 0; i < maxPlayers; i++)
        {
            this.availableIds.add(i);
        }

        this.barrier = barrier;
        this.lock = new ReentrantLock();
    }

    /**
     * Adds a player to the game and attaches it to the given socket.
     *
     * @param socket The socket that the new player is at.
     * @return The new GamePlayer.
     */
    public GamePlayer addPLayer(SocketWrapper socket) {
        int playerId = this.getNewPlayerId();

        Player newPlayer = new Player(STARTING_POSITION, STARTING_ORIENTATION, playerId);
        GamePlayer newGamePlayer = new GamePlayer(socket, newPlayer, playerId);

        this.players.add(newGamePlayer);
        this.gameState.addPlayer(newPlayer);

        return newGamePlayer;
    }

    /**
     * Returns what the new player id should be by getting the next empty slot in the array.
     */
    private int getNewPlayerId()
    {
        for (int i = 0; i < this.actions.length; i++)
        {
            if (this.availableIds.contains(i))
            {
                this.availableIds.remove(i);
                return i;
            }
        }

        throw new IllegalStateException("No new players allowed!");
    }

    /**
     * A LoAction is a list of action that acts as a wrapper for ArrayList. The only reason this exists is because of
     * Java's rules around not having arrays over objects with generic types.
     */
    private static class LoAction implements Iterable<Action>
    {
        ArrayList<Action> actionArrayList;

        public LoAction(ArrayList<Action> actionArrayList)
        {
            this.actionArrayList = actionArrayList;
        }

        @Override
        public Iterator<Action> iterator() {
            return actionArrayList.iterator();
        }
    }

    /**
     * A GamePlayer represents a single player in the game. This is created every time a new player connects, and will
     * close itself out when a player disconnects.
     */
    private class GamePlayer implements Runnable{
        SocketWrapper socket;
        Player player;
        int id;

        Scanner input;
        PrintWriter output;

        public GamePlayer(SocketWrapper socket, Player player, int id) {
            this.socket = socket;
            this.player = player;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                setup();
                processRequests();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                exit();
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        }

        private void processRequests() {
            barrier.register();
            while (input.hasNextLine()) {
                String request = "";
                TimeLimiter timeLimiter = SimpleTimeLimiter.create(Executors.newCachedThreadPool());
                try {
                    request = timeLimiter.callWithTimeout(input::nextLine, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException | UncheckedTimeoutException e) {
                    // timed out
                } catch (Exception e) {
                    // something bad happened while reading the line
                }

                // Put the request into some kind of shared list of requests
                String[] keystrokes = request.split(",");

                ArrayList<Action> listOfActions = new ArrayList<Action>();

                for (int i = 0; i < keystrokes.length; i++)
                {
                    listOfActions.add(keyStrokeToAction(keystrokes[i]));
                }

                actions[this.id] = new LoAction(listOfActions);

                // We want everyone to be done with their actions before we send them off
                barrier.arriveAndAwaitAdvance();

                // Lock this because only one thread needs to send this to the gameState
                // If this is called when another thread is already doing the work, just don't do anything
                if (lock.tryLock())
                {
                    // If it happens that we've gotten the movements for all players, update the GameState
                    ArrayList<LoAction> playerActions = new ArrayList<LoAction>();

                    for (int i = 0; i < actions.length; i++)
                    {
                        if (actions[i] != null)
                        {
                            playerActions.add(actions[i]);
                        }
                    }
                    gameState.step(playerActions, 1);

                    lock.unlock();
                }

                // Wait for that one thread who is sending it to the gameState to finish doing that
                barrier.arriveAndAwaitAdvance();

                // Write the output back to the clients so they can see what's going on
                output.print(gameState.toJson());
            }
            barrier.arriveAndDeregister();
        }

        private Action keyStrokeToAction(String keystroke)
        {
            switch (keystroke) {
                case BANK_LEFT:
                    return Action.BankLeft;
                case BANK_RIGHT:
                    return Action.BankRight;
                case THRUST:
                    return Action.Thrust;
                case FIRE:
                    return Action.Fire;
            }
            return null;
        }

        private void exit()
        {
            socket.close();
            players.remove(this);
            availableIds.add(this.id);
        }
    }
}