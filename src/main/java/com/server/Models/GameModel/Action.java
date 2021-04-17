package com.server.Models.GameModel;

/**
 * Represents a single action: firing, banking left or right, or thrusting.
 */
public enum Action {
    BankLeft,
    BankRight,
    Thrust,
    Fire;

    @Override
    public String toString() {
        switch(this) {
            case BankLeft: return "Bank Left";
            case BankRight: return "Bank Right";
            case Thrust: return "Thrust";
            case Fire: return "Fire";
            default: throw new IllegalArgumentException();
        }
    }
}
