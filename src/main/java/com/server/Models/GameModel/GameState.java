package com.server.Models.GameModel;

import com.server.Configuration.Constants;
import com.server.Models.GameModel.JSON.BulletJSON;
import com.server.Models.GameModel.JSON.GameStateJSON;
import com.server.Models.GameModel.JSON.PlayerJSON;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The state of the game at a specific time, including the player states and bullet states.
 */
public class GameState {
    List<Player> players;

    Map<Integer, ArrayList<Bullet>> playerBullets;

    double t;

    GameConfig config;

    PhysicsModel physics;

    MatchSetup match;

    boolean isOver;

    Integer winningPlayer;

    Integer losingPlayer;

    public GameState(List<Player> players, Map<Integer, ArrayList<Bullet>> playerBullets,
                     double t, GameConfig config, PhysicsModel physics, MatchSetup match) {
        this.players = players;
        this.playerBullets = playerBullets;
        this.t = t;
        this.config = config;
        this.physics = physics;
        this.match = match;
        this.isOver = false;
        this.winningPlayer = null;
        this.losingPlayer = null;
    }

    /**
     * Initializes using built-in constants for the game config values and player start positions, no bullets, and no
     * elapsed game time.
     */
    public GameState() {
        Player baron = new Player(Constants.AI_STARTING_POSITION, Constants.AI_STARTING_ORIENTATION,
                Constants.AI_INDEX);
        Player snoopy = new Player(Constants.STARTING_POSITION, Constants.STARTING_ORIENTATION,
                Constants.AI_INDEX + 1);
        this.players = List.of(baron, snoopy);

        this.playerBullets = new HashMap<>();
        for (Player player : this.players) {
            this.playerBullets.put(player.id, new ArrayList<>());
        }

        this.t = 0;
        this.config = new GameConfig(Constants.TURN_SPEED, Constants.BULLET_RADIUS, Constants.BULLET_SPEED,
                Constants.PLAYER_RADIUS, Constants.BULLET_COOLDOWN, Constants.NUM_BULLETS, Constants.BULLET_SPREAD);

        this.physics = new PhysicsModel(Constants.THRUST_POWER, Constants.GRAVITY_STRENGTH, Constants.DRAG_FACTOR);
        this.match = new MatchSetup(Constants.WIDTH, Constants.HEIGHT);
        // TODO refactor to use sum type
        this.isOver = false;
        this.winningPlayer = null;
        this.losingPlayer = null;
    }

    /**
     * Puts the players randomly in the middle 2/3 of the field in both x and y axis such that the two players don't
     * initially intersect and with random orientations.
     */
    public static GameState randomState() {
        return GameState.randomState(new Random().nextLong());
    }

    /**
     * Puts the players randomly in the middle 2/3 of the field in both x and y axis such that the two players don't
     * initially intersect and with random orientations.
     */
    public static GameState randomState(long seed) {
        Random rng = new Random(seed);
        boolean isValid = false;
        GamePosn pos1 = null, pos2 = null;
        while (!isValid) {
            pos1 = new GamePosn(
                    (rng.nextDouble() * 2 / 3 + 1 / 6.0) * Constants.WIDTH,
                    (rng.nextDouble() * 2 / 3 + 1 / 6.0) * Constants.HEIGHT);
            pos2 = new GamePosn(
                    (rng.nextDouble() * 2 / 3 + 1 / 6.0) * Constants.WIDTH,
                    (rng.nextDouble() * 2 / 3 + 1 / 6.0) * Constants.HEIGHT);

            isValid = (pos1.distance(pos2) > 5 * Constants.PLAYER_RADIUS);
        }

        double angle1 = pos1.addPosn(pos2.scale(-1)).atan();
        double angle2 = pos2.addPosn(pos1.scale(-1)).atan();
        List<Player> players = List.of(
                new Player(pos1, angle1, Constants.AI_INDEX),
                new Player(pos2, angle2, Constants.AI_INDEX + 1)
        );
        GameState gs = new GameState();
        gs.players = players;
        return gs;
    }

