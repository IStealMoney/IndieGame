package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Inventory {
    Json json = new Json();
    Gson gson = new Gson();
    String inventoryPath = "inventory/inventory.json";
    GlyphLayout layout = new GlyphLayout();
    public static HashMap<Integer, Texture> itemTextures = new HashMap<Integer, Texture>();
    ArrayList<HashMap<String, Object>> itemData = new ArrayList<HashMap<String, Object>>();

    public static int[] size = new int[] {9, 3};
    public static int[][][] inventory = new int[size[1]][size[0]][2]; // in the left column store the item id, in the right column store the item amount
    public static int[] selectedSlot = {-1, -1};
    public static int[] draggedSlot = {-1, -1};

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

    // dragged item
    float draggedItemX;
    float draggedItemY;
    Texture draggedItemTexture;

    // dragged amount
    float draggedAmountX;
    float draggedAmountY;
    String draggedAmount;

    // active item
    public static int[] activeItem = new int[2];
    float activeItemX = Toolbar.xPosition + inventoryBorder + 4.5f * Main.MULTIPLIER;
    float activeItemY = Toolbar.yPosition + inventoryBorder + 4.5f * Main.MULTIPLIER + itemSize * 5.35f;
    Rectangle activeItemRect = new Rectangle(activeItemX, activeItemY, 16 * Main.MULTIPLIER, 16 * Main.MULTIPLIER);

    public static boolean isVisible;

    Inventory(float x, float y) {
        rect = new Rectangle(x, y, width, height);
        clickableRect = new Rectangle(x +  inventoryBorder, y + inventoryBorder, width - (inventoryBorder * 2), height - (inventoryBorder * 2)); // Rectangle without texture borders
        loadItemTextures();
        loadInventory();
        pickup(0, 20);
        pickup(2, 30);
        pickup(3, 40);
        pickup(1, 129);
        pickup(4, 90);
        pickup(5, 70);

        add(new int[] {2, 2}, 2, 20);
    }

    public void loadItemTextures() {
        ArrayList<LinkedTreeMap> plantsArray = new ArrayList<LinkedTreeMap>();
        plantsArray = gson.fromJson(Gdx.files.internal("data/plants.json").reader(), plantsArray.getClass());

        for (int i = 0; i < plantsArray.size(); i++) {
            itemData.add(gson.fromJson(String.valueOf(plantsArray.get(i)), new HashMap<String, Object>().getClass()));

            itemTextures.put(i, new Texture(Gdx.files.internal("items/" + itemData.get(i).get("item_texture").toString() + ".png")));
        }
    }

    public void loadInventory() {
        inventory = json.fromJson(inventory.getClass(), Gdx.files.internal(inventoryPath));

        activeItem = gson.fromJson(Gdx.files.internal("inventory/active_item.json").reader(), activeItem.getClass());
    }

    public void saveInventory() {
        String inventoryString = json.toJson(inventory);
        String activeItemString = json.toJson(activeItem);

        try {
            FileWriter fileWriter = new FileWriter(Gdx.files.internal(inventoryPath).toString());
            fileWriter.write(inventoryString);
            fileWriter.close();

            FileWriter itemFileWriter = new FileWriter(Gdx.files.internal("inventory/active_item.json").toString());
            itemFileWriter.write(activeItemString);
            itemFileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void distributeAmount(int id, int[] startSlot, int amount) {
        for (int i = startSlot[1]; i < size[1]; i++) {
            for (int j = startSlot[0]; j < size[0]; j++) {
                int invAmount = inventory[i][j][1];
                int difAmount = maxAmount;

                if (amount < maxAmount) {
                    difAmount = amount;
                }

                if (invAmount >= 0 && invAmount < maxAmount && amount > 0 && inventory[i][j][0] == -1) {
                    inventory[i][j][1] += difAmount;
                    inventory[i][j][0] = id;
                    amount -= difAmount;
                }
            }
        }
    }

    public void pickup(int id, int amount) {
        for (int i = 0; i < size[1]; i++) {
            for (int j = 0; j < size[0]; j++) {
                int invId = inventory[i][j][0];
                int invAmount = inventory[i][j][1];
                int difAmount = maxAmount - invAmount;

                if (amount < maxAmount) {
                    difAmount = amount;
                }


                if (invId == id && invAmount < maxAmount && amount > 0) {
                    inventory[i][j][1] += difAmount;
                    amount -= difAmount;
                }
            }
        }

        distributeAmount(id, new int[] {0, 0}, amount);
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

    public void clearSlots() {
        selectedSlot[0] = -1;
        selectedSlot[1] = -1;
        draggedSlot[0] = -1;
        draggedSlot[1] = -1;
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        draggedItemTexture = null;
        draggedAmount = null;
    }

    public void handleInput() {
        // Check if mouse is hovering over inventory
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        mouseRect.setPosition(mouseX, mouseY);

        if (rect.contains(mouseRect) && isVisible) {
            Main.mouseAboveHud = true;
        } else {
            Main.mouseAboveHud = false;
        }

        // Handle visibility
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            isVisible = !isVisible;
            clearSlots();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            boolean exists = true;

            // coordinate system begins in lower left corner
            float dx = mouseX - clickableRect.x;
            float dy = mouseY - clickableRect.y;

            if (activeItem[0] == -1 && activeItem[1] == 0) {
                exists = false;
            }

            if (clickableRect.contains(mouseRect)) {
                // check if selected item is put into active item slot
                if (!exists) {
                    activeItem[0] = inventory[(int) dy / itemSize][(int) dx/itemSize][0];
                    activeItem[1] = inventory[(int) dy / itemSize][(int) dx/itemSize][1];
                    inventory[(int) dy / itemSize][(int) dx/itemSize][0] = -1;
                    inventory[(int) dy / itemSize][(int) dx/itemSize][1] = 0;
                    Tool.weaponType = 0;
                    Main.tool.refreshTexture();
                    return;
                }
                if (exists) {
                    int[] tempItem = new int[] {activeItem[0], activeItem[1]};

                    activeItem[0] = inventory[(int) dy / itemSize][(int)dx / itemSize][0];
                    activeItem[1] = inventory[(int) dy / itemSize][(int)dx / itemSize][1];

                    inventory[(int) dy / itemSize][(int)dx / itemSize][0] = tempItem[0];
                    inventory[(int) dy / itemSize][(int)dx / itemSize][1] = tempItem[1];

                    Tool.weaponType = 0;
                    Main.tool.refreshTexture();
                    return;
                }
            }

        }

        // Handle click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            boolean isDragged = false;

            if (draggedSlot[0] >= 0 && draggedSlot[1] >= 0) {
                isDragged = true;
            }

            if (activeItemRect.contains(mouseRect) && (activeItem[0] != -1 && activeItem[1] != 0)) {
                // check if active item is put back into inventory
                pickup(activeItem[0], activeItem[1]);
                // reset active item
                activeItem[0] = -1;
                activeItem[1] = 0;

                Tool.weaponType = 2;
                Main.tool.refreshTexture();
            }

            if (clickableRect.contains(mouseRect)) {
                // coordinate system begins in lower left corner
                float dx = mouseX - clickableRect.x;
                float dy = mouseY - clickableRect.y;

                // check if item is dragged on same slot
                if (draggedSlot[0] == (int) dx / itemSize && draggedSlot[1] == (int) dy / itemSize) {
                    clearSlots();
                    return;
                }

                if (isDragged) {
                    // check if item is dragged but released onto other item and slot is not empty
                    if (inventory[(int) dy / itemSize][(int) dx / itemSize][0] != inventory[draggedSlot[1]][draggedSlot[0]][0] && inventory[(int) dy / itemSize][(int) dx / itemSize][0] != -1) {
                        clearSlots();
                        return;
                    }

                    // check if item is dropped onto same item
                    if (inventory[(int) dy / itemSize][(int) dx / itemSize][0] == inventory[draggedSlot[1]][draggedSlot[0]][0]) {
                        int sameItemAmount = inventory[(int) dy / itemSize][(int) dx / itemSize][1];
                        int itemAmount = inventory[draggedSlot[1]][draggedSlot[0]][1];
                        if (sameItemAmount < maxAmount) {
                            int difAmount = maxAmount - sameItemAmount;
                            if (difAmount > itemAmount) {
                                difAmount = itemAmount;
                                inventory[draggedSlot[1]][draggedSlot[0]][0] = -1;
                            }
                            System.out.println(difAmount);

                            inventory[(int) dy / itemSize][(int) dx / itemSize][1] += difAmount;
                            inventory[draggedSlot[1]][draggedSlot[0]][1] -= difAmount;
                        }

                        clearSlots();
                        return;
                    }
                }

                if (isDragged) {
                    int[] newSlot = new int[] {(int) (dx / itemSize), (int) (dy / itemSize)};
                    add(newSlot, inventory[selectedSlot[1]][selectedSlot[0]][0], inventory[selectedSlot[1]][selectedSlot[0]][1]);
                    remove(selectedSlot, inventory[selectedSlot[1]][selectedSlot[0]][1]);
                    clearSlots();
                    return;
                }
                if (inventory[(int) dy / itemSize][(int) dx / itemSize][1] > 0) { // if slot is not empty
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
                    selectedSlot[0] = (int) (dx / itemSize);
                    selectedSlot[1] = (int) (dy / itemSize);
                    draggedSlot[0] = (int) (dx / itemSize);
                    draggedSlot[1] = (int) (dy / itemSize);
                }
            }

        }
    }

    public void update() {
        handleInput();
    }

    public void draw(SpriteBatch batch) {
        Main.font.getData().setScale(1);

        // draw active item
        if (activeItem[0] != -1) {
            batch.draw(itemTextures.get(activeItem[0]), activeItemX, activeItemY, 16*Main.MULTIPLIER * 0.8f, 16*Main.MULTIPLIER * 0.8f);

            // draw active item amount
            String activeItemAmount = String.valueOf(activeItem[1]);
            layout.setText(Main.font, activeItemAmount);
            Main.font.draw(batch, String.valueOf(activeItem[1]), activeItemX + 16 * Main.MULTIPLIER * 0.8f - layout.width * 0.9f, activeItemY + inventoryBorder / 2);
        }

        if (isVisible) {
            // draw background
            batch.draw(backgroundTexture, rect.x, rect.y, rect.width, rect.height);

            // draw selectedSlot
            if (selectedSlot[0] != -1 && selectedSlot[1] != -1) {
                float[] selectedSlotOffset = {scaler * selectedSlot[0] * Main.MULTIPLIER + (selectedSlot[0] * Main.MULTIPLIER * scaler), selectedSlot[1] * Main.MULTIPLIER * scaler + (selectedSlot[1] * Main.MULTIPLIER * scaler)};

                batch.draw(selectedSlotTexture, rect.x + selectedSlot[0] * (slotSize * Main.MULTIPLIER * scaler) + inventoryBorder + selectedSlotOffset[0],
                        rect.y + selectedSlot[1] * (slotSize * Main.MULTIPLIER * scaler) + inventoryBorder + selectedSlotOffset[1],
                        slotSize * Main.MULTIPLIER * scaler,
                        slotSize * Main.MULTIPLIER * scaler);
            }

            // draw item amount and items
            for (int i = 0; i < size[1]; i++) {
                for (int j = 0; j < size[0]; j++) {
                    boolean isDragged = false;

                    if (draggedSlot[0] == j && draggedSlot[1] == i) {
                        isDragged = true;
                    }
                        // items
                    int itemId =  inventory[i][j][0];
                    if (itemId != -1) {
                        float itemX = clickableRect.x + (j * itemSize) + inventoryBorder / 2;
                        float itemY = clickableRect.y + (i * itemSize) + inventoryBorder / 2;

                        if (isDragged) {
                            itemX = mouseRect.x - (16 * Main.MULTIPLIER * scaler / 2);
                            itemY = mouseRect.y - (16 * Main.MULTIPLIER * scaler / 2);
                        }

                        if (!isDragged) {
                            batch.draw(itemTextures.get(itemId), itemX, itemY,16 * Main.MULTIPLIER * scaler, 16 * Main.MULTIPLIER * scaler);
                        } else {
                            draggedItemTexture = itemTextures.get(itemId);
                            draggedItemX = itemX;
                            draggedItemY = itemY;
                        }
                    }

                    // item amount
                    String amount = String.valueOf(inventory[i][j][1]);
                    layout.setText(Main.font, amount);
                    float fontHeight = layout.height;
                    float fontWidth = layout.width;
                    float amountX = clickableRect.x + (j * itemSize) + slotSize * Main.MULTIPLIER * scaler - fontWidth - itemSize * 0.125f;
                    float amountY = clickableRect.y + (i * itemSize) + (fontHeight) + itemSize * 0.125f;

                    if (isDragged) {
                        amountX = mouseRect.x + slotSize * Main.MULTIPLIER * scaler - fontWidth - Main.MULTIPLIER * scaler * 16 * 0.8f;
                        amountY = mouseRect.y - fontHeight;
                    }

                    if (!isDragged) {
                        Main.font.draw(batch, amount, amountX, amountY);
                    } else {
                        draggedAmountX = amountX;
                        draggedAmountY = amountY;
                        draggedAmount = amount;
                    }
                }
            }
            // draw dragged item, so its always on top
            if (draggedItemTexture != null) {
                batch.draw(draggedItemTexture, draggedItemX, draggedItemY, 16 * Main.MULTIPLIER * scaler, 16 * Main.MULTIPLIER * scaler);
            }

            // draw dragged amount, so its always on top
            if (draggedAmount != null) {
                Main.font.draw(batch, draggedAmount, draggedAmountX, draggedAmountY);
            }
        }
    }
}
