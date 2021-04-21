package com.server.Models.GameModel;
import com.server.Models.GameModel.JSON.BulletJSON;

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

    public ABody getBody() {
        return body;
    }

    public boolean isInBounds(int width, int height) {
        GamePosn bPos = this.body.getCenter();
        if (bPos.x >= 0 && bPos.y >= 0 && bPos.x <= width && bPos.y <= height)
        {
            return true;
        }
        return false;
//        return this.body.collidesWith(new Rect(0, 0, width, height));
    }

    /**
     * Serializes the object to a JSON string in the expected format.
     */
    public BulletJSON toJson() {
        return new BulletJSON(this.body.getCenter().toJson(),
                this.velocity.toJson());
    }
}
