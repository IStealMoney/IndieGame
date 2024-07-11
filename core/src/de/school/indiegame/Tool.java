package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Tool {
    public Object rectangle;
    String[] weapons = new String[] {"", "basket", "axe", "pickaxe", "hoe"};
    Texture texture;
    Rectangle hitbox;
    public static int weaponType;
    Sprite sprite;
    float[] offset;

    // Appearance
    boolean isPressed = false;
    double pressedTime = 200f;
    double pressedStartTime = System.currentTimeMillis();
    double currentPressedTime;
    int defaultRotation = 10;
    int rotationAmount = 10;

    Tool(float x, float y, int weaponType) {
        Tool.weaponType = weaponType;
        this.texture = new Texture(Gdx.files.internal("tools/" + weapons[weaponType] + ".png"));
        float width = texture.getWidth() * Main.MULTIPLIER;
        float height = texture.getHeight() * Main.MULTIPLIER;
        offset = new float[] {-width / 2, height / 2 + 10};
        x = x + offset[0];
        y = y + offset[1];
        this.hitbox = new Rectangle(x, y, width, height/2);
        this.sprite = new Sprite(this.texture);
        this.sprite.setBounds(x, y, width, height);
        sprite.setOrigin(width / 2, 0); // Set rotation origin to bottom middle
        sprite.setRotation(defaultRotation);
        sprite.setFlip(false, false);
    }

    public void refreshTexture() {
        if (weaponType != 0) {
            this.texture = new Texture(Gdx.files.internal("tools/" + weapons[weaponType] + ".png"));
            sprite.setTexture(this.texture);
        }
    }

    public void calculateHitbox() {
        this.hitbox = this.sprite.getBoundingRectangle();
        if (sprite.isFlipX()) {
            this.hitbox.width = this.hitbox.width / 2;
            this.hitbox.x += this.hitbox.width;
        } else {
            this.hitbox.width = this.hitbox.width / 2;
        }
    }

    public void handleInput() {
        // Check if mouse is not above hud
        if (!Main.mouseAboveHud) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isPressed) {
                    isPressed = true;
                    pressedStartTime = System.currentTimeMillis();
                }
            }
        }

        // Change tool
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            weaponType = 0;
            Toolbar.changeSelectToolbar();
            refreshTexture();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            weaponType = 1;
            Toolbar.changeSelectToolbar();
            refreshTexture();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            weaponType = 2;
            Toolbar.changeSelectToolbar();
            refreshTexture();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            weaponType = 3;
            Toolbar.changeSelectToolbar();
            refreshTexture();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            weaponType = 4;
            Toolbar.changeSelectToolbar();
            refreshTexture();
        }

        // Flip Sprite
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (sprite.isFlipX()) {
                sprite.setFlip(false, false);
                sprite.setRotation(defaultRotation);
                sprite.setX((Main.SCREEN_SIZE[0] / 2 - sprite.getWidth() / 2) + offset[0]);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!sprite.isFlipX()) {
                sprite.setFlip(true, false);
                sprite.setX((Main.SCREEN_SIZE[0] / 2 - sprite.getWidth() / 2) - offset[0]);
                sprite.setRotation(-defaultRotation);
            }

        }
    }

    public void animate() {
        if (isPressed) {
            currentPressedTime = System.currentTimeMillis();

            // flip rotation
            if (sprite.isFlipX()) {
                sprite.rotate(-rotationAmount);
            } else {
                sprite.rotate(rotationAmount);
            }

            if (currentPressedTime - pressedStartTime >= pressedTime) {
                isPressed = false;
                if (sprite.isFlipX()) {
                    sprite.setRotation(-defaultRotation);
                } else {
                    sprite.setRotation(defaultRotation);
                }

                pressedStartTime = System.currentTimeMillis();
            }
        }
    }

    public void update() {
        calculateHitbox();
        handleInput();
        animate();

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
