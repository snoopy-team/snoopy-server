package main.java;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GridBulletsTest {

    @Test
    void computeCollisionEdge() {
        Bullets b1 = new GridBullets(5, 4, 100, 80);
        ABody player = new Rect(5, 5, 10, 10);
        assertFalse(b1.hasCollision(player));
        b1.add(new Circle(6, 4, 1));
        assertTrue(b1.hasCollision(player));
    }

    @Test
    void testCorner() {
        Bullets b1 = new GridBullets(5, 4, 100, 80);
        ABody player = new Rect(30, 30, 80, 80);
        assertFalse(b1.hasCollision(player));
        b1.add(new Circle(6, 4, 1));
        assertFalse(b1.hasCollision(player));
        b1.add(new Circle(100, 80, 1));
        assertTrue(b1.hasCollision(player));
    }

    @Test
    void testNoInterpolation() {
        // as of now, we do not do any interpolation in between bounding boxes!
        Bullets b1 = new GridBullets(5, 4, 100, 80);
        ABody player = new Rect(30, 30, 80, 80);
        assertFalse(b1.hasCollision(player));
        b1.add(new Circle(6, 4, 1));
        assertFalse(b1.hasCollision(player));
        b1.add(new Circle(50, 50, 4));
        assertFalse(b1.hasCollision(player));
    }

    @Test
    void perfTest() {
        ABody player = new Rect(5, 5, 10, 10);
        // this larger version takes ~13s on my machine
        int N_TRIALS = 100_000;
        int N_BULLETS = 300;
        // int N_TRIALS = 1;
        // int N_BULLETS = 1;
        Random rand = new Random(123);
        for (int t = 0; t < N_TRIALS; t++) {
            Bullets b1 = new GridBullets(20, 16, 100, 80);
            for (int i = 0; i < N_BULLETS; i++) {
                int cx = rand.nextInt(101);
                int cy = rand.nextInt(80);
                double r = rand.nextDouble() * 2;
                b1.add(new Circle(cx, cy, r));
                b1.computeCollision(player);
            }
        }
    }

    @Test
    void perfTestNaive() {
        ABody player = new Rect(5, 5, 10, 10);
        // this larger version takes ~28s on my machine
        //int N_TRIALS = 100_000;
        //int N_BULLETS = 300;
        int N_TRIALS = 1;
        int N_BULLETS = 1;
        Random rand = new Random(123);
        for (int t = 0; t < N_TRIALS; t++) {
            Bullets b1 = new NaiveBullets();
            for (int i = 0; i < N_BULLETS; i++) {
                int cx = rand.nextInt(101);
                int cy = rand.nextInt(80);
                double r = rand.nextDouble() * 2;
                b1.add(new Circle(cx, cy, r));
                b1.computeCollision(player);
            }
        }
    }
}