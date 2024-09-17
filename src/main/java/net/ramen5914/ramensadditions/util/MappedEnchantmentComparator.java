package net.ramen5914.ramensadditions.util;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Comparator;
import java.util.Map;

public class MappedEnchantmentComparator implements Comparator<Map.Entry<Holder<Enchantment>, Integer>> {
    @Override
    public int compare(Map.Entry<Holder<Enchantment>, Integer> o1, Map.Entry<Holder<Enchantment>, Integer> o2) {
        return o1.getKey().getRegisteredName().compareTo(o2.getKey().getRegisteredName());
    }
}
