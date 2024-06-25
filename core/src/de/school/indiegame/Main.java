package de.school.indiegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class Main extends ApplicationAdapter {
	public static int[] GAME_SIZE = {400, 225};
	public static int MULTIPLIER = 4;
	public static int[] SCREEN_SIZE = {GAME_SIZE[0] * MULTIPLIER, GAME_SIZE[1] * MULTIPLIER};
	public static int TILE_SIZE = 16;
	public static Map MAP;

	SpriteBatch batch;
	public static Player player;
	OrthographicCamera camera;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		player = new Player((float) GAME_SIZE[0] / 2 * MULTIPLIER, (float) GAME_SIZE[1] / 2 * MULTIPLIER);
		MAP = new Map();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.begin();

		updateSprites();
		drawSprites(batch);

		batch.end();
	}

	public void drawSprites(SpriteBatch batch) {
		// Draw all sprites here
		Map.draw(batch);
		player.draw(batch);

	}

	public void updateSprites() {
		// update all sprites here
		player.update();
	}
	
	@Override
	public void dispose () {
		// Dispose every texture here
		batch.dispose();
		player.texture.dispose();

		for (Tile tile : Map.mapTiles) {
			tile.texture.dispose();
		}
	}
}
