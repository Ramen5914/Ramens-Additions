package net.ramen5914.ramensadditions.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomFunctionsTest {

    @Test
    void intToRomanNumeral() {
        assertEquals("I", CustomFunctions.intToRomanNumeral(1));
        assertEquals("II", CustomFunctions.intToRomanNumeral(2));
        assertEquals("III", CustomFunctions.intToRomanNumeral(3));
        assertEquals("IV", CustomFunctions.intToRomanNumeral(4));
        assertEquals("V", CustomFunctions.intToRomanNumeral(5));
        assertEquals("VI", CustomFunctions.intToRomanNumeral(6));
        assertEquals("VII", CustomFunctions.intToRomanNumeral(7));
        assertEquals("VIII", CustomFunctions.intToRomanNumeral(8));
        assertEquals("IX", CustomFunctions.intToRomanNumeral(9));
        assertEquals("X", CustomFunctions.intToRomanNumeral(10));
        assertEquals("XX", CustomFunctions.intToRomanNumeral(20));
        assertEquals("XXX", CustomFunctions.intToRomanNumeral(30));
        assertEquals("XL", CustomFunctions.intToRomanNumeral(40));
        assertEquals("L", CustomFunctions.intToRomanNumeral(50));
        assertEquals("LX", CustomFunctions.intToRomanNumeral(60));
        assertEquals("LXX", CustomFunctions.intToRomanNumeral(70));
        assertEquals("LXXX", CustomFunctions.intToRomanNumeral(80));
        assertEquals("XC", CustomFunctions.intToRomanNumeral(90));
        assertEquals("C", CustomFunctions.intToRomanNumeral(100));
        assertEquals("CC", CustomFunctions.intToRomanNumeral(200));
        assertEquals("CCC", CustomFunctions.intToRomanNumeral(300));
        assertEquals("CD", CustomFunctions.intToRomanNumeral(400));
        assertEquals("D", CustomFunctions.intToRomanNumeral(500));
        assertEquals("DC", CustomFunctions.intToRomanNumeral(600));
        assertEquals("DCC", CustomFunctions.intToRomanNumeral(700));
        assertEquals("DCCC", CustomFunctions.intToRomanNumeral(800));
        assertEquals("CM", CustomFunctions.intToRomanNumeral(900));
        assertEquals("M", CustomFunctions.intToRomanNumeral(1000));
    }
}