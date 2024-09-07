package net.ramen5914.ramensadditions.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.gui.menu.ModMenuTypes;
import net.ramen5914.ramensadditions.gui.screen.AdvancedGrindstoneScreen;

@EventBusSubscriber(modid = RamensAdditions.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ADVANCED_GRINDSTONE.get(), AdvancedGrindstoneScreen::new);
    }
}
