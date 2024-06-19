package de.school.indiegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    String[] types = {"grass", "dirt"};

    Texture texture;
    float x;
    float y;
    float width = Main.TILE_SIZE * Main.MULTIPLIER;
    float height = Main.TILE_SIZE * Main.MULTIPLIER;
    int type;

    Tile(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.texture = new Texture("tiles/" + types[type] + ".png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
