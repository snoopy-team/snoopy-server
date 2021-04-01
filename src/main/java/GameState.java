package main.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The state of the game at a specific time, including the player states and bullet states.
 */
public class GameState { ;
    List<Player> players;

    List<? extends List<Bullet>> playerBullets;

    double t;

    GameConfig config;

    PhysicsModel physics;

    MatchSetup match;

    boolean isOver;

    public GameState(List<Player> players, List<? extends List<Bullet>> playerBullets, double t,
                     GameConfig config, PhysicsModel physics, MatchSetup match) {
        this.players = players;
        this.playerBullets = playerBullets;
        this.t = t;
        this.config = config;
        this.physics = physics;
        this.match = match;
        this.isOver = false;
    }

    public GameConfig getConfig() {
        return this.config;
    }

    public PhysicsModel getPhysics() {
        return this.physics;
    }

    public boolean isOver() {
        return this.isOver;
    }
    /**
     * Advances the game state using the given inputs for the agents.
     * @param actions a list, of the same length as the players, giving the inputs for each player
     * @param dt the time (in game seconds) to play forward
     */
    public void step(Iterable<? extends Iterable<Action>> actions, double dt) {
        /*
        move players first, then bullets, then check collision
        this tends to be generous: you can outrun a bullet that occupies the spot where you were the frame you leave it
         */
        int playerInd = 0;
        for (Iterable<Action> actionList : actions) {
            Player currPlayer = this.players.get(playerInd);
            playerInd++;
            currPlayer.takeActions(actionList, this, dt);
        }

        this.moveBullets(dt);
        this.collideBullets();
        this.t += dt;
    }

    /**
     * Moves each bullet forward the specified amount of time, removing bullets that no longer exist.
     */
    private void moveBullets(double dt) {
        int w = this.match.getWidth();
        int h = this.match.getHeight();
        this.playerBullets.forEach(bullets -> bullets.forEach(bullet -> bullet.move(dt)));
        this.playerBullets.forEach(bullets -> bullets.removeIf(bullet -> !bullet.isInBounds(w, h)));
    }

    /**
     * Checks for bullet collisions, updating the game status accordingly.
     */
    private void collideBullets() {
        /*
        loop through each player and check the bullets from every other player
        the reason we're not just ignoring bullet ownership is that otherwise you'd die the instant you fired
         */
        for (int playerInd = 0; playerInd < this.players.size(); playerInd++) {
            // this is where tuning for performance should happen
            Bullets bullets = new GridBullets(
                    20, 20,
                    this.match.getWidth(), this.match.getHeight());
            for (int oppInd = 0; oppInd < this.players.size(); oppInd++) {
                if (oppInd != playerInd) {
                    bullets.addAll(this.playerBullets.get(oppInd)
                            .stream()
                            .map(Bullet::getBody)
                            .collect(Collectors.toList()));
                }
                if (bullets.hasCollision(this.players.get(playerInd).getBody(this.getConfig()))) {
                    this.endGame();
                }
            }
        }
    }

    private void endGame() {
        this.isOver = true;
    }

    /**
     * Fires a bullet at the given position in the given direction.
     * @param shooterId the ID of the player shooting the bullet (the index in the players list)
     * @param posn the spot to fire a bullet at
     * @param orientation the orientation in which to fire the bullet (in radians)
     */
    public void fire(int shooterId, GamePosn posn, double orientation) {
        this.playerBullets.get(shooterId).add(this.makeBullet(posn, orientation));
    }

    public Bullet makeBullet(GamePosn posn, double orientation) {
        ABody body = new Circle(posn, this.getConfig().getBulletRadius());
        return new Bullet(body, GameVector.fromPolar(this.getConfig().getBulletSpeed(), orientation));
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        if (this.isOver()) {
            json.addProperty("status", "over");
        } else {
            json.addProperty("status", "ongoing");
        }

        json.addProperty("t", this.t);

        JsonArray players = new JsonArray();
        for (Player p : this.players) {
            players.add(p.toJson());
        }
        json.add("players", players);

        JsonArray bullets = new JsonArray();
        for (List<Bullet> bulletList : this.playerBullets) {
            JsonArray pBullets = new JsonArray();
            for (Bullet b : bulletList) {
                pBullets.add(b.toJson());
            }
            bullets.add(pBullets);
        }
        json.add("bullets", bullets);

        return json;
    }

    /**
     * Adds a player to this GameState..
     * @param player the player to add to the GameState
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }
}
