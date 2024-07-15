package de.school.indiegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import static de.school.indiegame.Main.batch;

public class Customer {
    private static final int TILE_SIZE = 16;
    private static final int MAP_SIZE = 512;
    private Vector2 customerPosition;
    private Vector2 playerPosition;
    private float interactionRange = 128;
    public static Sprite cusInvSprite;
    public static Texture cusInvTexture;

    Customer() {

        }

    public static void showSellTextures() {
        cusInvTexture = new Texture("customer/inventory.png");
        cusInvSprite = new Sprite(cusInvTexture);
        cusInvSprite.setBounds(Main.SCREEN_SIZE[0]-20 * Main.MULTIPLIER, Main.SCREEN_SIZE[1]- 20 * Main.MULTIPLIER,
                cusInvTexture.getWidth()*Main.MULTIPLIER, cusInvTexture.getHeight()*Main.MULTIPLIER);
        cusInvSprite.draw(batch);
        System.out.println("customer is clickable");
    }
}
