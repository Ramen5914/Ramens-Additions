package net.ramen5914.ramensadditions.util;

import java.util.concurrent.atomic.AtomicReference;

public class CustomFunctions {
    public static String intToRomanNumeral(int number) {
        int[] values =      { 1000, 900,  500, 400,  100, 90,   50,  40,   10,  9,    5,   4,    1 };
        String[] numerals = { "M",  "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

        AtomicReference<String> romanNumeral = new AtomicReference<>("");

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                romanNumeral.set(romanNumeral + numerals[i]);
                number -= values[i];
            }
        }

        return romanNumeral.get();
    }
}
