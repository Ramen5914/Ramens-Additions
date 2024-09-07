package net.ramen5914.ramensadditions.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ramen5914.ramensadditions.RamensAdditions;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RamensAdditions.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
