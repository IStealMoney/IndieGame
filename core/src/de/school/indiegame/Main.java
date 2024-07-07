package de.school.indiegame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static de.school.indiegame.MenuScreen.game;

// Main doesn't contain any logic or rendering code bcs it extends Game
// class for shared resources

public class Main extends Game {
	public static int[] GAME_SIZE = {480, 270};
	public static float MULTIPLIER;
	public static float[] SCREEN_SIZE = new float[2];
	public static int TILE_SIZE = 16;
	public static Map MAP;

	static SpriteBatch batch;
	static ShapeRenderer shape;
	public static Player player;
	public static Tool tool;
	public static BitmapFont font;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;

	@Override
	public void create () {
		// for GameScreen
		gameScreen = new GameScreen(this);
		//menuScreen = new MenuScreen(this);
		SCREEN_SIZE[0] = Gdx.graphics.getWidth();
		SCREEN_SIZE[1] = Gdx.graphics.getHeight();
		MULTIPLIER = (SCREEN_SIZE[0] / GAME_SIZE[0] + SCREEN_SIZE[1] / GAME_SIZE[1]) / 2;
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		shape.setAutoShapeType(true);
		BitmapFont font = new BitmapFont();
		player = new Player((float) SCREEN_SIZE[0] / 2, (float) SCREEN_SIZE[1] / 2);
		tool = new Tool(player.rect.x, player.rect.y, 0);
		MAP = new Map();
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose () {
		// Dispose every texture here
		batch.dispose();
		player.texture.dispose();
		tool.texture.dispose();

		for (Tile tile : Map.mapTiles) {
			tile.texture.dispose();
			tile.sprite.getTexture().dispose();
		}
	}

	public void switchToMenuScreen() {
		setScreen(menuScreen);
	}

	public void switchToGameScreen() {
		setScreen(gameScreen);
	}
}
