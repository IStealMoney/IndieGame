package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Tile {
    Texture texture;
    int[] textureIndex;
    String tileset;
    Sprite sprite;
    Rectangle rect;
    Rectangle hitbox;

    int mapX;
    int mapY;
    float width;
    float height;
    int type;
    boolean isBlockable;
    boolean isTranslucent;
    boolean isHarvestable;
    boolean isAxeable;
    String group;

    // Appearance
    float opacity = 1f;

    // Breaking
    int health;
    boolean canPress = false;
    double pressedTime = Main.tool.pressedTime;
    double pressedStartTime = System.currentTimeMillis();
    double currentPressedTime;

    Tile(String tileset, int[] textureIndex, float x, float y, int mapX, int mapY, int type) {
        this.texture = new Texture(Map.tilesetPixmaps.get(tileset)[textureIndex[1]][textureIndex[0]]);
        this.textureIndex = textureIndex;
        this.tileset = tileset;
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
        setProperties();
    }

    public void setProperties() {
        if (tileset.equals("blockable")) {
            isBlockable = true;
        }
        if (tileset.equals("environment")) {
            isTranslucent = true;
            isAxeable = true;
            this.health = new Random().nextInt(3,6);
        }

        if (tileset.equals("ground")) {
            isHarvestable = true;
        }
        // Set ground tiles, that are beneath blockable tiles to not harvestable
        if (tileset.equals("blockable")) {
            for (Tile tile : Map.mapTiles) {
                if (tile.tileset.equals("ground") && tile.mapX == this.mapX && tile.mapY == this.mapY) {
                    tile.isHarvestable = false;
                }
            }
        }
    }

    public void refreshTexture() {
        this.textureIndex = new int[] {this.type % Map.tilesetSize, this.type / Map.tilesetSize};
        this.texture = new Texture(Map.tilesetPixmaps.get(tileset)[textureIndex[1]][textureIndex[0]]);
        sprite.setTexture(this.texture);
    }

    public void updateTileOnMap() {
        Map.maps.get(tileset)[mapY][mapX] = this.type;
        // Delete tile, before error occurs when refreshing texture
        if (this.type == -1) {
            return;
        }
        refreshTexture();

    }

    public void harvest() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (this.rect.overlaps(Main.player.rect)) {
                if (this.type == 0) {
                    this.type = 1;
                    updateTileOnMap();
                }
            }
        }

        // Calculate if player can press again
        currentPressedTime = System.currentTimeMillis();
        if (currentPressedTime - pressedStartTime >= pressedTime) {
            canPress = true;
            pressedStartTime = System.currentTimeMillis();
        }

        // Tool harvest
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (canPress) {
                if (this.rect.overlaps(Main.tool.hitbox)) {
                    if (Main.tool.weaponType == 0) { // axe
                        if (isAxeable) {
                            this.health -= 1;
                            if (this.health == 0) {
                                this.type = -1;
                            }
                            updateTileOnMap();
                        }
                    }
                    if (Main.tool.weaponType == 2) {
                        if (isHarvestable) {
                            if (this.type == 0) {
                                this.type = 1;
                                updateTileOnMap();
                            }
                        }
                    }

                }
                canPress = false;
            }
        }
    }

    public void update() {
        harvest();
    }

    public void draw(SpriteBatch batch) {
        if (isTranslucent) {
            this.sprite.setAlpha(opacity);
        }

        this.sprite.draw(batch);
    }
}
