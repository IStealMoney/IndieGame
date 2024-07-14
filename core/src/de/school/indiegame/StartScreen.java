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
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    Texture backgroundTexture;

    private Stage startStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;
    public static float bgWidth;
    public static float bgHeight;
    private ImageButton startGameBtn;
    Texture startGameTexture;
    private ImageButton quitBtn;
    Texture quitTexture;

    private Table startTable;
    Label startLabel;
    BitmapFont font;

    public StartScreen(final Main game) {
        batch = new SpriteBatch();
        this.game = game;
        font = Main.menuFont;

        // stage
        startStage = new Stage(new StretchViewport(1920, 1080));

        //background
        backgroundTexture = new Texture("menu/sky.png");
        Image backgroundSs = new Image(backgroundTexture);
        backgroundSs.setSize(1920, 1080);
        backgroundSs.setPosition(0, 0);
        bgWidth = (float) (backgroundTexture.getWidth()*(1920/backgroundTexture.getWidth())*2);
        bgHeight = (float) (backgroundTexture.getHeight()*(1080/backgroundTexture.getHeight())*2);

        //table
        startTable = new Table();
        startTable.setFillParent(true);

        // Welcome text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        startLabel = new Label("Welcome!", labelStyle);

        // start game btn
        startGameTexture = new Texture(Gdx.files.internal("menu/startGameBtn.png"));
        textureRegion = new TextureRegion(startGameTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        startGameBtn = new ImageButton(textureRegionDrawable);
        startGameBtn.setTransform(true);
        startGameBtn.getImageCell().expand().fill();
        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/quitBtn.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        quitBtn.setTransform(true);
        quitBtn.getImageCell().expand().fill();


        // Listener
        Gdx.input.setInputProcessor(startStage);
        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.paused = false;
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
        startTable.add(startGameBtn).size(200, 200).padBottom(100).row();
        startTable.add(quitBtn).size(200, 200);

        startStage.addActor(startTable);
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(startStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, bgWidth, bgHeight);
        batch.end();
        startStage.act(delta);
        startStage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.showPauseScreen();
        }
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
        if (startGameBtn != null) {
            startGameTexture.dispose();
        }
        if (quitBtn != null) {
            quitTexture.dispose();
        }
        batch.dispose();
        backgroundTexture.dispose();
    }
}

