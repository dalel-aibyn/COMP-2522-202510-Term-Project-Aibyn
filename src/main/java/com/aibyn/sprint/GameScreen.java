package com.aibyn.sprint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen implements Screen {
    private final Main game;
    private final OrthographicCamera camera;
    private final Level level;
    private final ShapeRenderer shapeRenderer;
    private final Player player;
    private boolean gameOver;
    private boolean victory;
    private float gameOverTimer = 0;
    private float victoryTimer = 0;
    private static final float GAME_OVER_DELAY = 0.5f; // half second
    private static final float VICTORY_DELAY = 2.0f; // 2 seconds
    private float gameTimer;
    private final BitmapFont font;

    public GameScreen(final Main game, final Level level) {
        this.game = game;
        this.level = level;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        font.setColor(Color.BLACK);

        if (level.getStartPosition() != null) {
            player = new Player(level.getStartPosition().x, level.getStartPosition().y);
        } else {
            player = new Player(100, 100);
        }

        gameOver = false;
        victory = false;
        gameTimer = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!gameOver && !victory) {
            player.update(delta, level);
            gameTimer += delta;

            if (player.isDead()) {
                gameOver = true;
                gameOverTimer = 0;
            }

            if (level.getFinishBounds() != null &&
                    CollisionUtils.circleRectangleCollision(player.getBounds(), level.getFinishBounds())) {
                victory = true;
                victoryTimer = 0;
                System.out.println("Victory! Time: " + String.format("%.2f", gameTimer) + " seconds");
            }
        }

        if (gameOver) {
            gameOverTimer += delta;
            if (gameOverTimer >= GAME_OVER_DELAY) {
                game.setScreen(new GameScreen(game, level));
            }
        }

        if (victory) {
            victoryTimer += delta;
            if (victoryTimer >= VICTORY_DELAY) {
                // Return to editor after delay
                game.setScreen(new EditorScreen(game, level));
            }
        }

        camera.position.x = player.getPosition().x + player.getBounds().radius;
        camera.position.y = player.getPosition().y + player.getBounds().radius;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        level.render(shapeRenderer);

        player.render(shapeRenderer);
        shapeRenderer.end();

        game.getBatch().begin();

        font.draw(game.getBatch(), "Time: " + String.format("%.2f", gameTimer), 10, Gdx.graphics.getHeight() - 10);

        String posInfo = String.format("X: %.1f Y: %.1f", player.getPosition().x, player.getPosition().y);
        String speedInfo = String.format("Speed X: %.1f Y: %.1f", player.getVelocity().x, player.getVelocity().y);
        font.draw(game.getBatch(), posInfo, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 10);
        font.draw(game.getBatch(), speedInfo, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 30);

        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new EditorScreen(game, level));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game, level));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}