import main.java.Player;

import java.util.List;

/**
 * The state of the game at a specific time, including the player states and bullet states.
 */
public class GameState {
    List<Player> players;

    List<Bullet> bullets;

    double t;

    public GameState(List<Player> players, List<Bullet> bullets, double t) {
        this.players = players;
        this.bullets = bullets;
        this.t = t;
    }

    /*
    missing here: a step() method, some way of taking in input, and some kind of interface for agents
     */
}
