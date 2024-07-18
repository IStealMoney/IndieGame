package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static de.school.indiegame.Inventory.*;
import static de.school.indiegame.Main.moneySystem;
import static de.school.indiegame.Main.shape;

public class Customer {
    private static Sprite sprite;
    public static Texture cusInvTexture;
    Texture selectedSlotTexture = new Texture(Gdx.files.internal("inventory/selected_slot.png"));
    public static boolean cusInvVisible;

    public static int[] size = new int[] {9, 2};
    public static int[][][] inventory = {{{-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}},
                                        {{-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}}}; // in the left column store the item id, in the right column store the item amount

    GlyphLayout layout = new GlyphLayout();
    int totalValue;
    String totalValueText;

    float inventoryBorder = 4 * Main.MULTIPLIER * scaler;
    static Rectangle rect;
    Rectangle clickableRect;
    Rectangle mouseRect = new Rectangle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1, 1);
    float slotSize = 20;
    int offset = 2;
    int itemSize = (int) ((slotSize + offset) * Main.MULTIPLIER * scaler);

    float buttonSize = (slotSize * Main.MULTIPLIER * scaler);
    // accept button
    Rectangle acceptRect;
    // cancel button
    Rectangle cancelRect;

    public static boolean justCancelled = false;


    Customer() {
        cusInvTexture = new Texture("customer/inventory.png");
        sprite = new Sprite(cusInvTexture);
        float xPos = Inventory.rect.x - Inventory.backgroundTexture.getWidth() * Main.MULTIPLIER - 20;
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

        if (!Main.mouseAboveHud){
            return;
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (acceptRect.overlaps(mouseRect)) {
                // Accept buy
                for (int[][] item : inventory) {
                    for (int j = 0; j < item.length; j++) {
                        item[j][0] = -1;
                        item[j][1] = 0;
                    }
                }
                moneySystem.add(totalValue);
                totalValue = 0;
            }
            if (cancelRect.overlaps(mouseRect)) {
                changeVisibility();
                justCancelled = true;
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

    public void calculateValue() {
        totalValue = 0;

        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                if (inventory[i][j][0] != -1) {
                    totalValue += inventory[i][j][1] * (int) (double) Main.inventory.itemData.get(inventory[i][j][0]).get("value");
                }
            }
        }

        if (Main.inventory.draggedType != -1 && draggedInventory == inventory) { // if dragged item exists and is in customer inventory
            totalValue -= Main.inventory.draggedAmount * (int) (double) Main.inventory.itemData.get(Main.inventory.draggedType).get("value");
        }

        totalValueText = String.valueOf(totalValue);
    }

    public void update() {
        justCancelled = false;
        handleInput();
        calculateValue();

    }

    public void draw(SpriteBatch batch) {
        if (cusInvVisible) {
            sprite.draw(batch);

            // draw total value
            layout.setText(Main.costumerFont, totalValueText);
            float textX = rect.x + rect.width / 2 - layout.width / 2;
            float textY = rect.y + inventoryBorder + inventoryBorder / 1.85f + layout.height;
            float coinX = textX + MoneySystem.coinTexture.getWidth() * Main.MULTIPLIER / 5f;
            float coinY = textY - (float) MoneySystem.coinTexture.getHeight() * Main.MULTIPLIER / 1.6f;
            batch.draw(MoneySystem.coinTexture, coinX + layout.width, coinY, MoneySystem.coinTexture.getWidth() * Main.MULTIPLIER * 0.75f, MoneySystem.coinTexture.getHeight() * Main.MULTIPLIER * 0.75f);
            Main.costumerFont.draw(batch, totalValueText, textX, textY);

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

                    if (draggedSlot[0] == j && draggedSlot[1] == i && draggedInventory == inventory) {
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
                            Main.inventory.draggedItemX = itemX;
                            Main.inventory.draggedItemY = itemY;
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
                        Main.inventory.draggedAmountX = amountX;
                        Main.inventory.draggedAmountY = amountY;
                        draggedAmountString = amount;
                    }
                }
            }
        }
    }
}
