package de.school.indiegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Customer {
    private static Sprite sprite;
    public static Texture cusInvTexture;
    public static boolean cusInvVisible;
    private float xPos;

    Customer() {
        cusInvTexture = new Texture("customer/inventory.png");
        sprite = new Sprite(cusInvTexture);
        xPos = Inventory.rect.x-Inventory.backgroundTexture.getWidth()*Main.MULTIPLIER-20;
        sprite.setBounds(xPos, Inventory.rect.y, Inventory.width, Inventory.height);
        }

    public static void handleInput() {
        cusInvVisible = true;
        Inventory.isVisible = true;
    }

    public void update() {
        handleInput();
    }

    public static void draw(SpriteBatch batch) {
        if (cusInvVisible) {
            sprite.draw(batch);
        }
    }
}
