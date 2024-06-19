package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import java.io.File;

public class Player {

    // Drawing
    Texture texture = new Texture(Gdx.files.internal("player/player.png"));
    float x;
    float y;
    int width = texture.getWidth() * Main.MULTIPLIER;
    int height = texture.getHeight() * Main.MULTIPLIER;

    // Moving
    Vector2 movement = new Vector2(0,0);
    float speed = 5f;

    Player(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void handleInput() {
        Input input = Gdx.input;
        if (input.isKeyPressed(Keys.A) || input.isKeyPressed(Keys.LEFT)) {
            movement.x = -speed;
        } else if (input.isKeyPressed(Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
            movement.x = speed;
        } else {
            movement.x = 0;
        }

        if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
            movement.y = speed;
        } else if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
            movement.y = -speed;
        } else {
            movement.y = 0;
        }
    }

    public void move() {
        x += movement.x;
        y += movement.y;
    }

    public void update() {
        handleInput();
        move();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x - width, y - height, width, height);
    }
}
