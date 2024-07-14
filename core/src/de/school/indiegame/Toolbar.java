package de.school.indiegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Toolbar {
    public static Sprite spriteTb;
    public static Sprite spriteStb;
    public static Texture toolbarTexture;
    public static Texture selectSlotTexture;
    float widthTb, heightTb;
    public static float widthSs, heightSs;
    public static int xPosition;
    public static int yPosition;
    public static Rectangle targetBasket, targetAxe, targetPickaxe, targetHoe;

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
        targetBasket = new Rectangle((int) (xPosition+4*Main.MULTIPLIER), (int) ((yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*3+6*Main.MULTIPLIER), (int) widthSs, (int) heightSs);
        targetAxe = new Rectangle((int) (xPosition+4*Main.MULTIPLIER), (int) ((yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*2+4*Main.MULTIPLIER), (int) widthSs, (int) heightSs);
        targetPickaxe = new Rectangle((int) (xPosition+4*Main.MULTIPLIER), (int) ((yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER+2*Main.MULTIPLIER), (int) widthSs, (int) heightSs);
        targetHoe = new Rectangle((int) (xPosition+4*Main.MULTIPLIER), (int) (yPosition+4*Main.MULTIPLIER), (int) widthSs, (int) heightSs);
    }

    public static void changeSelectToolbar() {
        if (Tool.weaponType == 0) { // selected item
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*4+8*Main.MULTIPLIER, widthSs, heightSs);
            Inventory.isVisible = false;
        } else if (Tool.weaponType == 1) {  // basket
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*3+6*Main.MULTIPLIER, widthSs, heightSs);
        } else if (Tool.weaponType == 2) {  // axe
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER*2+4*Main.MULTIPLIER, widthSs, heightSs);
            Inventory.isVisible = false;
        } else if (Tool.weaponType == 3) {  //pickaxe
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, (yPosition+4*Main.MULTIPLIER)+selectSlotTexture.getWidth()*Main.MULTIPLIER+2*Main.MULTIPLIER, widthSs, heightSs);
            Inventory.isVisible = false;
        } else if (Tool.weaponType == 4) {  //hoe
            spriteStb.setBounds(xPosition+4*Main.MULTIPLIER, yPosition+4*Main.MULTIPLIER, widthSs, heightSs);
            Inventory.isVisible = false;
        }
    }

    public void handleInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                screenY = Gdx.graphics.getHeight() - screenY;
                if (targetBasket.contains(screenX, screenY) && !Inventory.isVisible) {
                    Tool.weaponType = 1;
                    changeSelectToolbar();
                    Main.tool.refreshTexture();
                    Inventory.isVisible = true;
                    return true;
                }
                if (targetBasket.contains(screenX, screenY) && Inventory.isVisible) {
                    Inventory.isVisible = false;
                    return true;
                }
                if (targetAxe.contains(screenX, screenY)) {
                    Tool.weaponType = 2;
                    changeSelectToolbar();
                    Main.tool.refreshTexture();
                    Inventory.isVisible = false;
                    return true;
                }
                if (targetPickaxe.contains(screenX, screenY)) {
                    Tool.weaponType = 3;
                    changeSelectToolbar();
                    Main.tool.refreshTexture();
                    Inventory.isVisible = false;
                    return true;
                }
                if (targetHoe.contains(screenX, screenY)) {
                    Tool.weaponType = 4;
                    changeSelectToolbar();
                    Main.tool.refreshTexture();
                    Inventory.isVisible = false;
                    return true;
                }
                return false;
            }
        });
    }

    public void draw(SpriteBatch batch) {
        spriteTb.draw(batch);
        spriteStb.draw(batch);
    }

    public void update() {
        handleInput();
    }
}







