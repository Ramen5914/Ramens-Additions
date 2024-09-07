package net.ramen5914.advancedgrindstone.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.ramen5914.advancedgrindstone.AdvancedGrindstone;
import net.ramen5914.advancedgrindstone.gui.menu.ModMenuTypes;
import net.ramen5914.advancedgrindstone.gui.screen.AdvancedGrindstoneScreen;

@EventBusSubscriber(modid = AdvancedGrindstone.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ADVANCED_GRINDSTONE.get(), AdvancedGrindstoneScreen::new);
    }
}
