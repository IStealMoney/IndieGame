package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static de.school.indiegame.Main.shape;

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

    static Texture backgroundTexture = new Texture(Gdx.files.internal("inventory/background.png"));
    Texture selectedSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));

    public static float scaler = 0.75f;
    static float width = backgroundTexture.getWidth() * Main.MULTIPLIER * scaler;
    static float height = backgroundTexture.getHeight() * Main.MULTIPLIER * scaler;
    static Rectangle rect;
    Rectangle clickableRect;
    Rectangle mouseRect = new Rectangle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1, 1);
    float inventoryBorder = 4 * Main.MULTIPLIER * scaler;
    float slotSize = 20;
    int offset = 2;
    int maxAmount = 64;
    int itemSize = (int) ((slotSize + offset) * Main.MULTIPLIER * scaler);

    // dragged item
    public float draggedItemX;
    public float draggedItemY;
    public static Texture draggedItemTexture;

    // dragged type
    int draggedType;

    // dragged amount
    public float draggedAmountX;
    public float draggedAmountY;
    public static String draggedAmountString;

    // dragged item inventory
    public static int[][][] draggedInventory;
    int draggedAmount;

    // active item
    public static int[] activeItem = new int[2];
    public static Sprite activeItemSprite;
    float activeItemX = Toolbar.xPosition + inventoryBorder + 4.5f * Main.MULTIPLIER;
    float activeItemY = Toolbar.yPosition + inventoryBorder + 4.5f * Main.MULTIPLIER + itemSize * 5.35f;
    Rectangle activeItemRect = new Rectangle(activeItemX, activeItemY, 16 * Main.MULTIPLIER, 16 * Main.MULTIPLIER);

    public static boolean isVisible;

    Inventory(float x, float y) {
        rect = new Rectangle(x, y, width, height);
        clickableRect = new Rectangle(x +  inventoryBorder, y + inventoryBorder, width - (inventoryBorder * 2), height - (inventoryBorder * 2)); // Rectangle without texture borders
        loadItemTextures();
        loadInventory();
        activeItemSprite = new Sprite(itemTextures.get(0));
    }

    public void loadItemTextures() {
        ArrayList<LinkedTreeMap> plantsArray = new ArrayList<LinkedTreeMap>();
        plantsArray = gson.fromJson(Gdx.files.internal("plants/plants.json").reader(), plantsArray.getClass());

        for (int i = 0; i < plantsArray.size(); i++) {
            itemData.add(gson.fromJson(String.valueOf(plantsArray.get(i)), new HashMap<String, Object>().getClass()));

            itemTextures.put(i, new Texture(Gdx.files.internal("items/" + itemData.get(i).get("name").toString() + ".png")));
        }
    }

    public void loadInventory() {
        inventory = json.fromJson(inventory.getClass(), Gdx.files.internal(inventoryPath));
        activeItem = json.fromJson(activeItem.getClass(), Gdx.files.internal("inventory/active_item.json"));
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

    public void distributeAmount(int[][][] inventory, int id, int[] startSlot, int amount) {
        for (int i = startSlot[1]; i < size[1]; i++) {
            for (int j = startSlot[0]; j < size[0]; j++) {
                int invAmount = inventory[i][j][1];
                int difAmount = 0;

                if (amount < maxAmount && Math.abs(invAmount - maxAmount) >= amount) {
                    difAmount = amount;
                }

                if (amount > (maxAmount - invAmount)) {
                    difAmount = maxAmount - invAmount;
                }
                if (amount == maxAmount) {
                    difAmount = maxAmount;
                }

                if (invAmount >= 0 && amount > 0 && inventory[i][j][0] == -1) {
                    inventory[i][j][1] += difAmount;
                    inventory[i][j][0] = id;
                    amount -= difAmount;
                }
            }
        }
    }

    public void pickup(int[][][] inventory, int id, int amount) {
        for (int i = 0; i < size[1]; i++) {
            for (int j = 0; j < size[0]; j++) {
                int invId = inventory[i][j][0];
                int invAmount = inventory[i][j][1];
                int difAmount = 0;
                if (invId == id) {
                    if (amount < maxAmount && Math.abs(invAmount - maxAmount) >= amount) {
                        difAmount = amount;
                    }

                    if (amount > (maxAmount - invAmount)) {
                        difAmount = maxAmount - invAmount;
                    }

                    if (invAmount < maxAmount && amount > 0) {
                        inventory[i][j][1] += difAmount;
                        amount -= difAmount;
                    }
                }
            }
        }
        if (amount > 0) {
            distributeAmount(inventory, id, new int[] {0, 0}, amount);
        }
    }

    public void add(int[][][] inventory, int[] slot, int id, int amount) {
        if (inventory[slot[1]][slot[0]][0] == id || inventory[slot[1]][slot[0]][0] == -1) { // if slot has same item id or is empty
            inventory[slot[1]][slot[0]][0] = id;
            inventory[slot[1]][slot[0]][1] += amount;
        }
    }

    public void remove(int[][][] inventory, int[] slot, int amount) {
        inventory[slot[1]][slot[0]][1] -= amount;

        if (inventory[slot[1]][slot[0]][1] <= 0) { // if amount is 0, delete slot
            inventory[slot[1]][slot[0]][0] = -1; // set item id to 0
            inventory[slot[1]][slot[0]][1] = 0; // set amount to 0
        }
    }

    public void resetActiveItem() {
        activeItem[0] = -1;
        activeItem[1] = -1;
        Main.tool.refreshTexture();
    }

    public void clearSlots() {
        selectedSlot[0] = -1;
        selectedSlot[1] = -1;
        draggedSlot[0] = -1;
        draggedSlot[1] = -1;
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        draggedItemTexture = null;
        draggedAmountString = null;
        draggedAmount = -1;
        draggedType = -1;
    }

    public void handleInput() {
        // Check if mouse is hovering over inventory
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        mouseRect.setPosition(mouseX, mouseY);
        float dx = -1;
        float dy = -1;

        if (rect.overlaps(mouseRect) && isVisible || Customer.rect.overlaps(mouseRect) && Customer.cusInvVisible || Main.toolbar.mouseAboveHud) {
            Main.mouseAboveHud = true;
        } else {
            Main.mouseAboveHud = false;
        }

        int[][][] tempInventory = null;

        if (clickableRect.overlaps(mouseRect) && isVisible) {
            tempInventory = inventory;
            dx = mouseX - clickableRect.x;
            dy = mouseY - clickableRect.y;
        }

        if (Main.customer.clickableRect.overlaps(mouseRect) && Customer.cusInvVisible) {
            tempInventory = Customer.inventory;
            // coordinate system begins in lower left corner
            dx = mouseX - Main.customer.clickableRect.x;
            dy = mouseY - Main.customer.clickableRect.y;
        }

        // handle visibility
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) { // change to basket
            Tool.weaponType = 1;
            Toolbar.changeSelectToolbar();
            Main.tool.refreshTexture();
            isVisible = !isVisible;
            clearSlots();
        }

        // Clicking actions
        boolean isDragged = false;

        if (draggedSlot[0] >= 0 && draggedSlot[1] >= 0) {
            isDragged = true;
        }


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && isVisible) {
            boolean exists = true;

            if (activeItem[0] == -1 && activeItem[1] == 0) {
                exists = false;
            }

            if (tempInventory == inventory) {
                if (!isDragged) {
                    if (!exists) {
                        activeItem[0] = inventory[(int) dy / itemSize][(int) dx/itemSize][0];
                        activeItem[1] = inventory[(int) dy / itemSize][(int) dx/itemSize][1];
                        inventory[(int) dy / itemSize][(int) dx / itemSize][0] = -1;
                        inventory[(int) dy / itemSize][(int) dx / itemSize][1] = 0;
                    } else {
                        int[] tempItem = new int[] {activeItem[0], activeItem[1]};

                        activeItem[0] = inventory[(int) dy / itemSize][(int) dx / itemSize][0];
                        activeItem[1] = inventory[(int) dy / itemSize][(int) dx / itemSize][1];

                        inventory[(int) dy / itemSize][(int)dx / itemSize][0] = tempItem[0];
                        inventory[(int) dy / itemSize][(int)dx / itemSize][1] = tempItem[1];
                    }

                    Tool.weaponType = 0;
                    Main.tool.refreshTexture();
                    return;
                }
            }

        }

        // Handle click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (activeItemRect.contains(mouseRect) && (activeItem[0] != -1 && activeItem[1] != 0)) {
                // check if active item is put back into inventory
                pickup(inventory, activeItem[0], activeItem[1]);
                // reset active item
                activeItem[0] = -1;
                activeItem[1] = 0;

                Tool.weaponType = 1;
                Main.tool.refreshTexture();
                return;
            }

            if (tempInventory != null) {
                // check if item is dragged on same slot
                if (draggedSlot[0] == (int) dx / itemSize && draggedSlot[1] == (int) dy / itemSize && tempInventory == draggedInventory) {
                    clearSlots();
                    return;
                }

                if (isDragged) {
                    // check if item is dragged but released onto other item and slot is not empty
                    if (tempInventory[(int) dy / itemSize][(int) dx / itemSize][0] != draggedType && tempInventory[(int) dy / itemSize][(int) dx / itemSize][0] != -1) {
                        // swap items
                        int[] tempDragged = {draggedType, draggedAmount};
                        draggedInventory[selectedSlot[1]][selectedSlot[0]] = tempInventory[(int) dy / itemSize][(int) dx / itemSize];
                        tempInventory[(int) dy / itemSize][(int) dx / itemSize] = tempDragged;
                        clearSlots();
                        return;
                    }

                    // check if item is dropped onto same item
                    if (tempInventory[(int) dy / itemSize][(int) dx / itemSize][0] == draggedType) {
                        int sameItemAmount = tempInventory[(int) dy / itemSize][(int) dx / itemSize][1];
                        int itemAmount = draggedAmount;
                        if (sameItemAmount < maxAmount) {
                            int difAmount = maxAmount - sameItemAmount;
                            if (difAmount > itemAmount) {
                                difAmount = itemAmount;
                                draggedInventory[draggedSlot[1]][draggedSlot[0]][0] = -1;
                            }

                            tempInventory[(int) dy / itemSize][(int) dx / itemSize][1] += difAmount;
                            draggedInventory[draggedSlot[1]][draggedSlot[0]][1] -= difAmount;

                            if (tempInventory[draggedSlot[1]][draggedSlot[0]][1] <= 0) { // if all items have been transferred to item that has been clicked on
                                draggedInventory[draggedSlot[1]][draggedSlot[0]][0] = -1;
                                draggedInventory[draggedSlot[1]][draggedSlot[0]][1] = 0;
                            }
                        }

                        clearSlots();
                        return;
                    }
                }

                if (isDragged) { // if slot is empty
                    int[] newSlot = new int[] {(int) (dx / itemSize), (int) (dy / itemSize)};
                    add(tempInventory, newSlot, draggedType, draggedAmount);
                    remove(draggedInventory, draggedSlot, draggedInventory[draggedSlot[1]][draggedSlot[0]][1]);
                    clearSlots();
                    return;
                }

                if (tempInventory[(int) dy / itemSize][(int) dx / itemSize][1] > 0) { // if slot is not empty
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
                    selectedSlot[0] = (int) (dx / itemSize);
                    selectedSlot[1] = (int) (dy / itemSize);
                    draggedSlot[0] = (int) (dx / itemSize);
                    draggedSlot[1] = (int) (dy / itemSize);
                    draggedInventory = tempInventory;
                    draggedType = tempInventory[(int) dy / itemSize][(int) dx / itemSize][0];
                    draggedAmount = tempInventory[(int) dy / itemSize][(int) dx / itemSize][1];
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
            activeItemSprite.setTexture(itemTextures.get(activeItem[0]));
            activeItemSprite.setBounds(activeItemX, activeItemY, 16*Main.MULTIPLIER * 0.8f, 16*Main.MULTIPLIER * 0.8f);
            activeItemSprite.draw(batch);

            // draw active item amount
            String activeItemAmount = String.valueOf(activeItem[1]);
            layout.setText(Main.activeItemFont, activeItemAmount);
            Main.activeItemFont.draw(batch, String.valueOf(activeItem[1]), activeItemX + 16 * Main.MULTIPLIER * 0.8f - layout.width * 0.9f, activeItemY + inventoryBorder / 2);
        }

        if (isVisible) {
            // draw background
            batch.draw(backgroundTexture, rect.x, rect.y, rect.width, rect.height);

            // draw selected Slot
            if (selectedSlot[0] != -1 && selectedSlot[1] != -1 && draggedInventory == inventory) {
                float[] selectedSlotOffset = {scaler * selectedSlot[0] * Main.MULTIPLIER + (selectedSlot[0] * Main.MULTIPLIER * scaler), selectedSlot[1] * Main.MULTIPLIER * scaler + (selectedSlot[1] * Main.MULTIPLIER * scaler)};

                batch.draw(selectedSlotTexture, clickableRect.x + selectedSlot[0] * (slotSize * Main.MULTIPLIER * scaler) + selectedSlotOffset[0],
                        clickableRect.y + selectedSlot[1] * (slotSize * Main.MULTIPLIER * scaler) + selectedSlotOffset[1],
                        slotSize * Main.MULTIPLIER * scaler,
                        slotSize * Main.MULTIPLIER * scaler);
            }

            // draw item amount and items
            for (int i = 0; i < size[1]; i++) {
                for (int j = 0; j < size[0]; j++) {
                    boolean isDragged = false;

                    if (draggedSlot[0] == j && draggedSlot[1] == i && draggedInventory == inventory) {
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
                        if (inventory[i][j][1] > 0) {
                            Main.font.draw(batch, amount, amountX, amountY);
                        }
                    } else {
                        draggedAmountX = amountX;
                        draggedAmountY = amountY;
                        draggedAmountString = amount;
                    }
                }
            }
            // draw dragged item, so its always on top
            if (draggedItemTexture != null) {
                batch.draw(draggedItemTexture, draggedItemX, draggedItemY, 16 * Main.MULTIPLIER * scaler, 16 * Main.MULTIPLIER * scaler);
            }

            // draw dragged amount, so its always on top
            if (draggedAmountString != null) {
                Main.font.draw(batch, draggedAmountString, draggedAmountX, draggedAmountY);
            }
        }
    }
}
