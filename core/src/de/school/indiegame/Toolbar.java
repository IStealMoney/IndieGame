package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Toolbar {
    private SpriteBatch batch;
    Sprite sprite;
    Texture toolbarTexture;
    Texture selectSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));
    float width, height;
    public static int xPosition;
    public static int yPosition;

    Toolbar() {
        batch = new SpriteBatch();
        toolbarTexture = new Texture(Gdx.files.internal("toolbar/background.png"));
        width = toolbarTexture.getWidth() * Main.MULTIPLIER;
        height = toolbarTexture.getHeight() * Main.MULTIPLIER;
        xPosition = (int) (Main.SCREEN_SIZE[0] - width);
        yPosition = (int) (Main.SCREEN_SIZE[1] - (Main.SCREEN_SIZE[1]+height)/2);
        sprite = new Sprite(toolbarTexture);

        sprite.setBounds(xPosition, yPosition, width, height);
    }

    public static void changeSelectToolbar() {

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update() {

    }
}







