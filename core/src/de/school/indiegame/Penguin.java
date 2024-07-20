package de.school.indiegame;

public class Penguin {

    Penguin() {

    }

    public static void decide() {
        if (Tool.weaponType == 0) {
            Main.moneySystem.add(20);
        } else {
            Main.moneySystem.substract(20);

        }
        // wenn weapon, dann Abzug
        //wenn active item ist essen, dann geld, aber active item wird 1 abgezogen
        // schwelle, ab der man wieder alles verliert, was man gesammelt hat
    }
}
