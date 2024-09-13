package net.ramen5914.ramensadditions.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;

public class RamensAdditionsEMIPlugin implements EmiPlugin {
    public static final EmiRecipeCategory ADVANCED_GRINDSTONE = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "advanced_grindstone"), EmiStack.of(ModBlocks.ADVANCED_GRINDSTONE));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ADVANCED_GRINDSTONE);

        registry.addWorkstation(ADVANCED_GRINDSTONE, EmiStack.of(ModBlocks.ADVANCED_GRINDSTONE));
    }
}
