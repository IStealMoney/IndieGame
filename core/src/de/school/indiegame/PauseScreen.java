package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PauseScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    Texture backgroundTexture;
    private Stage pauseStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;

    private ImageButton quitBtn;
    private Texture quitTexture;
    private ImageButton continueBtn;
    private Texture continueTexture;
    private ImageButton homeBtn;
    private Texture homeTexture;
    private ImageButton saveBtn;
    private Texture saveTexture;


    private Table pauseTable;
    private Label pauseLabel;
    BitmapFont font;

    public PauseScreen(final Main game) {
        this.game = game;
        font = Main.menuFont;
        batch = new SpriteBatch();

        // stage
        pauseStage = new Stage(new StretchViewport(1920, 1080));
        Gdx.input.setInputProcessor(pauseStage);

        //background
        backgroundTexture = new Texture("menu/sky.png");
        Image backgroundPS = new Image(backgroundTexture);
        backgroundPS.setSize(1920, 1080);
        backgroundPS.setPosition(0, 0);

        // table
        pauseTable = new Table();
        pauseTable.setFillParent(true);

        // menu text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        pauseLabel = new Label("Pause", labelStyle);

        // continue btn
        continueTexture = new Texture(Gdx.files.internal("menu/continueBtn.png"));
        textureRegion = new TextureRegion(continueTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        continueBtn = new ImageButton(textureRegionDrawable);
        continueBtn.setTransform(true);
        continueBtn.getImageCell().expand().fill();
        // home btn
        homeTexture = new Texture(Gdx.files.internal("menu/homeBtn.png"));
        textureRegion = new TextureRegion(homeTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        homeBtn = new ImageButton(textureRegionDrawable);
        homeBtn.setTransform(true);
        homeBtn.getImageCell().expand().fill();
        // save btn
        saveTexture = new Texture(Gdx.files.internal("menu/saveBtn.png"));
        textureRegion = new TextureRegion(saveTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        saveBtn = new ImageButton(textureRegionDrawable);
        saveBtn.setTransform(true);
        saveBtn.getImageCell().expand().fill();
        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/quitBtn.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        homeBtn.setTransform(true);
        quitBtn.getImageCell().expand().fill();

        // Listener
        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.paused = false;
                game.showGameScreen();
            }
        });
        homeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.resetGame();
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        saveBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.saveGame();
            }
        });


        // add elements to table
        pauseTable.add(pauseLabel).row();
        pauseTable.add(continueBtn).size(200, 200).padRight(100);
        pauseTable.add(saveBtn).size(200, 200).padRight(100);
        pauseTable.add(homeBtn).size(200,200).padRight(100);
        pauseTable.add(quitBtn).size(200,200);
        pauseStage.addActor(pauseTable);
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, StartScreen.bgWidth, StartScreen.bgHeight);
        batch.end();
        pauseStage.act(delta);
        pauseStage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.showGameScreen();
        }
    }

    @Override
    public void resize(int width, int height) {
        pauseStage.getViewport().update(width, height, true);
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
        if (pauseStage != null) {
            pauseStage.dispose();
        }
        if (continueBtn != null) {
            continueTexture.dispose();
        }
        if (homeBtn != null) {
            homeTexture.dispose();
        }
        if (quitBtn != null) {
            quitTexture.dispose();
        }
        batch.dispose();
        backgroundTexture.dispose();
    }
}
