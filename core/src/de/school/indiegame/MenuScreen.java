package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

// add btns to table and table to stage

public class MenuScreen implements Screen {
    private final Main game;
    private Stage menuStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;

    private ImageButton continueBtn;
    private Texture continueTexture;
    private ImageButton quitBtn;
    private Texture quitTexture;


    private Table menuTable;
    Label menuLabel;
    BitmapFont font;

    public MenuScreen(Main game) {
        this.game = game;
        font = new BitmapFont();

        // stage
        menuStage = new Stage(new StretchViewport(1920, 1080));
        //table
        menuTable = new Table();
        menuTable.setFillParent(true);
        //menuTable.center();
        //menuTable.setDebug(true);
        menuStage.addActor(menuTable);

        // menu text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.font.getData().setScale(10);
        menuLabel = new Label("Menu", labelStyle);

        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        menuTable.addActor(quitBtn);
        // continue btn
        continueTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(continueTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        continueBtn = new ImageButton(textureRegionDrawable);
        menuTable.addActor(continueBtn);

        // Listener
        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.paused = false;
                game.switchToGameScreen();
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // add elements to table
        menuTable.add(menuLabel).row();
        menuTable.add(continueBtn).row();
        menuTable.add(quitBtn).row();
    }

    @Override
    public void show() {
        menuStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    GameScreen.paused = false;
                    game.switchToGameScreen();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(menuStage);
    }

    @Override
    public void render(float delta) {
        menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        menuStage.getViewport().update(width, height, true);
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
        if (menuStage != null) {
            menuStage.dispose();
        }
    }
}
