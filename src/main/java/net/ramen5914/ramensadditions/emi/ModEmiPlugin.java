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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;
import net.ramen5914.ramensadditions.emi.recipes.AdvancedGrindingRecipe;

import java.util.List;

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

        registry.addRecipe(new AdvancedGrindingRecipe());

//        for (Item i : BuiltInRegistries.ITEM) {
//            try {
//                if (i.components().getOrDefault(DataComponents.MAX_DAMAGE, 0) > 0) {
//                    RamensAdditions.LOGGER.info(i.toString());
//                    String format = String.format("/%s/%s/%s", "advanced_grindstone/disenchanting", BuiltInRegistries.ITEM.getKey(i).getNamespace(), BuiltInRegistries.ITEM.getKey(i).getPath());
//                    RamensAdditions.LOGGER.info(format);
//                    registry.addRecipe(new AdvancedGrindingRecipe(i, ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, format)));
//                }
//            } catch (Throwable t) {
//                RamensAdditions.LOGGER.error("Exception thrown registering repair recipes");
//                t.printStackTrace();
//            }
//        }
    }
}
