package main.java;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    void move() {
    }

    @Test
    void toJson() {
        Player player = new Player(new GamePosn(5, 5), 10, 1);
        player.velocity = new GameVector(0.1, -0.1);
        player.accel = new GameVector(10.0, 2.0);

        JsonObject actual = new JsonObject();
        actual.add("position", new GamePosn(5, 5).toJson());
        actual.add("velocity", new GameVector(0.1, -0.1).toJson());
        actual.add("acceleration", new GameVector(10.0, 2.0).toJson());
        actual.addProperty("orientation", 10.0);
        actual.addProperty("cooldown", 0);
        assertEquals(player.toJson(), actual);
    }
}