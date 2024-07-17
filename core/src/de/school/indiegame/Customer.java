package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static de.school.indiegame.Inventory.*;
import static de.school.indiegame.Main.shape;

public class Customer {
    private static Sprite sprite;
    public static Texture cusInvTexture;
    Texture selectedSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));
    public static boolean cusInvVisible;
    private float xPos;

    GlyphLayout layout = new GlyphLayout();

    public static int[] size = new int[] {9, 2};
    public static int[][][] inventory = {{{-1, 0}, {5, 1}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}}, {{-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}}}; // in the left column store the item id, in the right column store the item amount

    float inventoryBorder = 4 * Main.MULTIPLIER * scaler;
    static Rectangle rect;
    Rectangle clickableRect;
    Rectangle mouseRect = new Rectangle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1, 1);
    public boolean mouseAboveHud = false;
    float slotSize = 20;
    int offset = 2;
    int maxAmount = 64;
    int itemSize = (int) ((slotSize + offset) * Main.MULTIPLIER * scaler);

    float buttonSize = (slotSize * Main.MULTIPLIER * scaler);

    // dragged item
    float draggedItemX;
    float draggedItemY;

    // dragged amount
    float draggedAmountX;
    float draggedAmountY;

    // accept button
    Rectangle acceptRect;

    // cancel button
    Rectangle cancelRect;


    Customer() {
        cusInvTexture = new Texture("customer/inventory.png");
        sprite = new Sprite(cusInvTexture);
        xPos = Inventory.rect.x-Inventory.backgroundTexture.getWidth()*Main.MULTIPLIER-20;
        sprite.setBounds(xPos, Inventory.rect.y, Inventory.rect.width, Inventory.rect.height);

        // Customer inventory
        rect = new Rectangle(sprite.getBoundingRectangle());

        clickableRect = new Rectangle(rect.x + inventoryBorder, (rect.y + rect.height) - inventoryBorder / 2 - (itemSize * 2), rect.width - inventoryBorder * 2, rect.height - itemSize - inventoryBorder * 2);

        cancelRect = new Rectangle(rect.x + inventoryBorder, rect.y + inventoryBorder, buttonSize, buttonSize);
        acceptRect = new Rectangle(rect.x + (itemSize * (size[0] - 1)) + inventoryBorder, rect.y + inventoryBorder, buttonSize, buttonSize);
    }

    public void handleInput() {
        // Check if mouse is hovering over inventory
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        mouseRect.setPosition(mouseX, mouseY);

       /* if (rect.overlaps(mouseRect) && cusInvVisible) {
            Main.mouseAboveHud = true;
            mouseAboveHud = true;
        } else {
            Main.mouseAboveHud = false;
            mouseAboveHud = false;
        }*/

        if (!Main.mouseAboveHud){
            return;
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (acceptRect.overlaps(mouseRect)) {
                // Accept buy
            }
            if (cancelRect.overlaps(mouseRect)) {
                changeVisibility();
            }
        }
    }

    public static void changeVisibility() {
        if (Customer.cusInvVisible) {
            Inventory.isVisible = false;
            Customer.cusInvVisible = false;
        } else {
            Inventory.isVisible = true;
            Customer.cusInvVisible = true;
        }

    }

    public void update() {
        handleInput();
    }

    public void draw(SpriteBatch batch) {
        if (cusInvVisible) {
            sprite.draw(batch);
            //shape.rect(cancelRect.x, cancelRect.y, cancelRect.width, cancelRect.height);
            //shape.rect(acceptRect.x, acceptRect.y, acceptRect.width, acceptRect.height);
            //shape.rect(clickableRect.x, clickableRect.y, clickableRect.width, clickableRect.height);

            // draw selected Slot
            if (selectedSlot[0] != -1 && selectedSlot[1] != -1 && draggedInventory == inventory) {
                float[] selectedSlotOffset = {scaler * selectedSlot[0] * Main.MULTIPLIER + (selectedSlot[0] * Main.MULTIPLIER * scaler), selectedSlot[1] * Main.MULTIPLIER * scaler + (selectedSlot[1] * Main.MULTIPLIER * scaler)};

                batch.draw(selectedSlotTexture, clickableRect.x + selectedSlot[0] * (slotSize * Main.MULTIPLIER * scaler) + selectedSlotOffset[0],
                        clickableRect.y + selectedSlot[1] * (slotSize * Main.MULTIPLIER * scaler) + selectedSlotOffset[1],
                        slotSize * Main.MULTIPLIER * scaler,
                        slotSize * Main.MULTIPLIER * scaler);
            }

            // draw items
            for (int i = 0; i < size[1]; i++) {
                for (int j = 0; j < size[0]; j++) {
                    boolean isDragged = false;
                    //System.out.println(draggedSlot[0] + " "+ draggedSlot[1]);

                    if (draggedSlot[0] == j && draggedSlot[1] == i && Main.inventory.draggedInventory == inventory) {
                        isDragged = true;
                    }

                    // items
                    int itemId = inventory[i][j][0];
                    if (itemId != -1) {
                        float itemX = clickableRect.x + (j * itemSize) + inventoryBorder / 2;
                        float itemY = clickableRect.y + (i * itemSize) + inventoryBorder / 2;

                        if (isDragged) {
                            itemX = mouseRect.x - (16 * Main.MULTIPLIER * scaler / 2);
                            itemY = mouseRect.y - (16 * Main.MULTIPLIER * scaler / 2);
                        }

                        if (!isDragged) {
                            batch.draw(itemTextures.get(itemId), itemX, itemY, 16 * Main.MULTIPLIER * scaler, 16 * Main.MULTIPLIER * scaler);
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
            if (draggedItemTexture != null && draggedInventory == inventory) {
                batch.draw(draggedItemTexture, draggedItemX, draggedItemY, 16 * Main.MULTIPLIER * scaler, 16 * Main.MULTIPLIER * scaler);
            }

            // draw dragged amount, so its always on top
            if (draggedAmountString != null && draggedInventory == inventory) {
                Main.font.draw(batch, draggedAmountString, draggedAmountX, draggedAmountY);
            }
        }
    }
}
