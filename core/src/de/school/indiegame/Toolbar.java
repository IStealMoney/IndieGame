package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Toolbar {
    private static SpriteBatch batch;
    public static Sprite spriteTb;
    public static Sprite spriteStb;
    public static Texture toolbarTexture;
    public static Texture selectSlotTexture;
    float widthTb, heightTb;
    public static float xPos, yPos;
    public static float widthSs, heightSs;
    public static int xPosition;
    public static int yPosition;

    Toolbar() {
        batch = new SpriteBatch();
        toolbarTexture = new Texture(Gdx.files.internal("toolbar/background.png"));
        widthTb = toolbarTexture.getWidth() * Main.MULTIPLIER;
        heightTb = toolbarTexture.getHeight() * Main.MULTIPLIER;
        xPosition = (int) (Main.SCREEN_SIZE[0] - widthTb);
        yPosition = (int) (Main.SCREEN_SIZE[1] - (Main.SCREEN_SIZE[1]+ heightTb)/2);
        spriteTb = new Sprite(toolbarTexture);
        spriteTb.setBounds(xPosition, yPosition, widthTb, heightTb);
        createSelectToolbar();
    }

    public static void createSelectToolbar() {
        batch = new SpriteBatch();
        selectSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));
        widthSs = selectSlotTexture.getWidth() * Main.MULTIPLIER;
        heightSs = selectSlotTexture.getHeight() * Main.MULTIPLIER;
        xPos = xPosition;
        yPos = yPosition;
        spriteStb = new Sprite(selectSlotTexture);
    }

    public static void changeSelectToolbar() {
        if (Tool.weaponType == 0) {
            spriteStb.setBounds(xPos+4*Main.MULTIPLIER, yPos+4*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 1) {
            xPos = xPosition;
            yPos = yPosition;
        } else if (Tool.weaponType == 2) {
            xPos = xPosition;
            yPos = yPosition;
        }
    }

    public void draw(SpriteBatch batch) {
        spriteTb.draw(batch);
        spriteStb.draw(batch);
    }

    public void update() {

    }
}







