package com.aibyn.sprint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private Vector2 startPosition;
    private Vector2 finishPosition;
    private final Rectangle finishBounds;
    private final List<Block> blocks;
    private final List<Spike> spikes;
    private static final float BLOCK_SIZE = 32.0f;

    public Level() {
        blocks = new ArrayList<>();
        spikes = new ArrayList<>();
        startPosition = null;
        finishPosition = null;
        finishBounds = new Rectangle();
    }

    public void setStartPosition(float x, float y) {
        startPosition = new Vector2(x, y);
    }

    public void setFinishPosition(float x, float y) {
        finishPosition = new Vector2(x, y);
        finishBounds.set(x, y, BLOCK_SIZE, BLOCK_SIZE);
    }

    public void addBlock(float x, float y) {
        for (Block block : blocks) {
            if (block.getBounds().x == x && block.getBounds().y == y) {
                return;
            }
        }
        blocks.add(new Block(x, y));
    }

    public void addSpike(float x, float y) {
        for (Spike spike : spikes) {
            if (spike.getBounds().x == x && spike.getBounds().y == y) {
                return;
            }
        }
        spikes.add(new Spike(x, y));
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (Block block : blocks) {
            block.render(shapeRenderer);
        }

        for (Spike spike : spikes) {
            spike.render(shapeRenderer);
        }

        if (startPosition != null) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.circle(startPosition.x + BLOCK_SIZE / 2, startPosition.y + BLOCK_SIZE / 2, BLOCK_SIZE / 2);
        }

        if (finishPosition != null) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(finishPosition.x, finishPosition.y, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Rectangle getFinishBounds() {
        return finishBounds;
    }

    public boolean isValid() {
        return startPosition != null && finishPosition != null;
    }

    public void removeObjectAt(float x, float y) {
        blocks.removeIf(block -> Math.abs(block.getBounds().x - x) < 0.001f &&
                Math.abs(block.getBounds().y - y) < 0.001f);

        spikes.removeIf(spike -> Math.abs(spike.getBounds().x - x) < 0.001f &&
                Math.abs(spike.getBounds().y - y) < 0.001f);

        if (startPosition != null &&
                Math.abs(startPosition.x - x) < 0.001f &&
                Math.abs(startPosition.y - y) < 0.001f) {
            startPosition = null;
        }

        if (finishPosition != null &&
                Math.abs(finishPosition.x - x) < 0.001f &&
                Math.abs(finishPosition.y - y) < 0.001f) {
            finishPosition = null;
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Spike> getSpikes() {
        return spikes;
    }
}