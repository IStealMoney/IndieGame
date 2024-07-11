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

public class Player {
    // Drawing
    Texture texture = new Texture(Gdx.files.internal("player/Bob_vorne.png"));
    Sprite sprite;
    Rectangle rect;
    float width = texture.getWidth() * Main.MULTIPLIER;
    float height = texture.getHeight() * Main.MULTIPLIER;

    // Moving
    Vector2 movement = new Vector2(0,0);
    float speed = 5f * Main.MULTIPLIER * 0.25f;

    // Effects
    Circle translucentCircle;
    float translucentRadius = 200f;
    float minDistance = 80f; // Controls opacity of tree

    Player(float x, float y) {
        this.rect = new Rectangle(x - width / 2, y - height / 2, width, height - height / 2);

        sprite = new Sprite(texture);
        sprite.setBounds(x, y, width, height);
        this.translucentCircle = new Circle(this.rect.x, this.rect.y, translucentRadius);

    }

    public void calculateTranslucentTiles() {
        for (Tile tile : Map.mapTiles) {
            if (tile.tileset.equals("environment")) {
                float dx = Math.abs(tile.rect.x - this.rect.x);
                float dy = Math.abs(tile.rect.y - this.rect.y);
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

    public void handleMovement() {
        Input input = Gdx.input;
        // test map saving
        if (input.isKeyJustPressed(Keys.O)) {
            Map.saveMap();
        }

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
            changeTexture("Bob_hinten");
        } else if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
            movement.y = -speed;
            changeTexture("Bob_vorne");
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

    public void move() {
        // Move map instead of player
        Map.moveMap(movement.x, movement.y);
        this.sprite.setPosition(this.rect.x, this.rect.y);
        this.translucentCircle.setPosition(this.rect.x, this.rect.y);

    }

    public void update() {
        handleMovement();
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
        calculateTranslucentTiles();
    }
}
