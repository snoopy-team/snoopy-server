import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.java.ABody;
import main.java.GameVector;

/**
 * A bullet.
 */
public class Bullet {
    /**
     * The bullet hurtbox.
     */
    ABody body;

    /**
     * The velocity.
     */
    GameVector velocity;

    public Bullet(ABody body, GameVector velocity) {
        this.body = body;
        this.velocity = velocity;
    }

    public void move(double dt) {
        this.body = this.body.moveBy(this.velocity.scale(dt));
    }

    /**
     * Serializes the object to a JSON string in the expected format.
     */
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("position", this.body.getCenter().toJson());
        json.add("velocity", this.velocity.toJson());
        return json;
    }
}
