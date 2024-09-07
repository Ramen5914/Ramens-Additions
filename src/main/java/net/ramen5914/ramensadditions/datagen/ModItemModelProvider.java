package net.ramen5914.ramensadditions.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RamensAdditions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        complexBlock(ModBlocks.ADVANCED_GRINDSTONE.get());
    }

    private ItemModelBuilder complexBlock(Block block) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID,
                "block/" + BuiltInRegistries.BLOCK.getKey(block).getPath()));
    }
}

