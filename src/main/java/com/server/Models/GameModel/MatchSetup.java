package com.server.Models.GameModel;

/**
 * Class encapsulating the information needed to start a match: the size of the playing field, the initial agents and
 * starter locations, etc.
 */
public class MatchSetup {
    /**
     * The width of the playing field in game units.
     */
    private final int width;

    /**
     * The height of the playing field in game units.
     */
    private final int height;

    public MatchSetup(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
