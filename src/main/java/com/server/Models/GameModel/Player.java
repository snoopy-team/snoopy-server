package com.server.Models.GameModel;
import com.server.Models.GameModel.JSON.PlayerJSON;

/**
 * The game state idea of a controlled rocket that fires and thrusts. Can be controlled by a human or computer.
 */
public class Player {
    /**
     * The position of the player.
     */
    GamePosn posn;

    /**
     * The velocity of the player.
     */
    GameVector velocity;

    /**
     * The acceleration of the player not due to gravity.
     */
    GameVector accel;

    /**
     * The orientation of the player, in radians. This is the direction the thrust moves the player, so 0 means the
     * player is facing right.
     */
    double orientation;

    /**
     * Whether the thrusters are active or not.
     */
    boolean thrustersOn;

    /**
     * The angular velocity in radians per game second.
     */
    double angularVelocity;

    /**
     * The game seconds until the player can fire again, or 0 if the player can fire at will.
     */
    double cooldown;

    /**
     * The ID number of the player, which should be unique within a single game.
     */
    int id;

    /**
     * Creates a player at the given position and orientation with zero velocity and acceleration, thrusters off, and
     * no cooldown on firing.
     */
    public Player(GamePosn posn, double orientation, int id) {
        this.posn = posn;
        this.orientation = orientation;
        this.id = id;
        this.velocity = GameVector.origin();
        this.accel = GameVector.origin();
        this.thrustersOn = false;
        this.angularVelocity = 0;
        this.cooldown = 0;
    }

    public ABody getBody(GameConfig config) {
        return new Rect(this.posn, config.getPlayerRadius(), config.getPlayerRadius());
    }

    /**
     * Updates the location and state of the player for the given length of time. No intermediate calculation is
     * done, so the physics will become less and less accurate as dt increases.
     *
     * @param dt the delta to compute time for, in game seconds
     * @param model the physics model to use for the calculations
     */
    public void move(double dt, PhysicsModel model) {
        double SCALE = 50;
        GameVector thrust;
        if (this.thrustersOn) {
            thrust = model.computeThrust(this.orientation);
        } else {
            thrust = GameVector.origin();
        }

        this.posn = posn.addVector(this.velocity.scale(dt));
        this.orientation = (this.orientation + dt * this.angularVelocity) % (Math.PI * 2);

        GameVector drag = model.computeDrag(this.velocity.scale(1 / SCALE));

        GameVector realAccel = this.accel.add(model.getGravity()).add(drag).add(thrust);
        this.velocity = this.velocity.add(realAccel.scale(SCALE).scale(dt));


        // thrusters and angular velocity remain unchanged

        // update cooldown
        this.cooldown += dt;
    }

    public void fireThrusters() {
        this.thrustersOn = true;
    }

    public void stopThrusters() {
        this.thrustersOn = false;
    }

    /**
     * Serializes the object to a JSON string in the expected format.
     */
    public PlayerJSON toJson() {
        return new PlayerJSON(this.posn.toJson(), this.velocity.toJson(), this.accel.toJson(),
                this.orientation, this.cooldown);
    }

    /**
     * With the given list of actions, advances this player's state by the given amount of time. The input GameState
     * is used to fire bullets, but dying is handled outside of this class.
     */
    public void takeActions(Iterable<Action> actions, GameState state, double dt) {
        this.reset();
        for (Action a : actions) {
            this.takeAction(a, state, dt);
        }

        // now move forward with the updated state
        this.move(dt, state.getPhysics());
    }

    public void takeAction(Action action, GameState state, double dt) {
        /*
        So the big question is: why not do this through double dispatch? I considered making Action a full-blown
        interface with four implementing classes, which has the virtue of being able to add more actions quickly. I
        think that this is ultimately better, however, because the exact mechanics of player movement vary more than
        the set of actions the player can take. This ensures that the Player class retains full control over the
        physics and game control.
         */

        switch (action) {
            case BankLeft:
                this.bankLeft(state);
                break;
            case BankRight:
                this.bankRight(state);
                break;
            case Thrust:
                this.fireThrusters();
                break;
            case Fire:
                this.fire(state);
                break;
        }
    }

    public void fire(GameState state) {
        double maxCooldown = state.getConfig().getBulletCooldown();
        int numBullets = state.getConfig().getNumBullets();
        double bulletSpread = state.getConfig().getBulletSpread();

        if (this.cooldown > maxCooldown) {
            this.cooldown = 0;


            for (int i = 0; i < numBullets; i++) {
                // convert, e.g., 0 through 4 to -2 through 2
                // >> is dividing by 2 and flooring in a way IntelliJ won't be mad at me for doing
                double currAngle = (numBullets >> 1) * bulletSpread - i * bulletSpread;
                state.fire(this.id, this.posn, this.orientation + currAngle);
            }
        }
    }

    public void bankLeft(GameState state) {
        this.angularVelocity = state.getConfig().getTurnSpeed();
    }

    public void bankRight(GameState state) {
        this.angularVelocity = -state.getConfig().getTurnSpeed();
    }

    /**
     * Resets the angular velocity and thrusters to a motionless state.
     */
    public void reset() {
        this.angularVelocity = 0;
        this.stopThrusters();
    }

    /**
     * Returns the player id.
     * @return player id.
     */
    public int getId() {
        return this.id;
    }
}
