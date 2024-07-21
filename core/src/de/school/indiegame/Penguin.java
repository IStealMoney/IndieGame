package de.school.indiegame;

import java.util.Random;

public class Penguin {
    public static Random rand = new Random();
    public static int possibleClicks;
    public static int totalCoins = 0;

    Penguin() {

    }

    public static void decide() {
        if (Tool.weaponType == 0 && Tile.penguinClicks == 1) {  // first click
            possibleClicks = rand.nextInt(5) + 1; // rand number 1-6
            System.out.println(possibleClicks);
        } else if (Tool.weaponType != 0 && Tile.penguinClicks == 1 && MoneySystem.currentMoney >= 19) {   // not active item selected
            Main.moneySystem.substract(20);
            Tile.penguinClicks = 0;
        }

        if (Tool.weaponType == 0 && Tile.penguinClicks != possibleClicks) {
            Main.moneySystem.add(20);
            totalCoins += 20;
        } else if (Tool.weaponType == 0 && Tile.penguinClicks == possibleClicks){   //if greedy
            Main.moneySystem.substract(totalCoins);
            totalCoins = 0;
            Tile.penguinClicks = 0;
        }
    }
}
