package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.internal.LinkedTreeMap;

import java.io.FileWriter;
import java.io.IOException;

import static de.school.indiegame.Main.SCREEN_SIZE;
import static de.school.indiegame.Map.json;

public class MoneySystem {
    public static Sprite sprite;
    public static Texture coinTexture;
    public static int currentMoney = 0;
    public static String currency = "Bob-Coins";

    MoneySystem() {
        coinTexture = new Texture("money/bobcoin.png");
        sprite = new Sprite(coinTexture);
        sprite.setBounds(Main.SCREEN_SIZE[0] - 20 * Main.MULTIPLIER, Main.SCREEN_SIZE[1] - 20 * Main.MULTIPLIER,
                coinTexture.getWidth() * Main.MULTIPLIER, coinTexture.getHeight() * Main.MULTIPLIER);
        loadMoney();
    }

    public void loadMoney() {
        LinkedTreeMap moneyData = new LinkedTreeMap<>();
        moneyData = json.fromJson(moneyData.getClass(), Gdx.files.internal("money/money_data.json"));

        currentMoney = (int) moneyData.get("current_money");
    }

    public static void saveMoney() {
        LinkedTreeMap moneyData = new LinkedTreeMap<>();

        moneyData.put("current_money", currentMoney);
        String moneyString = json.toJson(moneyData);
        System.out.println(moneyString);

        try {
            FileWriter fW = new FileWriter(Gdx.files.internal("money/money_data.json").toString());
            fW.write(moneyString);
            fW.close();
        } catch (IOException e) {

        }
    }

    public void add(int amount) {
        currentMoney += amount;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
