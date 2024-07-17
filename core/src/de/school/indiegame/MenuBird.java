package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.Random;

import static de.school.indiegame.Main.SCREEN_SIZE;
import static de.school.indiegame.Main.batch;
import static de.school.indiegame.PauseScreen.*;

public class MenuBird {
    public static Sprite sprite;
    public static Texture texture;

    private int duckFrame = 0;
    public static float width, height;
    public static float xPosition, yPosition = 0;
    public static Rectangle birdRec;    // for clickable feature
    private String direction = "LeftToRight";
    private String currentDuck = "brownDuck";
    Random rand = new Random();
    private int randHeight, randDirection, randDuck;
    public static boolean duckReachedEnd;
    public static String[] duckTypes = new String[] {"brown1", "brown2", "brown3", "gold1", "gold2", "gold3"};

    double flyInterval = 5f;
    double startFlyTime = System.currentTimeMillis();
    double currentFlyTime = System.currentTimeMillis();

    MenuBird() {
        procedure();
    }

    public void procedure() {
        chooseDirectionRandomly();
        chooseHeightRandomly();
        chooseTextureRandomly();

        sprite = new Sprite(texture);
        sprite.setSize(width, height);
    }

    public void chooseDirectionRandomly() {
        randDirection = rand.nextInt(2);
        if (randDirection == 0) {
            direction = "LeftToRight";
            xPosition = 0;
        } else {
            direction = "RightToLeft";
            xPosition = 1920;
        }
    }

    public void chooseHeightRandomly() {
        yPosition = rand.nextInt(1080);
    }

    public void chooseTextureRandomly() {
        randDuck = rand.nextInt(2);
        if (randDuck == 0) {   //brown duck (duckFrame 0,1,2)
            currentDuck = "duckBrown";
            duckFrame = 0;
        } else {    //gold duck (duckFrame 3,4,5)
            currentDuck = "duckGold";
            duckFrame = 3;
        }

        texture = new Texture(Gdx.files.internal("menu/animals/duck_" + duckTypes[duckFrame] + ".png"));
        width = texture.getWidth()*Main.MULTIPLIER;
        height = texture.getHeight()*Main.MULTIPLIER;
    }

    public void fly() {
        if (direction.equals("LeftToRight")) {
            sprite.setFlip(true, false);
            leftToRight();
        } else if (direction.equals("RightToLeft")) {
            sprite.setFlip(false, false);
            rightToLeft();
        }
    }

    public void leftToRight() {
        if (xPosition%10 == 0) {
            if (currentDuck.equals("duckBrown")) {
                changeTextureBrown();
            } else if (currentDuck.equals("duckGold")) {
                changeTextureGold();
            }
        }
        xPosition += 1;
        sprite.setPosition(xPosition, yPosition);
    }

    public void rightToLeft() {
        if (xPosition%10 == 0) {
            if (currentDuck.equals("duckBrown")) {
                changeTextureBrown();
            } else if (currentDuck.equals("duckGold")) {
                changeTextureGold();
            }
        }
        xPosition -= 1;
        System.out.println(sprite.getX());
        sprite.setPosition(xPosition, yPosition);
    }

    public void changeTextureBrown() {
        if (duckFrame != 2) {
            duckFrame++;
        } else {
            duckFrame = 0;
        }
        texture = new Texture(Gdx.files.internal("menu/animals/duck_" + duckTypes[duckFrame] + ".png"));

        sprite.setTexture(texture);
    }

    public void changeTextureGold() {
        if (duckFrame != 5) {
            duckFrame++;
        } else {
            duckFrame = 3;
        }
        texture = new Texture(Gdx.files.internal("menu/animals/duck_" + duckTypes[duckFrame] + ".png"));
        sprite.setTexture(texture);
    }

    public void waitUnknownTime() {
        //new Timer().scheduleAtFixedRate(task, after, interval);
        rand = new Random(5000);
        //wait random time before flying again
    }

    public void draw() {
        sprite.draw(batch);

        currentFlyTime = System.currentTimeMillis();

        if (currentFlyTime - startFlyTime > flyInterval) {
            fly();
            startFlyTime = System.currentTimeMillis();
        }
    }
}
