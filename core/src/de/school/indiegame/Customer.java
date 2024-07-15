package de.school.indiegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Customer {
    private static final int TILE_SIZE = 16;
    private static final int MAP_SIZE = 512;
    private Vector2 customerPosition;
    private Vector2 playerPosition;
    private float interactionRange = 128;
    public static Sprite interactivebtnSprite;
    public static Texture interactivebtnTexture;

    Customer() {
        interactivebtnTexture = new Texture("customer/interactiveBtn.png");
        interactivebtnSprite = new Sprite(interactivebtnTexture);
        interactivebtnSprite.setBounds(Main.SCREEN_SIZE[0]-20 * Main.MULTIPLIER, Main.SCREEN_SIZE[1]- 20 * Main.MULTIPLIER,
                interactivebtnTexture.getWidth()*Main.MULTIPLIER, interactivebtnTexture.getHeight()*Main.MULTIPLIER);
        }

    public static void showSellTextures() {
        System.out.println("customer is clickable");
    }
}
