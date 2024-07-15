package de.school.indiegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static de.school.indiegame.Main.SCREEN_SIZE;

public class MoneySystem {
    public static Sprite sprite;
    public static Texture coinTexture;
    public static int currentMoney = 0;
    public static String currency = "Bob-Coins";

    //interactive customer to sell items
    //needs to be a Tile

    MoneySystem() {
        coinTexture = new Texture("money/bobcoin.png");
        sprite = new Sprite(coinTexture);
        sprite.setBounds(Main.SCREEN_SIZE[0]-20 * Main.MULTIPLIER, Main.SCREEN_SIZE[1]- 20 * Main.MULTIPLIER,
                coinTexture.getWidth()*Main.MULTIPLIER, coinTexture.getHeight()*Main.MULTIPLIER);
        //spriteCus.setBounds();
        //Playeer.width
        //tiles/water.png
    }

    public void add(int amount) {
        currentMoney += amount;
    }

    public void isInRange() {

    }

    public void updateCurrentMoney() {

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
