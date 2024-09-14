package net.ramen5914.ramensadditions.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.emi.ModEmiPlugin;
import net.ramen5914.ramensadditions.gui.screen.AdvancedGrindstoneScreen;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedGrindingRecipe implements EmiRecipe {
    private List<EmiIngredient> inputs = List.of(EmiIngredient.of(Ingredient.of(Items.NETHERITE_SWORD)), EmiIngredient.of(Ingredient.of(Items.NETHERITE_INGOT)));
    private List<EmiStack> outputs = List.of(EmiStack.of(Items.NETHERITE_SWORD));

    public AdvancedGrindingRecipe() {

    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ModEmiPlugin.ADVANCED_GRINDING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "/advanced_grindstone/test");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 154;
    }

    @Override
    public int getDisplayHeight() {
        return 79;
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AdvancedGrindstoneScreen.ADVANCED_GRINDSTONE_BACKGROUND, 0, 0, getDisplayWidth(), getDisplayHeight(), 11, 15);

        widgets.addSlot(inputs.getFirst(), 22, 14);
        widgets.addSlot(inputs.get(1), 83, 18);
        widgets.addSlot(outputs.getFirst(), 132, 18).recipeContext(this);
    }
}
