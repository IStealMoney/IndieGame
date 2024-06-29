package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import java.io.File;

public class Player {
    // Drawing
    Texture texture = new Texture(Gdx.files.internal("player/player.png"));
    Sprite sprite;
    Rectangle rect;
    float x;
    float y;
    float width = texture.getWidth() * Main.MULTIPLIER;
    float height = texture.getHeight() * Main.MULTIPLIER;

    // Moving
    Vector2 movement = new Vector2(0,0);
    float speed = 5f * Main.MULTIPLIER * 0.25f;
    boolean isScrolling = true;

    Player(float x, float y) {
        this.rect = new Rectangle(x - width / 2, y - height / 2, width, height);

        sprite = new Sprite(texture);
        sprite.setBounds(x, y, width, height);
    }

    public void handleMovement() {
        Input input = Gdx.input;
        // test map saving
        if (input.isKeyJustPressed(Keys.O)) {
            Map.saveMap("map");
        }

        // set the x-movement vector according to the input
        if (input.isKeyPressed(Keys.A) || input.isKeyPressed(Keys.LEFT)) {
            movement.x = -speed;
        } else if (input.isKeyPressed(Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
            movement.x = speed;
        } else {
            movement.x = 0;
        }

        // Calculate input, then move, then check if in collision -> if yes, move map back and set player position to according tile position || x-Axis
        move();


        // Add collision for each axis
        // x-Axis collision
        for (Tile tile : Map.mapTiles) {
            if (tile.isBlockable) {
                if (this.rect.overlaps(tile.hitbox)) {
                    if (movement.x < 0) {
                        float moveAmount =  this.rect.x - (tile.hitbox.x + tile.hitbox.width); // Distance between tile and player
                        Map.moveMap(-moveAmount, 0);
                    }
                    if (movement.x > 0) {
                        float moveAmount = (tile.hitbox.x - tile.hitbox.width) - this.rect.x; // Distance between tile and player
                        Map.moveMap(moveAmount, 0);
                    }
                }
            }
        }
        movement.x = 0;

        // set the y-movement vector according to the input
        if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
            movement.y = speed;
        } else if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
            movement.y = -speed;
        } else {
            movement.y = 0;
        }

        // Calculate input, then move, then check if in collision -> if yes, move map back and set player position to according tile position || y-Axis
        move();

        // y-Axis collision
        for (Tile tile : Map.mapTiles) {
            if (tile.isBlockable) {
                if (this.rect.overlaps(tile.hitbox)) {
                    if (movement.y < 0) {
                        float moveAmount =  this.rect.y - (tile.hitbox.y + tile.hitbox.height); // Distance between tile and player
                        Map.moveMap(0, -moveAmount);
                    }
                    if (movement.y > 0) {
                        float moveAmount =  (tile.hitbox.y - this.rect.height) - this.rect.y; // Distance between tile and player
                        Map.moveMap(0, moveAmount);
                        this.rect.y = tile.hitbox.y - this.height;
                    }
                }
            }
        }
        movement.y = 0;
    }

    public void move() {
        // Move map instead of player
        Map.moveMap(movement.x, movement.y);
        this.sprite.setPosition(this.rect.x, this.rect.y);
    }

    public void update() {
        handleMovement();
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }
}
