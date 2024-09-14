package net.ramen5914.ramensadditions.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.block.ModBlocks;

public class ModEnglishLangProvider extends LanguageProvider {
    public ModEnglishLangProvider(PackOutput output) {
        super(output, RamensAdditions.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.ADVANCED_GRINDSTONE, "Advanced Grindstone");

        add(String.format("container.%s.advanced_grindstone_title", RamensAdditions.MOD_ID), "Advanced Grindstone");
        add(String.format("itemgroup.%s.advanced_grindstone_tab", RamensAdditions.MOD_ID), "Ramen's Additions");
        add(String.format("tag.item.%s.disenchanting_catalysts", RamensAdditions.MOD_ID), "Disenchanting Catalysts");
        add(String.format("emi.category.%s.advanced_grinding", RamensAdditions.MOD_ID), "Advanced Grinding");
    }
}
