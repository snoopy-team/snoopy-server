package com.server.Models.GameModel;

/**
 * Represents a single action: firing, banking left or right, or thrusting.
 */
public enum Action {
    BankLeft,
    BankRight,
    Thrust,
    Fire;

    /**
     * More space-efficient version of the toString method.
     *  <ul>
     *      <li>L is BankLeft</li>
     *      <li>R is BankRight</li>
     *      <li>T is Thrust</li>
     *      <li>F is Fire</li>
     *  </ul>
     * @return the action as a character
     */
    public char toChar() {
        switch(this) {
            case BankLeft: return 'L';
            case BankRight: return 'R';
            case Thrust: return 'T';
            case Fire: return 'F';
            default: throw new IllegalArgumentException();
        }
    }

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
