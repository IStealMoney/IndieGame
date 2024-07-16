package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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
    boolean isDestructible;
    boolean isIndestructible;
    boolean isCustomer;
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
        setProperties();
    }

    public void setProperties() {
        if(tileset.equals("customer")) {
            isCustomer = true;
        }
        if (tileset.equals("indestructible")) {
            isBlockable = true;
            isIndestructible = true;
        }
        if (tileset.equals("destructible")) {
            isBlockable = true;
            isDestructible = true;
            this.health = new Random().nextInt(1,4);
        }
        if (tileset.equals("environment")) {
            isTranslucent = true;
            isAxeable = true;
            this.health = new Random().nextInt(3,6);
        }

        if (tileset.equals("ground")) {
            isHarvestable = true;
        }

        // Set ground tiles, that are beneath destructible tiles to not harvestable
        if (tileset.equals("destructible")) {
           /* for (Tile tile : Map.mapTiles) {
                if (tile.tileset.equals("ground") && tile.mapX == this.mapX && tile.mapY == this.mapY) {
                    tile.isHarvestable = false;
                }
            }*/
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

    public void plant() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (canPress && !Main.mouseAboveHud) {
                if (this.rect.overlaps(Tool.hitbox)) {
                    if (Tool.weaponType == 0) {
                        if (isHarvestable) {
                            if (Inventory.activeItem[0] != -1) {
                                int selectedItem = Inventory.activeItem[0];
                                String itemName = Main.inventory.itemData.get(selectedItem).get("name").toString();
                                double itemDuration = Double.parseDouble(Main.inventory.itemData.get(selectedItem).get("duration").toString());
                                double itemValue = Double.parseDouble(Main.inventory.itemData.get(selectedItem).get("value").toString());

                                for (Integer[] coords : Map.plantMapCoords) {
                                    if (coords[0] == mapX && coords[1] == mapY) {
                                        return;
                                    }
                                }


                                if (type == 2) { // is farm land
                                    Map.plants.add(new Plant(mapX, mapY, this.rect.x, this.rect.y, selectedItem, itemName, itemDuration, itemDuration, itemValue, Map.plantTextures.get(selectedItem).size() - 1, Map.plantTextures.get(selectedItem).size() - 1));
                                    Inventory.activeItem[1] -= 1;
                                    if (Inventory.activeItem[1] <= 0) { // if amount of active item is 0
                                        Main.inventory.resetActiveItem();
                                    }
                                    Map.plantMapCoords.add(new Integer[]{mapX, mapY});
                                }
                            }
                        }
                    }
                }
                canPress = false;
            }
        }
    }

    public void harvest() {
        // Calculate if player can press again
        currentPressedTime = System.currentTimeMillis();
        if (currentPressedTime - pressedStartTime >= pressedTime) {
            canPress = true;
            pressedStartTime = System.currentTimeMillis();
        }

        // Customer inventory
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!Main.mouseAboveHud) {
                if (this.rect.overlaps(Tool.hitbox)) {
                    if (Tool.weaponType == 1) { //basket
                        if (isCustomer) {
                            Customer.handleInput();
                        }
                    }
                }
            }
        }


        // Tool harvest
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (canPress && !Main.mouseAboveHud) {
                if (this.rect.overlaps(Tool.hitbox)) {
                    if (Tool.weaponType == 2) { // axe
                        if (isAxeable) {
                            this.health -= 1;
                            if (this.health == 0) {
                                this.type = -1;
                            }
                            updateTileOnMap();
                        }
                    }
                    if (Tool.weaponType == 3) { // pickaxe
                        if (isDestructible) {
                            this.health--;
                            if (this.health == 0) {
                                this.type = -1;
                            }
                            updateTileOnMap();
                        }
                    }
                    if (Tool.weaponType == 4) { // hoe
                        if (isHarvestable) {
                            if (this.type == 0) {
                                this.type = 1;
                                updateTileOnMap();
                            }
                            if (this.type == 1) {
                                this.type = 2;
                                updateTileOnMap();
                            }
                        }
                    }
                }
                //canPress = false;
            }
        }
    }

    public void update() {
        harvest();
        plant();
    }

    public void draw(SpriteBatch batch) {
        if (isTranslucent) {
            this.sprite.setAlpha(opacity);
        }

        this.sprite.draw(batch);
    }
}