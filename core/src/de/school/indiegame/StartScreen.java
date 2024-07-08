package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartScreen implements Screen {
    private final Main game;
    private Stage startStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;

    private ImageButton startGameBtn;
    private Texture startGameTexture;
    private ImageButton quitBtn;
    private Texture quitTexture;


    private Table startTable;
    Label startLabel;
    BitmapFont font;

    public StartScreen(final Main game) {
        this.game = game;
        font = new BitmapFont();

        // stage
        startStage = new Stage(new StretchViewport(1920, 1080));
        //table
        startTable = new Table();
        startTable.setFillParent(true);
        //startTable.center();
        //startTable.setDebug(true);
        startStage.addActor(startTable);

        // Welcome text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.font.getData().setScale(10);
        startLabel = new Label("Welcome!", labelStyle);

        // start game btn
        startGameTexture = new Texture(Gdx.files.internal("menu/startGameBtn.png"));
        textureRegion = new TextureRegion(startGameTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        startGameBtn = new ImageButton(textureRegionDrawable);
        startTable.addActor(startGameBtn);
        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/quitBtn.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        startTable.addActor(quitBtn);

        // Listener
        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.paused = false;
                // game screen needs to be in beginning state
                // also calls showGameScreen method
                // game.resetGame();
                game.showGameScreen();
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // add elements to table
        startTable.add(startLabel).row();
        startTable.add(startGameBtn).row();
        startTable.add(quitBtn).row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(startStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        startStage.act(delta);
        startStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        startStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (startStage != null) {
            startStage.dispose();
        }
    }
}

