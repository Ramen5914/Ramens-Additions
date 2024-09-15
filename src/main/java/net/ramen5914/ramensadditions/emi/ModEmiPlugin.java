package net.ramen5914.ramensadditions.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;
import net.ramen5914.ramensadditions.emi.recipes.AdvancedGrindingRecipe;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Consumer;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    public static final ResourceLocation WIDGET_SPRITE_SHEET = ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "textures/gui/widgets.png");

    // Advanced Grindstone
    public static final EmiStack ADVANCED_GRINDSTONE = EmiStack.of(ModBlocks.ADVANCED_GRINDSTONE);
    public static final EmiRecipeCategory ADVANCED_GRINDING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "advanced_grinding"), ADVANCED_GRINDSTONE);

    @Override
    public void register(EmiRegistry registry) {
        List<Enchantment> targetedEnchantments = Lists.newArrayList();
        List<Enchantment> universalEnchantments = Lists.newArrayList();
        for (Enchantment enchantment : Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)) {
            try {
                if (enchantment.canEnchant(ItemStack.EMPTY)) {
                    universalEnchantments.add(enchantment);
                    continue;
                }
            } catch (Throwable ignored) {

            }
            targetedEnchantments.add(enchantment);
        }

        // Advanced Grindstone
        registry.addCategory(ADVANCED_GRINDING);
        registry.addWorkstation(ADVANCED_GRINDING, ADVANCED_GRINDSTONE);
        registry.addRecipe(
                new EmiInfoRecipe(
                        List.of(EmiIngredient.of(Ingredient.of(ModBlocks.ADVANCED_GRINDSTONE.get()))),
                        List.of(Component.literal(
                                "An upgrade of the vanilla grindstone which has the power to remove curses! (at a cost)"
                        )),
                        ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "/info_recipes/advanced_grindstone")
                )
        );

        for (Item i : BuiltInRegistries.ITEM) {
            try {
                ItemStack defaultStack = i.getDefaultInstance();
                int acceptableEnchantments = 0;
                for (Enchantment e : targetedEnchantments) {
                    if (defaultStack.supportsEnchantment(Holder.direct(e))) {
                        acceptableEnchantments++;
                    }
                }
                if (acceptableEnchantments > 0) {
                    for (Enchantment e : universalEnchantments) {
                        if (defaultStack.supportsEnchantment(Holder.direct(e))) {
                            acceptableEnchantments++;
                        }
                    }
                    String itemNamespace = BuiltInRegistries.ITEM.getKey(i).getNamespace();
                    String itemPath = BuiltInRegistries.ITEM.getKey(i).getPath();
                    String id = String.format("/advanced_grindstone/disenchanting/%s/%s", itemNamespace, itemPath);

                    registry.addRecipe(new AdvancedGrindingRecipe(i, ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, id)));
                }
            } catch (Throwable t) {
                RamensAdditions.LOGGER.warn("Exception thrown registering enchantment recipes");
                t.printStackTrace();
                StringWriter writer = new StringWriter();
                t.printStackTrace(new PrintWriter(writer, true));
                String[] strings = writer.getBuffer().toString().split("/");
                for (String s : strings) {
                    RamensAdditions.LOGGER.warn(s);
                }
            }
        }
    }
}
