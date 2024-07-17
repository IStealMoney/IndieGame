package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import java.util.HashMap;

import static de.school.indiegame.Main.shape;

public class Player {
    // Drawing
    Texture texture = new Texture(Gdx.files.internal("player/Bob_vorne.png"));
    Sprite sprite;
    static Rectangle rect;
    float width = texture.getWidth() * Main.MULTIPLIER;
    float height = texture.getHeight() * Main.MULTIPLIER;

    // Moving
    static Vector2 movement = new Vector2(0,0);
    float speedModifier = 5f;
    float speed = speedModifier * Main.MULTIPLIER * 0.25f;

    // Effects
    Circle translucentCircle;
    float translucentRadius = 200f;
    float minDistance = 140f; // Controls opacity of tree

    // Camera collision
    Rectangle mapLeftRect;
    Rectangle mapRightRect;
    Rectangle mapBottomRect;
    Rectangle mapTopRect;
    Boolean[] mapCollisions = {true, true, true, true};
    float mapRectsSize = speedModifier;

    Player(float x, float y) {
        this.rect = new Rectangle(x - width / 2, y - height / 2, width, height - height / 2);

        sprite = new Sprite(texture);
        sprite.setBounds(x, y, width, height);
        this.translucentCircle = new Circle(this.rect.x, this.rect.y, translucentRadius);

        mapLeftRect = new Rectangle(0 - mapRectsSize, Main.SCREEN_SIZE[1] / 2, mapRectsSize, mapRectsSize);
        mapRightRect = new Rectangle(Main.SCREEN_SIZE[0] , Main.SCREEN_SIZE[1] / 2, mapRectsSize, mapRectsSize);
        mapTopRect = new Rectangle(Main.SCREEN_SIZE[0] / 2, Main.SCREEN_SIZE[1], mapRectsSize, mapRectsSize);
        mapBottomRect = new Rectangle(Main.SCREEN_SIZE[0] / 2, 0 - mapRectsSize, mapRectsSize, mapRectsSize);
    }

    public void calculateTranslucentTiles() {
        for (Tile tile : Map.mapTiles) {
            if (tile.tileset.equals("environment")) {
                float dx = Math.abs(tile.rect.x - rect.x);
                float dy = Math.abs(tile.rect.y - rect.y);
                double tileDistance = Math.sqrt((dx * dx) + (dy * dy));

                if (tileDistance < translucentRadius) {
                    if (tileDistance > minDistance) {
                        tile.opacity = (float) (tileDistance / translucentRadius);
                    }
                }
            }
        }
    }

    public void changeTexture(String textureName) {
        Texture newTexture = new Texture(Gdx.files.internal("player/" + textureName + ".png"));
        this.sprite.setTexture(newTexture);
        this.texture = newTexture;

        // disable/enable tool
        if (textureName.contains("hinten")) {
            Tool.isHidden = true;
        } else {
            Tool.isHidden = false;
        }
    }

    public boolean checkXMapCollision() {
        // calculate if player would move out of map
        mapCollisions[0] = false;
        mapCollisions[1] = false;
        for (Tile tile : Map.mapTiles) {
            if (mapLeftRect.overlaps(tile.hitbox)) {
                mapCollisions[0] = true;
            }
            if (mapRightRect.overlaps(tile.hitbox)) {
                mapCollisions[1] = true;
            }
        }
        boolean collidesWithXMap = true;

        if (!mapCollisions[0] || !mapCollisions[1]) {
            collidesWithXMap = false;
        }

        // ensure player moves back to center
        // if player is left of center

        if (rect.x + width / 2 < Main.SCREEN_SIZE[0] / 2 && movement.x > 0) {
            collidesWithXMap = false;
        }
        if (rect.x + width / 2 > Main.SCREEN_SIZE[0] / 2 && movement.x < 0) {
            collidesWithXMap = false;
        }

        // if player is right of center

        return collidesWithXMap;
    }

    public boolean checkYMapCollision() {
        // calculate if player would move out of map
        mapCollisions[2] = false;
        mapCollisions[3] = false;
        for (Tile tile : Map.mapTiles) {
            if (mapTopRect.overlaps(tile.hitbox)) {
                mapCollisions[2] = true;
            }
            if (mapBottomRect.overlaps(tile.hitbox)) {
                mapCollisions[3] = true;
            }
        }
        boolean collidesWithYMap = true;

        if (!mapCollisions[2] || !mapCollisions[3]) {
            collidesWithYMap = false;
        }

        // ensure player moves back to center
        // if player is above of center
        if (rect.y - height / 2 > Main.SCREEN_SIZE[1] / 2 && movement.y < 0) {
            collidesWithYMap = false;
        }
        if (rect.y + rect.height / 2 < Main.SCREEN_SIZE[1] / 2  && movement.y > 0) {
            collidesWithYMap = false;
        }
        return collidesWithYMap;
    }

