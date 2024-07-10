package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static de.school.indiegame.Main.batch;
import static de.school.indiegame.Main.shape;


// class contains all of our game logic

public class GameScreen implements Screen {

    private final Main game;
    private Stage gameStage;
    public static boolean paused;

    public GameScreen(Main game) {
        this.game = game;
        GameScreen.paused = false;
        gameStage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        //Gdx.input.setInputProcessor(gameStage);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                // from GameScreen to PauseScreen
                if (keyCode == Input.Keys.ESCAPE) {
                    paused = true;
                    game.showPauseScreen();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act(delta);
        gameStage.draw();

        if (!paused) {
            ScreenUtils.clear(1, 1, 1, 1);
            shape.begin();
            batch.begin();

            updateSprites();
            drawSprites(batch);

            batch.end();
            shape.end();
        }

    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
    }

    public void drawSprites(SpriteBatch batch) {
        // Draw all sprites here
        Map.draw(batch);
        Main.player.draw(batch);
        Main.tool.draw(batch);
        Main.inventory.draw(batch);
    }

    public void updateSprites() {
        // update all sprites here
        Main.player.update();
        Main.tool.update();
        Main.inventory.update();
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (gameStage != null) {
            gameStage.dispose();
        }
    }
}
