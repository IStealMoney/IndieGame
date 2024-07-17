package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;

import static de.school.indiegame.Main.*;
import static de.school.indiegame.MoneySystem.*;


// class contains all of our game logic
public class GameScreen implements Screen {

    private final Main game;
    public static Stage gameStage;
    public static boolean paused;
    GlyphLayout layout = new GlyphLayout();


    public GameScreen(Main game) {
        this.game = game;
        GameScreen.paused = false;
        gameStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.showPauseScreen();
        }
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
    }

    public void drawSprites(SpriteBatch batch) {
        // Draw all sprites here
        Map.draw(batch);
        if (Tool.isHidden) {
            Main.tool.draw(batch);
            Main.player.draw(batch);
        } else {
            Main.player.draw(batch);
            Main.tool.draw(batch);
        }

        Main.toolbar.draw(batch);
        Main.customer.draw(batch);
        Main.inventory.draw(batch);
        layout.setText(moneyFont, String.valueOf(currentMoney));
        moneyFont.draw(batch, currentMoney + " ", Main.SCREEN_SIZE[0]-20 * MULTIPLIER - layout.width * 1.25f, (Main.SCREEN_SIZE[1] - 19 * MULTIPLIER) + layout.height);
        Main.moneySystem.draw(batch);
    }

    public void updateSprites() {
        // update all sprites here
        Main.player.update();
        Main.tool.update();
        Main.toolbar.update();
        customer.update();
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
        if (skin != null) {
            skin.dispose();
        }
    }
}
