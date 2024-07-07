package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// add btns to table and table to stage

public class MenuScreen implements Screen {

    private Stage menuStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;

    private ImageButton continueBtn;
    private Image continueImage;
    private Texture continueTexture;
    private ImageButton quitBtn;
    private Image quitImage;
    private Texture quitTexture;

    private Label menuLabel;

    private Table menuTable;
    private TextButton.TextButtonStyle textButtonStyle;
    private Skin skin;

    public boolean visible = false;

    public static Main game;

    public MenuScreen(final Main game) {
        MenuScreen.game = game;

        // stage
        menuStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);

        //table
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.center();
        menuTable.setDebug(true);
        menuStage.addActor(menuTable);

        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        menuStage.addActor(quitBtn);
        // continue btn
        // uncomment if new png available! quitTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(continueTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        continueBtn = new ImageButton(textureRegionDrawable);
        menuStage.addActor(continueBtn);

        //label
        menuLabel = new Label("Menu", skin);

        // load skins
        //Skin skin = new Skin(Gdx.files.internal("menu/test.png"));

        // Listener
        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchToGameScreen();
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // add UI-elements to table
        menuTable.add(menuLabel).padBottom(20).row();
        menuTable.add(continueBtn).padBottom(10).row();
        menuTable.add(quitBtn);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(menuStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuStage.act(delta);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        menuStage.dispose();
        skin.dispose();
    }
}
