package com.server.Models.GameModel.JSON;

import com.server.Configuration.Constants;

/**
 * Round doubles so they serialize without wasting space on meaningless decimal places.
 */
public class Rounder {
    /**
     * Round with the standard number of bits on the end.
     * @param x the double to round
     * @return the rounded number
     */
    public static double round(double x) {
        return round(x, Constants.NUM_DECIMAL_PLACES);
    }

    /**
     * Round with custom number of bits on the end.
     * @param x the double to round
     * @param numDigits the number of binary digits to keep at the end (e.g., 3 for eighths)
     * @return the rounded number
     */
    public static double round(double x, int numDigits) {
        double newFactor = Math.pow(2, numDigits);
        return Math.round(x * newFactor) / newFactor;
    }
}
