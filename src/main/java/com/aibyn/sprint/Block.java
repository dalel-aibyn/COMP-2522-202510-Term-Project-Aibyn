package com.aibyn.sprint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    private static final float SIZE = 32.0f;
    private final float x_position;
    private final float y_position;
    private final Rectangle bounds;

    public Block(final float x_position, final float y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.bounds = new Rectangle(x_position, y_position, SIZE, SIZE);
    }

    public void render(final ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x_position, y_position, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}