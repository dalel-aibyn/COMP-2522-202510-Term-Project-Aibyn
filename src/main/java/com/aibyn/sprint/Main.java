package com.aibyn.sprint;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private SpriteBatch batch;
    public static final String TITLE = "Sprint";

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration(); // xd
        config.setTitle(TITLE);
        config.setWindowedMode(800, 600);
        config.useVsync(true);

        new Lwjgl3Application(new Main(), config);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new EditorScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}