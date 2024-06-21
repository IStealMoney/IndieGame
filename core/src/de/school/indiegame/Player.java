package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import java.io.File;

public class Player {

    // Drawing
    Texture texture = new Texture(Gdx.files.internal("player/player.png"));
    Rectangle rect;
    float x;
    float y;
    int width = texture.getWidth() * Main.MULTIPLIER;
    int height = texture.getHeight() * Main.MULTIPLIER;

    // Moving
    Vector2 movement = new Vector2(0,0);
    float speed = 5f;

    Player(float x, float y) {
        rect = new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public void handleInput() {
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

        // Add collision for each axis

        // set the y-movement vector according to the input
        if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
            movement.y = speed;
        } else if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
            movement.y = -speed;
        } else {
            movement.y = 0;
        }
    }

    public void move() {
        // Move map instead of player
        Map.moveMap(movement.x, movement.y);
    }

    public void update() {
        handleInput();
        move();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, this.rect.x, this.rect.y, this.width, this.height);
    }
}