    public void handleMovement() {
        Input input = Gdx.input;

        // sprinting
        if (input.isKeyPressed(Keys.SHIFT_LEFT)) {
            speedModifier = 5f;
        } else {
            speedModifier = 5f;
        }
        speed = speedModifier * Main.MULTIPLIER * 0.25f;

        // set the x-movement vector according to the input
        if (input.isKeyPressed(Keys.A) || input.isKeyPressed(Keys.LEFT)) {
            movement.x = -speed;
            changeTexture("Bob_links");
        } else if (input.isKeyPressed(Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
            movement.x = speed;
            changeTexture("Bob_rechts");
        } else {
            movement.x = 0;
        }

        // Calculate input, then move, then check if in collision -> if yes, move map back and set player position to according tile position || x-Axis
        moveX();

        // Add collision for each axis
        // x-Axis collision
        for (Tile tile : Map.mapTiles) {
            if (tile.isBlockable) {
                if (rect.overlaps(tile.hitbox)) {
                    if (movement.x < 0) {
                        float moveAmount = rect.x - (tile.hitbox.x + tile.hitbox.width); // Distance between tile and player

                        if (!checkXMapCollision()) {
                            rect.x -= moveAmount;
                        } else {
                            Map.moveMap(-moveAmount, 0);
                        }
                    }
                    if (movement.x > 0) {
                        float moveAmount = (tile.hitbox.x - tile.hitbox.width) - rect.x; // Distance between tile and player

                        if (!checkXMapCollision()) {
                            rect.x += moveAmount;
                        } else {
                            Map.moveMap(moveAmount, 0);
                        }
                    }
                }
            }
        }

        movement.x = 0;

        // set the y-movement vector according to the input
        if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
            movement.y = speed;
            changeTexture("Bob_hinten");
        } else if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
            movement.y = -speed;
            changeTexture("Bob_vorne");
        } else {
            movement.y = 0;
        }

        // Calculate input, then move, then check if in collision -> if yes, move map back and set player position to according tile position || y-Axis
        moveY();

        // y-Axis collision
        for (Tile tile : Map.mapTiles) {
            if (tile.isBlockable) {
                if (rect.overlaps(tile.hitbox)) {
                    if (movement.y < 0) {
                        float moveAmount = rect.y - (tile.hitbox.y + tile.hitbox.height); // Distance between tile and player
                        if (!checkYMapCollision()) {
                            rect.y -=moveAmount;
                        } else {
                            Map.moveMap(0, -moveAmount);
                        }
                    }
                    if (movement.y > 0) {
                        float moveAmount =  (tile.hitbox.y - rect.height) - rect.y; // Distance between tile and player
                        System.out.println(checkYMapCollision());
                        if (!checkYMapCollision()) {
                            rect.y += moveAmount;
                        } else {
                            Map.moveMap(0, moveAmount);
                        }
                    }
                }
            }
        }
        movement.y = 0;

        // "animate" rest of inputs
        if ((Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) && (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))) {
            changeTexture("Bob_hinten_links");
        }
        if ((Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) && (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))) {
            changeTexture("Bob_hinten_rechts");
        }
        if ((Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) && (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))) {
            changeTexture("Bob_vorne_links");
        }
        if ((Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) && (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))) {
            changeTexture("Bob_vorne_rechts");
        }
    }

    public void moveX() {
        // X movement
        Map.moveMap(movement.x, 0);

        // switch movement method, if player would move out of map
        if (checkXMapCollision()) {
            // Move map instead of player
            Map.moveMap(movement.x, 0);
        } else {
            // move player
            rect.x += movement.x;
            // check if player would walk out of map
            if (rect.x < 0) {
                rect.x = 0;
            }
            if (rect.x + width > Main.SCREEN_SIZE[0]) {
                rect.x= Main.SCREEN_SIZE[0] - width;
            }
        }

        Map.moveMap(-movement.x, 0);
    }

    public void moveY() {
        // Y movement
        Map.moveMap(0, movement.y);
        // switch movement method, if player would move out of map
        if (checkYMapCollision()) {
            // Move map instead of player
            Map.moveMap(0, movement.y);
        } else {
            // move player
            rect.y += movement.y;
            // Y movement
            // check if player would walk out of map
            if (rect.y < 0) {
                rect.y -= movement.y;
            }
            if (rect.y + height > Main.SCREEN_SIZE[1]) {
                rect.y -= movement.y;
            }
        }

        Map.moveMap(0, -movement.y);

        this.sprite.setPosition(rect.x, rect.y);
        this.translucentCircle.setPosition(rect.x, rect.y);

    }

    public void update() {
        handleMovement();
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
        calculateTranslucentTiles();
    }
}