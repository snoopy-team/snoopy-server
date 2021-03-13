package main.java;

import java.util.*;

/**
 * A simple implementation of Bullets that simply checks each bullet in turn.
 */
public class NaiveBullets extends AbstractList<ABody> implements Bullets {
    /**
     * The list of bullets.
     */
    ArrayList<ABody> bullets;

    public NaiveBullets() {
        this.bullets = new ArrayList<>();
    }

    public NaiveBullets(Collection<ABody> bullets) {
        this.bullets = new ArrayList<>(bullets);
    }

    @Override
    public int size() {
        return this.bullets.size();
    }

    @Override
    public boolean add(ABody aBody) {
        return this.bullets.add(aBody);
    }

    @Override
    public ABody get(int i) {
        return this.bullets.get(i);
    }

    @Override
    public Optional<ABody> computeCollision(ABody body) {
        for (ABody bullet : this) {
            if (bullet.collidesWith(body)) {
                return Optional.of(bullet);
            }
        }
        return Optional.empty();
    }
}
