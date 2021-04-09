package com.server.Models.GameModel;


/**
 * An abstract class representing an object with collision. Yes, I specifically avoided using Shape because it gives
 * off too many AP CS vibes.
 */
public abstract class ABody {
    abstract boolean collidesWithCircle(Circle other);
    abstract boolean collidesWithRect(Rect other);

    /**
     * Does this body collide with the given one?
     * @param other the body to check collision with
     * @return whether the two collide
     */
    public abstract boolean collidesWith(ABody other);

    /**
     * Gets a Rect that bounds this body, such that any body colliding with this one necessarily collides with this
     * bounding box.
     * @return the bounding box
     */
    public abstract Rect boundingBox();

    /**
     * Returns a shape of the same type moved with the given offset
     * @param vec the offset to move by
     * @return the moved shape
     */
    public abstract ABody moveBy(GameVector vec);

    public abstract GamePosn getCenter();
}
