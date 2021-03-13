package main.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The agents in the game: rockets with physics associated with them.
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
     * Creates a player at the given position and orientation with zero velocity and acceleration, thrusters off, and
     * no cooldown on firing.
     */
    public Player(GamePosn posn, double orientation) {
        this.posn = posn;
        this.orientation = orientation;
        this.velocity = GameVector.origin();
        this.accel = GameVector.origin();
        this.thrustersOn = false;
        this.angularVelocity = 0;
        this.cooldown = 0;
    }

    /**
     * Updates the location and state of the player for the given length of time. No intermediate calculation is
     * done, so the physics will become less and less accurate as dt increases.
     *
     * @param dt the delta to compute time for, in game seconds
     * @param model the physics model to use for the calculations
     */
    public void move(double dt, PhysicsModel model) {
        this.posn = posn.addVector(this.velocity.scale(dt));
        this.orientation = (this.orientation + dt * this.angularVelocity) % (Math.PI * 2);

        GameVector drag = model.computeDrag(this.velocity);

        GameVector realAccel = this.accel.add(model.getGravity());
        this.velocity = this.velocity.add(realAccel.scale(dt));

        GameVector thrust;
        if (this.thrustersOn) {
            thrust = model.computeThrust(this.orientation);
        } else {
            thrust = GameVector.origin();
        }

        this.accel = this.accel.add(drag.scale(dt)).add(thrust.scale(dt));

        // thrusters and angular velocity remain unchanged
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
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("position", this.posn.toJson());
        json.add("velocity", this.velocity.toJson());
        json.add("acceleration", this.accel.toJson());
        json.addProperty("orientation", this.orientation);
        json.addProperty("cooldown", this.cooldown);
        return json;
    }
}
