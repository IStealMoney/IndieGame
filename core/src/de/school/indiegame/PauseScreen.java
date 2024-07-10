package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static de.school.indiegame.StartScreen.btnSize;

public class PauseScreen implements Screen {
    private final Main game;
    private Stage pauseStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;

    private ImageButton quitBtn;
    private Texture quitTexture;
    private ImageButton continueBtn;
    private Texture continueTexture;
    private ImageButton homeBtn;
    private Texture homeTexture;


    private Table pauseTable;
    private Label pauseLabel;
    BitmapFont font;

    public PauseScreen(final Main game) {
        this.game = game;
        font = new BitmapFont();

        // stage
        pauseStage = new Stage(new StretchViewport(1920, 1080));
        float xPosition = 1920/3;
        float yPosition = 1080/3 * 2;

        // table
        pauseTable = new Table();
        pauseTable.setFillParent(true);

        // menu text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.font.getData().setScale(10);
        pauseLabel = new Label("Pause", labelStyle);

        // continue btn
        continueTexture = new Texture(Gdx.files.internal("menu/continueBtn.png"));
        textureRegion = new TextureRegion(continueTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        continueBtn = new ImageButton(textureRegionDrawable);
        continueBtn.setSize(300, 300);
        continueBtn.setTransform(true);
        continueBtn.getImageCell().expand().fill();
        // home btn
        homeTexture = new Texture(Gdx.files.internal("menu/homeBtn.png"));
        textureRegion = new TextureRegion(homeTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        homeBtn = new ImageButton(textureRegionDrawable);
        homeBtn.setSize(300, 300);
        homeBtn.setTransform(true);
        homeBtn.getImageCell().expand().fill();
        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/quitBtn.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        quitBtn.setSize(300, 300);
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

        // add elements to table
        pauseTable.add(pauseLabel).row();
        pauseTable.add(continueBtn).size(StartScreen.btnSize, StartScreen.btnSize).row();
        pauseTable.add(homeBtn).size(StartScreen.btnSize, StartScreen.btnSize).row();
        pauseTable.add(quitBtn).size(StartScreen.btnSize, StartScreen.btnSize);


        pauseStage.addActor(pauseTable);
    }



    @Override
    public void show() {
        pauseStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    GameScreen.paused = false;
                    game.showGameScreen();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(pauseStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pauseStage.act(delta);
        pauseStage.draw();
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
    }
}
