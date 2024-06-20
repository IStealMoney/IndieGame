package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Tile {
    String[] types = {"grass", "dirt"};

    Texture texture;
    Rectangle rect;
    float x;
    float y;
    float width = Main.TILE_SIZE * Main.MULTIPLIER;
    float height = Main.TILE_SIZE * Main.MULTIPLIER;
    int type;

    Tile(float x, float y, int type) {
        rect = new Rectangle(x, y, width, height);
        this.type = type;
        this.texture = new Texture("tiles/" + types[type] + ".png");
    }

    public void refreshTexture() {
        this.texture = new Texture("tiles/" + types[type] + ".png");
    }

    public void harvest() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (this.rect.overlaps(Main.player.rect)) {
                if (this.type == 0) {
                    this.type = 1;
                    refreshTexture();
                }
            }
        }
    }

    public void update() {
        harvest();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, this.rect.x, this.rect.y, this.width, this.height);
    }
}
