package com.aibyn.sprint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private static final float SIZE = 32.0f;
    private static final float RADIUS = SIZE / 2;
    private static final float MAX_VELOCITY_X = 300.0f;
    private static final float ACCELERATION_X = 800.0f;
    private static final float JUMP_VELOCITY = 500.0f;
    private static final float GRAVITY = -1200.0f;

    private final Vector2 position;
    private final Vector2 velocity;
    private final Circle bounds;
    private boolean isGrounded;

    public Player(final float x, final float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Circle(x + RADIUS, y + RADIUS, RADIUS);
        isGrounded = false;
    }

    public void update(final float delta, final Level level) {
        handleInput(delta);

        velocity.y += GRAVITY * delta;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        bounds.setPosition(position.x + RADIUS, position.y + RADIUS);

        checkCollisions(level);

        checkSpikes(level);
    }

    private void handleInput(final float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x -= ACCELERATION_X * delta;
            if (velocity.x < -MAX_VELOCITY_X) {
                velocity.x = -MAX_VELOCITY_X;
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x += ACCELERATION_X * delta;
            if (velocity.x > MAX_VELOCITY_X) {
                velocity.x = MAX_VELOCITY_X;
            }
        }
        else {
            if (velocity.x > 0) {
                velocity.x = Math.max(0, velocity.x - ACCELERATION_X * delta);
            } else if (velocity.x < 0) {
                velocity.x = Math.min(0, velocity.x + ACCELERATION_X * delta);
            }
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(Input.Keys.W)) && isGrounded) {
            velocity.y = JUMP_VELOCITY;
            isGrounded = false;
        }
    }

    private void checkCollisions(final Level level) {
        isGrounded = false;

        for (Block block : level.getBlocks()) {
            if (CollisionUtils.circleRectangleCollision(bounds, block.getBounds())) {
                resolveCollision(block.getBounds());
            }
        }
    }

    private void resolveCollision(final Rectangle blockBounds) {
        float[] closest = CollisionUtils.findClosestPointOnRectangle(bounds, blockBounds);
        float closestX = closest[0];
        float closestY = closest[1];

        float dx = bounds.x - closestX;
        float dy = bounds.y - closestY;
        float distance = CollisionUtils.distance(closestX, closestY, bounds.x, bounds.y);

        if (distance < 0.0001f) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
                dx = velocity.x > 0 ? 1 : -1;
                dy = 0;
            } else {
                dx = 0;
                dy = velocity.y > 0 ? 1 : -1;
            }
            distance = 1;
        }

        float nx = dx / distance;
        float ny = dy / distance;

        float overlap = bounds.radius - distance;

        position.x += nx * overlap;
        position.y += ny * overlap;

        bounds.setPosition(position.x + RADIUS, position.y + RADIUS);

        float dotProduct = velocity.x * nx + velocity.y * ny;

        if (dotProduct < 0) {
            if (Math.abs(nx) > 0.7f) {
                velocity.x = 0;
            }
            else if (ny > 0.7f) {
                isGrounded = true;
                velocity.y = 0;
            } else if (ny < -0.7f) {
                velocity.y = 0;
            } else {
                velocity.x -= 1.2f * dotProduct * nx;
                velocity.y -= 1.2f * dotProduct * ny;
            }
        }
    }

    private void checkSpikes(final Level level) {
        for (Spike spike : level.getSpikes()) {
            float distance = CollisionUtils.distance(bounds.x, bounds.y, spike.getHitbox().x, spike.getHitbox().y);

            if (distance < bounds.radius + spike.getHitbox().radius) {
                break;
            }
        }
    }

    public void render(final ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.circle(bounds.x, bounds.y, bounds.radius);
    }

    public Circle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}