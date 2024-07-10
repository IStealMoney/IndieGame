package de.school.indiegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Inventory {
    Json json = new Json();
    String inventoryPath = "inventory/inventory.json";
    GlyphLayout layout = new GlyphLayout();

    public static int[] size = new int[] {9, 3};
    public static int[][][] inventory = new int[size[1]][size[0]][2]; // in the left column store the item id, in the right column store the item amount
    public static int[] selectedSlot = {0, 0};

    Texture backgroundTexture = new Texture(Gdx.files.internal("inventory/background.png"));
    Texture selectedSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));

    float scaler = 0.75f;
    float width = backgroundTexture.getWidth() * Main.MULTIPLIER * scaler;
    float height = backgroundTexture.getHeight() * Main.MULTIPLIER * scaler;
    Rectangle rect;
    Rectangle clickableRect;
    Rectangle mouseRect = new Rectangle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1, 1);
    float inventoryBorder = 4 * Main.MULTIPLIER * scaler;
    float slotSize = 20;
    int offset = 2;
    int maxAmount = 64;
    int itemSize = (int) ((slotSize + offset) * Main.MULTIPLIER * scaler);

    public static boolean isVisible;

    Inventory(float x, float y) {
        rect = new Rectangle(x, y, width, height);
        clickableRect = new Rectangle(x +  inventoryBorder, y + inventoryBorder, width - (inventoryBorder * 2), height - (inventoryBorder * 2)); // Rectangle without texture borders
        loadInventory();
    }

    public void loadInventory() {
        inventory = json.fromJson(inventory.getClass(), Gdx.files.internal(inventoryPath));
    }

    public void saveInventory() {
        String inventoryString = json.toJson(inventory);

        try {
            FileWriter fileWriter = new FileWriter(Gdx.files.internal(inventoryPath).toString());
            fileWriter.write(inventoryString);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void findAvailableSlot(int id, int amount) {
        for (int i = 0; i < size[1]; i++) {
            for (int j = 0; j < size[0]; j++) {
                int invAmount = inventory[i][j][1];

                if (inventory[i][j][0] == id && invAmount < maxAmount) {
                    distributeAmount(id, new int[] {j, i}, amount);
                }
            }
        }
    }

    public void distributeAmount(int id, int[] startSlot, int amount) {
        if (amount > 0) {
            int invId = inventory[startSlot[1]][startSlot[0]][0];
            int invAmount = inventory[startSlot[1]][startSlot[0]][1];

            if (invId == id) {
                if (invAmount < maxAmount) {
                    int difAmount = maxAmount - invAmount;

                    if (difAmount > amount) {
                        amount -= difAmount;
                        inventory[startSlot[1]][startSlot[0]][1] += difAmount;
                    } else {
                        amount -= amount - difAmount;
                    }
                }
            }
        } else {
            return;
        }

    }

    public void add(int[] slot, int id, int amount) {
        if (inventory[slot[1]][slot[0]][0] == id || inventory[slot[1]][slot[0]][0] == -1) { // if slot has same item id or is empty
            inventory[slot[1]][slot[0]][0] = id;
            inventory[slot[1]][slot[0]][1] += amount;
        }
    }

    public void remove(int[] slot, int amount) {
        inventory[slot[1]][slot[0]][1] -= amount;

        if (inventory[slot[1]][slot[0]][1] <= 0) { // if amount is 0, delete slot
            inventory[slot[1]][slot[0]][0] = -1; // set item id to 0
            inventory[slot[1]][slot[0]][1] = 0; // set amount to 0
        }
    }

    public void handleInput() {
        // Check if mouse is hovering over inventory
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        //System.out.println(mouseX  +" " + mouseY);
        //System.out.println(rect.x + " " + rect.y);
        mouseRect.setPosition(mouseX, mouseY);

        if (rect.contains(mouseRect) && isVisible) {
            Main.mouseAboveHud = true;
        } else {
            Main.mouseAboveHud = false;
        }

        // Handle visibility
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            isVisible = !isVisible;
            selectedSlot[0] = -1;
            selectedSlot[1] = -1;
        }

        // Handle click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (clickableRect.contains(mouseRect)) {
                // coordinate system begins in lower left corner
                float dx = mouseX - clickableRect.x;
                float dy = mouseY - clickableRect.y;

                selectedSlot[0] = (int) (dx / itemSize);
                selectedSlot[1] = (int) (dy / itemSize);
            }
        }
    }

    public void update() {
        handleInput();
    }

    public void draw(SpriteBatch batch) {
        if (isVisible) {
            // draw background
            batch.draw(backgroundTexture, rect.x, rect.y, rect.width, rect.height);

            // draw selectedSlot
            if (selectedSlot[0] != -1 && selectedSlot[1] != -1) {
                float[] selectedSlotOffset = {scaler * selectedSlot[0] * Main.MULTIPLIER + (selectedSlot[0] * Main.MULTIPLIER * scaler), selectedSlot[1] * Main.MULTIPLIER * scaler + (selectedSlot[1] * Main.MULTIPLIER * scaler)};
                batch.draw(selectedSlotTexture, rect.x + selectedSlot[0] * (slotSize * Main.MULTIPLIER * scaler) + inventoryBorder + selectedSlotOffset[0],
                        rect.y + selectedSlot[1] * (slotSize * Main.MULTIPLIER * scaler) + inventoryBorder + selectedSlotOffset[1], slotSize * Main.MULTIPLIER * scaler, slotSize * Main.MULTIPLIER * scaler);
            }

            // draw item amount
            for (int i = 0; i < size[1]; i++) {
                for (int j = 0; j < size[0]; j++) {
                    String amount = String.valueOf(inventory[i][j][1]);
                    layout.setText(Main.font, amount);
                    float fontHeight = layout.height;
                    float fontWidth = layout.width;
                    Main.font.draw(batch, amount, clickableRect.x + (j * itemSize) + slotSize * Main.MULTIPLIER * scaler - fontWidth - itemSize * 0.125f, clickableRect.y + (i * itemSize) + (fontHeight) + itemSize * 0.125f);
                }
            }
        }
    }
}
