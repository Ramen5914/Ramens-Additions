package net.ramen5914.ramensadditions;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ramen5914.ramensadditions.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RamensAdditions.MOD_ID);

    public static final Supplier<CreativeModeTab> ADVANCED_GRINDSTONE_TAB =
            CREATIVE_MODE_TABS.register("advanced_grindstone_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable(String.format("itemgroup.%s.advanced_grindstone_tab", RamensAdditions.MOD_ID)))
                    .icon(() -> new ItemStack(ModBlocks.ADVANCED_GRINDSTONE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.ADVANCED_GRINDSTONE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
