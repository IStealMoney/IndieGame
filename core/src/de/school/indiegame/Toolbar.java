package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Toolbar {
    public static Sprite spriteTb;
    public static Sprite spriteStb;
    public static Texture toolbarTexture;
    public static Texture selectSlotTexture;
    float widthTb, heightTb;
    public static float widthSs, heightSs;
    public static int xPosition;
    public static int yPosition;

    Toolbar() {
        toolbarTexture = new Texture(Gdx.files.internal("toolbar/background.png"));
        widthTb = toolbarTexture.getWidth() * Main.MULTIPLIER;
        heightTb = toolbarTexture.getHeight() * Main.MULTIPLIER;
        xPosition = (int) (Main.SCREEN_SIZE[0] - widthTb);
        yPosition = (int) (Main.SCREEN_SIZE[1] - (Main.SCREEN_SIZE[1]+ heightTb)/2);
        spriteTb = new Sprite(toolbarTexture);
        spriteTb.setBounds(xPosition, yPosition, widthTb, heightTb);
        createSelectToolbar();

        // start with axe selected
        spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*2+4*Main.MULTIPLIER, widthSs, heightSs);
    }

    public static void createSelectToolbar() {
        selectSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));
        widthSs = selectSlotTexture.getWidth() * Main.MULTIPLIER;
        heightSs = selectSlotTexture.getHeight() * Main.MULTIPLIER;
        spriteStb = new Sprite(selectSlotTexture);
    }

    public static void changeSelectToolbar() {
        if (Tool.weaponType == 0) {
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*4+8*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 1) {
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*3+6*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 2) {
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*2+4*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 3) {
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER+2*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 4) {
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, yPosition+4*Main.MULTIPLIER, widthSs, heightSs);
        }
    }

    public void draw(SpriteBatch batch) {
        spriteTb.draw(batch);
        spriteStb.draw(batch);
    }
}







