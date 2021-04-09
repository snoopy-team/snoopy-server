package com.server.Models.GameModel;

import java.util.Collection;
import java.util.Optional;

/**
 * An interface representing a set of bullets with a mechanism to determine collision with a given object.
 */
public interface Bullets extends Collection<ABody> {
    /**
     * Finds a body colliding with the given one if any exist.
     * @param body the body to check collision with
     * @return nothing, if no collision, otherwise a colliding body
     */
    Optional<ABody> computeCollision(ABody body);

    /**
     * Is there a collision in the given set of bullets?
     * @param body the body to check collision with
     * @return whether there is a collision or not
     */
    default boolean hasCollision(ABody body) {
        return computeCollision(body).isPresent();
    }
}
