package com.server.Models.GameModel;

import java.util.*;

/**
 * A Bullets implementation that uses a grid to partition space and optimize collision checks.
 */
public class GridBullets implements Bullets {
    /**
     * The number of cells in a single row of the grid.
     */
    final int gridWidth;

    /**
     * The number of cells in a single column of the grid.
     */
    final int gridHeight;

    /**
     * The width of the playing field being tracked, in game units.
     */
    final double gameWidth;

    /**
     * The height of the playing field being tracked, in game units.
     */
    final double gameHeight;

    /**
     * An array containing the set of bullets in the corresponding grid cell. The grid is laid out left-to-right,
     * bottom-to-top: 0 is the lower left cell and 1 is the cell to the right of that one.
     */
    List<? extends Bullets> bullets;

    /**
     * The set of all bullets, used because the list can have duplicates.
     */
    Set<ABody> bulletSet;

    /**
     * Constructs a GridBullets from the given config values and an initial set of bullets.
     * @param gridWidth the grid width
     * @param gridHeight the grid height
     * @param gameWidth the game width
     * @param gameHeight the game height
     * @param bullets the collection of bullets to add
     */
    public GridBullets(int gridWidth, int gridHeight, double gameWidth, double gameHeight, Collection<ABody> bullets) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.bullets = GridBullets.initEmptyGrid(gridWidth, gridHeight);
        this.bulletSet = new HashSet<>();
        this.addAll(bullets);
    }

    /**
     * Initializes with the given grid values but no actual bullets
     * @param gridWidth the grid width
     * @param gridHeight the grid height
     * @param gameWidth the game width
     * @param gameHeight the game height
     */
    public GridBullets(int gridWidth, int gridHeight, double gameWidth, double gameHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.bullets = GridBullets.initEmptyGrid(gridWidth, gridHeight);
        this.bulletSet = new HashSet<>();
    }

    /**
     * Initializes an empty grid with the correct amount of empty cells.
     * @param gridWidth the grid width
     * @param gridHeight the grid height
     * @return the initialized empty grid
     */
    static ArrayList<NaiveBullets> initEmptyGrid(int gridWidth, int gridHeight) {
        ArrayList<NaiveBullets> grid = new ArrayList<>();
        for (int i = 0; i < gridWidth * gridHeight; i++) {
            grid.add(new NaiveBullets());
        }
        return grid;
    }

    /**
     * Returns the index in which to place the given position;
     * @param posn the position to place
     * @return the index of the cell to which it belongs
     */
    int findIndex(GamePosn posn) {
        int col = (int)Math.floor(posn.getX() / this.gameWidth * this.gridWidth);
        int row = (int)Math.floor(posn.getY() / this.gameHeight * this.gridHeight);
        // clip in bounds in case you're given anything weird
        col = Math.min(col, this.gridWidth - 1);
        col = Math.max(col, 0);
        row = Math.min(row - 1, this.gridHeight - 1);
        row = Math.max(row, 0);

        return row * this.gridWidth + col;
    }

    /**
     * Collision check. Uses the grid to only check a small fraction of the total bullets.
     * @param body the body to check collision with
     * @return a colliding bullet if one exists
     */
    @Override
    public Optional<ABody> computeCollision(ABody body) {
        HashSet<Integer> alreadyChecked = new HashSet<>();
        for (GamePosn corner : body.boundingBox().getCorners()) {
            int i = this.findIndex(corner);
            if (!alreadyChecked.contains(i)) {
                alreadyChecked.add(i);
                Optional<ABody> collider = this.bullets.get(i).computeCollision(body);
                if (collider.isPresent()) {
                    return collider;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return this.bulletSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.bulletSet.contains(o);
    }

    @Override
    public Object[] toArray() {
        return this.bulletSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        // TODO why does this warning happen?
        return this.bulletSet.toArray(ts);
    }

    /**
     * Adds the element into the collection in its proper place in the grid.
     * @param bullet the bullet to add
     * @return whether this collection changed as a result of the call
     */
    @Override
    public boolean add(ABody bullet) {
        boolean didAdd = this.bulletSet.add(bullet);
        if (!didAdd) {
            // nothing changed, no reason to modify list
            return false;
        } else {
            // add to grid
            // use bounding box to include in all grid squares where it might collide
            for (GamePosn corner : bullet.boundingBox().getCorners()) {
                this.bullets.get(this.findIndex(corner)).add(bullet);
            }
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Cannot remove from GridBullets");
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.bulletSet.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends ABody> collection) {
        boolean hasChanged = false;
        for (ABody bullet : collection) {
            hasChanged = hasChanged || this.add(bullet);
        }
        return hasChanged;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Cannot remove from GridBullets");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Cannot remove from GridBullets");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot remove from GridBullets");
    }

    @Override
    public Iterator<ABody> iterator() {
        return this.bulletSet.iterator();
    }

    @Override
    public int size() {
        return this.bulletSet.size();
    }
}
