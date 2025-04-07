package com.aibyn.sprint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class EditorScreen implements Screen {
    private final Main game;
    private final OrthographicCamera camera;
    private final Level level;
    private final ShapeRenderer shapeRenderer;

    private enum PlacementMode {
        START, FINISH, BLOCK, SPIKE
    }

    private PlacementMode currentMode = PlacementMode.BLOCK;

    private static final int GRID_SIZE = 32;

    private boolean isDragging = false;
    private float lastX, lastY;
    private float mouseDownTime = 0;
    private static final float DRAG_THRESHOLD = 0.05f;
    private boolean mouseWasDown = false;

    public EditorScreen(final Main game) {
        this(game, new Level());

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 5; j++) {
                level.addBlock(i * GRID_SIZE, j * GRID_SIZE);
            }
        }
    }

    public EditorScreen(final Main game, final Level existingLevel) {
        this.game = game;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        this.level = existingLevel;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleCameraMovement(delta);
        camera.update();
        drawGrid();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        level.render(shapeRenderer);
        shapeRenderer.end();
        handleInput();
    }


    private void handleCameraMovement(float delta) {
        boolean mouseDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (mouseDown && !mouseWasDown) {
            mouseDownTime = 0;
            lastX = Gdx.input.getX();
            lastY = Gdx.input.getY();
        } else if (mouseDown) {
            mouseDownTime += delta;

            if (mouseDownTime > DRAG_THRESHOLD) {
                float x = Gdx.input.getX();
                float y = Gdx.input.getY();

                float dx = (x - lastX) * 0.5f;
                float dy = (y - lastY) * 0.5f;

                camera.position.x -= dx;
                camera.position.y += dy;

                lastX = x;
                lastY = y;
                isDragging = true;
            }
        } else {
            isDragging = false;
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            if (mouseWasDown) {
                float dx = (x - lastX) * 0.5f;
                float dy = (y - lastY) * 0.5f;
                camera.position.x -= dx;
                camera.position.y += dy;
            }

            lastX = x;
            lastY = y;
            mouseWasDown = true;
            isDragging = true;
        } else if (!mouseDown) {
            mouseWasDown = false;
        }
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;
        float endX = camera.position.x + camera.viewportWidth / 2;
        float endY = camera.position.y + camera.viewportHeight / 2;

        int startGridX = (int) (startX / GRID_SIZE) - 1;
        int startGridY = (int) (startY / GRID_SIZE) - 1;
        int endGridX = (int) (endX / GRID_SIZE) + 1;
        int endGridY = (int) (endY / GRID_SIZE) + 1;

        for (int x = startGridX; x <= endGridX; x++) {
            shapeRenderer.line(x * GRID_SIZE, startGridY * GRID_SIZE, x * GRID_SIZE, endGridY * GRID_SIZE);
        }

        for (int y = startGridY; y <= endGridY; y++) {
            shapeRenderer.line(startGridX * GRID_SIZE, y * GRID_SIZE, endGridX * GRID_SIZE, y * GRID_SIZE);
        }

        shapeRenderer.end();
    }

    private int[] screenToGrid(float screenX, float screenY) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);
        
        int gridX = (int) Math.floor(touchPos.x / GRID_SIZE);
        int gridY = (int) Math.floor(touchPos.y / GRID_SIZE);
        
        return new int[] {gridX, gridY};
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            currentMode = PlacementMode.START;
            System.out.println("Mode: START");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            currentMode = PlacementMode.FINISH;
            System.out.println("Mode: FINISH");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentMode = PlacementMode.BLOCK;
            System.out.println("Mode: BLOCK");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            currentMode = PlacementMode.SPIKE;
            System.out.println("Mode: SPIKE");
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            int[] gridPos = screenToGrid(Gdx.input.getX(), Gdx.input.getY());

            int gridX = gridPos[0];
            int gridY = gridPos[1];

            level.removeObjectAt(gridX * GRID_SIZE, gridY * GRID_SIZE);
        }

        if (Gdx.input.justTouched() && !isDragging && mouseDownTime < DRAG_THRESHOLD) {
            int[] gridPos = screenToGrid(Gdx.input.getX(), Gdx.input.getY());
            int gridX = gridPos[0];
            int gridY = gridPos[1];
            
            System.out.println("Grid coordinates: " + gridX + "," + gridY);

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                System.out.println("Attempting to place " + currentMode + " at " + gridX + "," + gridY);

                level.removeObjectAt(gridX * GRID_SIZE, gridY * GRID_SIZE);

                switch (currentMode) {
                    case START:
                        level.setStartPosition(gridX * GRID_SIZE, gridY * GRID_SIZE);
                        System.out.println("Placed START at " + gridX + "," + gridY);
                        break;
                    case FINISH:
                        level.setFinishPosition(gridX * GRID_SIZE, gridY * GRID_SIZE);
                        System.out.println("Placed FINISH at " + gridX + "," + gridY);
                        break;
                    case BLOCK:
                        level.addBlock(gridX * GRID_SIZE, gridY * GRID_SIZE);
                        System.out.println("Placed BLOCK at " + gridX + "," + gridY);
                        break;
                    case SPIKE:
                        level.addSpike(gridX * GRID_SIZE, gridY * GRID_SIZE);
                        System.out.println("Placed SPIKE at " + gridX + "," + gridY);
                        break;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (level.isValid()) {
                System.out.println("Starting game.");
                game.setScreen(new GameScreen(game, level));
            } else {
                System.out.println("Invalid level. Need both START and FINISH.");
            }
        }

        mouseWasDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
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
    }
}