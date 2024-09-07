package net.ramen5914.advancedgrindstone.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ramen5914.advancedgrindstone.AdvancedGrindstone;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AdvancedGrindstone.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
