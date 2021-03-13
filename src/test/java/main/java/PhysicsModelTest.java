package main.java;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhysicsModelTest {

    @Test
    void fromJson() {
        PhysicsModel gc = PhysicsModel.fromJson(new StringReader(
                "{\"thrustPower\":0.2,\"gravityStrength\":9.8,\"dragFactor\":1}"));
        assertEquals(gc.gravityStrength, 9.8);
        assertEquals(gc.thrustPower, 0.2);
        assertEquals(gc.dragFactor, 1);
    }
}