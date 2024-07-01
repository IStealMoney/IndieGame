package de.school.indiegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;
//lol
public class Main extends ApplicationAdapter {
	public static int[] GAME_SIZE = {480, 270};
	public static float MULTIPLIER;
	public static float[] SCREEN_SIZE = new float[2];
	public static int TILE_SIZE = 16;
	public static Map MAP;

	SpriteBatch batch;
	ShapeRenderer shape;
	public static Player player;
	public static Tool tool;

	@Override
	public void create () {
		SCREEN_SIZE[0] = Gdx.graphics.getWidth();
		SCREEN_SIZE[1] = Gdx.graphics.getHeight();
		MULTIPLIER = (SCREEN_SIZE[0] / GAME_SIZE[0] + SCREEN_SIZE[1] / GAME_SIZE[1]) / 2;
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		shape.setAutoShapeType(true);
		player = new Player((float) SCREEN_SIZE[0] / 2, (float) SCREEN_SIZE[1] / 2);
		tool = new Tool(player.rect.x, player.rect.y, 0);
		MAP = new Map();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		shape.begin();
		batch.begin();


		updateSprites();
		drawSprites(batch);

		batch.end();
		shape.end();

	}

	public void drawSprites(SpriteBatch batch) {
		// Draw all sprites here
		Map.draw(batch);
		player.draw(batch);
		tool.draw(batch);
	}

	public void updateSprites() {
		// update all sprites here
		player.update();
		tool.update();
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
}
