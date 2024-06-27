package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;

public class Tile {
    String[] types = {"grass", "dirt", "wall", "tree"};

    Texture texture;
    Texture groundTexture; // Texture beneath the tree for example
    Sprite sprite;
    Rectangle rect;
    Rectangle hitbox;
    int mapX;
    int mapY;
    float width;
    float height;
    int type;
    boolean isBlockable;
    String group;

    // Appearance
    float visibility = 1f;

    Tile(float x, float y, int mapX, int mapY, int type, boolean isBlockable, String group) {
        // loadTileNames(); add MAYBE
        this.texture = new Texture("tiles/" + group + "/" + types[type] + ".png");
        this.groundTexture = new Texture("tiles/ground/dirt.png");
        this.width = (float) texture.getWidth() * Main.MULTIPLIER;
        this.height = (float) texture.getHeight() * Main.MULTIPLIER; // width and height adjust to texture size
        sprite = new Sprite(this.texture);
        sprite.setBounds(x, y, width, height);
        rect = new Rectangle(x, y, width, height);
        hitbox = new Rectangle(x, y, Main.TILE_SIZE * Main.MULTIPLIER, Main.TILE_SIZE * Main.MULTIPLIER); // Always 1x1 tile
        this.mapX = mapX;
        this.mapY = mapY;
        this.type = type;
        this.group = group;
        this.isBlockable = isBlockable;
    }

    public void loadTileNames() {
        // Add new tile directories here
        String path = "tiles/";
        String[] directories = {"ground", "blockable"};

        int typesLength = 0;

        for (int i = 0; i < directories.length; i++) {
            typesLength += (int) Gdx.files.internal(path + directories[i] + "/").length();
        }

        String[] types = new String[typesLength];

        int counter = 0;

        for (int i = 0; i < directories.length; i++) {
            FileHandle[] tiles = Gdx.files.internal(path + directories[i] + "/").list();
            for (int j = 0; j < tiles.length; j++) {
                String tileName = tiles[j].name().replace(".png", "");
                System.out.println(tileName);
                types[counter] = tileName;
                counter++;
            }
        }

        this.types = types;
    }


    public void refreshTexture() {
        this.texture = new Texture("tiles/" + this.group + "/" + types[type] + ".png");
        sprite.setTexture(this.texture);
    }

    public void harvest() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (this.rect.overlaps(Main.player.rect)) {
                if (this.type == 0) {
                    this.type = 1;
                    Map.map[this.mapY][this.mapX] = this.type;
                    refreshTexture();
                }
            }
        }
    }

    public void update() {
        harvest();
    }

    public void draw(SpriteBatch batch) {
        if (isBlockable) {
            batch.draw(groundTexture, this.rect.x, this.rect.y, this.hitbox.width, this.hitbox.height);

            if (Main.player.rect.overlaps(this.rect)) { // Change visibility of for example tree, when player is under it.
                visibility -= 0.05f;
                if (visibility <= 0.3f) {
                    visibility = 0.3f;
                }
                this.sprite.setAlpha(visibility);
            } else {
                visibility += 0.05f;
                if (visibility >= 1f) {
                    visibility = 1f;
                }
                this.sprite.setAlpha(visibility);
            }
        }
        this.sprite.draw(batch);
    }
}