    /**
     * Copy constructor.
     * @param other the other game state
     */
    public GameState(GameState other) {
        this.players = other.players.stream().map(Player::new).collect(Collectors.toList());
        this.playerBullets = new HashMap<>();
        for (Map.Entry<Integer, ? extends List<Bullet>> entry: other.playerBullets.entrySet()) {
            ArrayList<Bullet> newBullets = new ArrayList<>();
            for (Bullet b : entry.getValue()) {
                newBullets.add(new Bullet(b));
            }
            this.playerBullets.put(entry.getKey(), newBullets);
        }
        this.t = other.t;
        this.config = other.config;
        this.physics = other.physics;
        this.match = other.match;
        this.isOver = other.isOver;
        this.winningPlayer = other.winningPlayer;
        this.losingPlayer = other.losingPlayer;
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
    public void step(Map<Integer, ? extends Iterable<Action>> actions, double dt) {
        /*
        move players first, then bullets, then check collision
        this tends to be generous: you can outrun a bullet that occupies the spot where you were the frame you leave it
         */
        for (Integer playerId : actions.keySet())
        {
            Iterable<Action> actionList;
            if (!playerId.equals(this.losingPlayer))
            {
                 actionList = actions.get(playerId);
            }
            else {
                actionList = new ArrayList<>();
            }

            this.players.get(playerId).takeActions(actionList, this, dt);
            this.players.get(playerId).handleBoundaries(this.match.getWidth(), this.match.getHeight());
            // bottom killplane check
            if (this.players.get(playerId).getPosn().getY() < 0) {
                this.losePlayer(playerId);
            }
        }

        this.moveBullets(dt);
        this.collideBullets();
        this.t += dt;
    }

    public void stepMany(Map<Integer, ? extends Iterable<Action>> actions, double dt,
                         int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            this.step(actions, dt / numSteps);
        }
    }

    /**
     * Moves each bullet forward the specified amount of time, removing bullets that no longer exist.
     */
    private void moveBullets(double dt) {
        int w = this.match.getWidth();
        int h = this.match.getHeight();

        for (List<Bullet> bullets : this.playerBullets.values())
        {
            for (Bullet bullet : bullets)
            {
                bullet.move(dt);
            }
        }

//        this.playerBullets.values().forEach(bullets -> bullets.forEach(bullet -> bullet.move(dt)));

        for (List<Bullet> bullets : this.playerBullets.values())
        {
            ArrayList<Bullet> toRemove = new ArrayList<>();
            for (Bullet bullet : bullets)
            {
                if (!bullet.isInBounds(w, h))
                {
                    toRemove.add(bullet);
                }
            }
            // To avoid concurrent modification
            for (Bullet bullet : toRemove)
            {
                bullets.remove(bullet);
            }
        }

//        this.playerBullets.values().forEach(bullets -> bullets.removeIf(bullet -> !bullet.isInBounds(w,
//                h)));
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
                    3, 3,
                    this.match.getWidth(), this.match.getHeight());
//            Bullets bullets = new NaiveBullets();
            for (int oppInd = 0; oppInd < this.players.size(); oppInd++) {
                if (oppInd != playerInd) {
                    bullets.addAll(this.playerBullets.get(oppInd)
                            .stream()
                            .map(Bullet::getBody)
                            .collect(Collectors.toList()));
                }
            }
            if (bullets.hasCollision(this.players.get(playerInd).getBody(this.getConfig()))) {
                this.losePlayer(playerInd);
            }
        }
    }



    public double getT() {
        return t;
    }

    public Integer getWinningPlayer() {
        return winningPlayer;
    }

    private void losePlayer(int losingPlayer) {
        this.winningPlayer = 1 - losingPlayer;
        this.losingPlayer = losingPlayer;
        this.isOver = true;
    }

    private void endGame()
    {
        if (this.players.get(losingPlayer).isDead())
        {
            this.isOver = true;
        }
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
        return new Bullet(body, GameVector.fromPolar(this.getConfig().getBulletSpeed(),
                orientation));
    }

    public GameStateJSON toJson() {
        String status;

        if (this.isOver()) {
            status = "over";
        } else {
            status = "ongoing";
        }

        Map<Integer, PlayerJSON> players = new HashMap<>();
        for (Player player : this.players) {
            players.put(player.getId(), player.toJson());
        }

        Map<Integer, BulletJSON[]> bullets = new HashMap<>();

        for (Integer playerId : this.playerBullets.keySet())
        {
            List<Bullet> bulletList = this.playerBullets.get(playerId);
            BulletJSON[] bulletJSONS = new BulletJSON[bulletList.size()];
            for (int i = 0; i < bulletList.size(); i++)
            {
                bulletJSONS[i] = bulletList.get(i).toJson();
            }
            bullets.put(playerId, bulletJSONS);
        }

        return new GameStateJSON(status, this.t, players, bullets, this.winningPlayer,
                this.isOver);
    }

    /**
     * Adds a player to this GameState..
     * @param player the player to add to the GameState
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Integer, ArrayList<Bullet>> getPlayerBullets() {
        return playerBullets;
    }

    public MatchSetup getMatch() {
        return match;
    }
}