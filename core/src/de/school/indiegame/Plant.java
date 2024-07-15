package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static de.school.indiegame.Main.moneySystem;
import static de.school.indiegame.Main.shape;

public class Plant {
    float x;
    float y;
    int mapX;
    int mapY;
    Rectangle rect;
    Texture texture;
    int id;
    String name;
    int duration;
    double startDuration;
    double value;
    int growthState;
    int maxGrowthState;
    Long durationStartTime = System.currentTimeMillis();
    Long currentDurationTime = System.currentTimeMillis();
    GlyphLayout layout;

    boolean canPress = false;
    double pressedTime = Main.tool.pressedTime;
    double pressedStartTime = System.currentTimeMillis();
    double currentPressedTime;

    Plant(int mapX, int mapY, float x, float y, int id, String name, double duration, double startDuration, double value, int maxGrowthState) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.x = x;
        this.y = y;
        rect = new Rectangle(x, y, Main.TILE_SIZE * Main.MULTIPLIER, Main.TILE_SIZE * Main.MULTIPLIER);
        this.id = id;
        this.name = name;
        this.duration = (int) duration;
        this.startDuration = startDuration;
        this.value = value;
        this.growthState = maxGrowthState;
        this.maxGrowthState = maxGrowthState;
        layout = new GlyphLayout();
        refreshTexture();
    }

    public void refreshTexture() {
        this.texture = Map.plantTextures.get(id).get(growthState);
    }

    public void updateDuration() {
        currentDurationTime = System.currentTimeMillis();
        if (currentDurationTime - durationStartTime >= 100) {
            if (duration > 0) {
                duration -= 1;
                growthState = (int) (duration / (startDuration / (maxGrowthState + 1)));
                refreshTexture();
            }
            durationStartTime = System.currentTimeMillis();
        }
    }

    public void drawDuration(SpriteBatch batch) {
        int S = duration % 60;  // Calculate the remaining seconds
        int H = duration / 60;  // Convert total seconds to minutes
        int M = H % 60;         // Calculate the remaining minutes
        H = H / 60;            // Convert total minutes to hours

        String durationString = "";

        if (H > 0) {
            durationString += H + "h ";
        }
        if (M > 0) {
            durationString += M + "m ";
        }
        if (S > 0) {
            durationString += S + "s";
        }
        layout.setText(Main.font, durationString);

        Main.font.draw(batch, durationString, x, y + texture.getHeight() * Main.MULTIPLIER - layout.height);
    }

    public void harvest() {
        currentPressedTime = System.currentTimeMillis();
        if (currentPressedTime - pressedStartTime >= pressedTime) {
            canPress = true;
            pressedStartTime = System.currentTimeMillis();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (canPress && !Main.mouseAboveHud) {
                if (Tool.weaponType == 1) {
                    if (Tool.hitbox.overlaps(rect)) {
                        if (duration <= 0) {
                            moneySystem.add((int) value);
                            Main.inventory.pickup(id, random.nextInt(1, 4));
                            id = -1;
                            Map.plantMapCoords.removeIf(coords -> (Arrays.equals(coords, new Integer[]{mapX, mapY})));
                        }
                    }
                }
                canPress = false;
            }
        }
    }

    public void update() {
        updateDuration();
        harvest();
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, x, y, Main.TILE_SIZE * Main.MULTIPLIER, Main.TILE_SIZE * Main.MULTIPLIER);
        drawDuration(batch);
    }
}
