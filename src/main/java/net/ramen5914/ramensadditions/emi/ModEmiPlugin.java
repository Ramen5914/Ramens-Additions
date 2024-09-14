package net.ramen5914.ramensadditions.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;

import java.util.List;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(
                new EmiInfoRecipe(
                        List.of(EmiIngredient.of(Ingredient.of(ModBlocks.ADVANCED_GRINDSTONE.get()))),
                        List.of(Component.literal("An upgrade of the vanilla grindstone which has the power to remove curses! (at a cost)")),
                        ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "/info_recipes/advanced_grindstone")
                )
        );
    }
}
