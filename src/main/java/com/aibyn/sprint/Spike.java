package com.aibyn.sprint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Spike {
    private static final float SIZE = 32.0f;
    private final float x_position;
    private final float y_position;
    private final Rectangle bounds;
    private final Circle hitbox;

    public Spike(final float x_position, final float y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.bounds = new Rectangle(x_position, y_position, SIZE, SIZE);

        float radius = SIZE / 4;
        float centerX = x_position + SIZE / 2;
        float centerY = y_position + SIZE / 2;
        this.hitbox = new Circle(centerX, centerY, radius);
    }

    public void render(final ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.triangle(
                x_position, y_position,
                x_position + SIZE, y_position,
                x_position + SIZE / 2, y_position + SIZE
        );

        Color circleColor = new Color(1, 1, 0, 0.3f);
        shapeRenderer.setColor(circleColor);
        shapeRenderer.circle(hitbox.x, hitbox.y, hitbox.radius);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Circle getHitbox() {
        return hitbox;
    }
}