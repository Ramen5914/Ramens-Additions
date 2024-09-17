package net.ramen5914.ramensadditions.util;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Comparator;

public class EnchantmentComparator implements Comparator<Holder<Enchantment>> {
    @Override
    public int compare(Holder<Enchantment> o1, Holder<Enchantment> o2) {
        return o1.getRegisteredName().compareTo(o2.getRegisteredName());
    }
}
