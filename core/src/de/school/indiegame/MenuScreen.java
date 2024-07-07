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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// add btns to table and table to stage

public class MenuScreen implements Screen {
    private final Main game;
    private Stage menuStage;
    private TextureRegion textureRegion;
    private TextureRegionDrawable textureRegionDrawable;
    private BitmapFont font;
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

    public MenuScreen(Main game) {
        this.game = game;
        // stage
        menuStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //table
        menuTable = new Table();
        menuTable.setFillParent(true);
        //menuTable.center();
        //menuTable.setDebug(true);
        menuStage.addActor(menuTable);

        //label
        menuLabel = new Label("Menu", skin);

        // quit btn
        quitTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(quitTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        quitBtn = new ImageButton(textureRegionDrawable);
        menuTable.addActor(quitBtn);
        // continue btn
        quitTexture = new Texture(Gdx.files.internal("menu/test.png"));
        textureRegion = new TextureRegion(continueTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        continueBtn = new ImageButton(textureRegionDrawable);
        menuTable.addActor(continueBtn);

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

        // add elements to table
        menuTable.add(menuLabel).padBottom(20).row();
        menuTable.add(continueBtn).padBottom(10).row();
        menuTable.add(quitBtn);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(menuStage);

        // from MenuScreen to GameScreen by pressing ESCAPE
        /*Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                // from MenuScreen to GameScreen
                if (keyCode == Input.Keys.ESCAPE) {
                    game.switchToGameScreen();
                    return true;
                }
                return false;
            }
        });*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        if (skin != null) {
            skin.dispose();
        }
    }
}
