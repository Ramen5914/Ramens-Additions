package net.ramen5914.advancedgrindstone.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.ramen5914.advancedgrindstone.AdvancedGrindstone;
import net.ramen5914.advancedgrindstone.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Blocks.GRINDSTONE),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.MISC,
                ModBlocks.ADVANCED_GRINDSTONE.asItem())
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, String.format("%s:advanced_grindstone_smithing", AdvancedGrindstone.MOD_ID));
    }
}
