package net.ramen5914.ramensadditions.emi.recipes;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.ramen5914.ramensadditions.ModTags;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.emi.ModEmiPlugin;
import net.ramen5914.ramensadditions.gui.screen.AdvancedGrindstoneScreen;
import net.ramen5914.ramensadditions.util.CustomFunctions;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AdvancedGrindingRecipe implements EmiRecipe {
    private final int uniq = new Random().nextInt();
    private final Item tool;
    private final ResourceLocation id;

    public AdvancedGrindingRecipe(Item tool, ResourceLocation id) {
        this.tool = tool;
        this.id = id;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ModEmiPlugin.ADVANCED_GRINDING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiStack.of(tool), EmiIngredient.of(ModTags.Items.DISENCHANTING_CATALYSTS));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(tool));
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
        widgets.addTexture(AdvancedGrindstoneScreen.ADVANCED_GRINDSTONE_BACKGROUND, 0, 0, 154, 79, 11, 15);

        widgets.addGeneratedSlot(r -> getTool(r, true, widgets), uniq, 22, 14).drawBack(false);
        widgets.addSlot(EmiIngredient.of(ModTags.Items.DISENCHANTING_CATALYSTS), 83, 18).drawBack(false);
        widgets.addGeneratedSlot(r -> getTool(r, false, widgets), uniq, 132, 18).drawBack(false).recipeContext(this);

        widgets.addText(Component.literal("All"), 5, 66, 0xffffff, true);
    }

    private EmiStack getTool(Random random, Boolean enchant, WidgetHolder widgets) {
        ItemStack itemStack = new ItemStack(tool);
        int enchantments = 1 + Math.max(random.nextInt(5), random.nextInt(3));

        List<Enchantment> list = Lists.newArrayList();

        final Registry<Enchantment> ENCHANTMENT_REGISTRY = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        outer:
        for (int i = 0; i < enchantments; i++) {
            Enchantment enchantment = getEnchantment(random);

            int maxLvl = enchantment.getMaxLevel();
            int minLvl = enchantment.getMinLevel();

            int lvl = maxLvl > 0 ? random.nextInt(maxLvl) + 1 : 0;

            if (lvl < minLvl) {
                lvl = minLvl;
            }

            for (Enchantment e : list) {
                if (e == enchantment || !Enchantment.areCompatible(ENCHANTMENT_REGISTRY.wrapAsHolder(e), ENCHANTMENT_REGISTRY.wrapAsHolder(enchantment))) {
                    continue outer;
                }
            }
            list.add(enchantment);

            if (enchant) {
                itemStack.enchant(ENCHANTMENT_REGISTRY.wrapAsHolder(enchantment), lvl);
            }
        }

        return EmiStack.of(itemStack);
    }

    private Enchantment getEnchantment(Random random) {
        final Registry<Enchantment> ENCHANTMENT_REGISTRY = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        List<Enchantment> enchantments = ENCHANTMENT_REGISTRY.stream().filter(i -> tool.getDefaultInstance().supportsEnchantment(Holder.direct(i))).toList();
        int enchantment = random.nextInt(enchantments.size());
        return enchantments.get(enchantment);
    }
}
