package net.ramen5914.ramensadditions.gui.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.gui.menu.custom.AdvancedGrindstoneMenu;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, RamensAdditions.MOD_ID);

    public static final Supplier<MenuType<AdvancedGrindstoneMenu>> ADVANCED_GRINDSTONE =
            MENU_TYPES.register("advanced_grindstone_mt", () -> new MenuType<>(AdvancedGrindstoneMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
