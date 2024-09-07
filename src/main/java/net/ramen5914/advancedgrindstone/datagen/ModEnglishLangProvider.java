package net.ramen5914.advancedgrindstone.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.ramen5914.advancedgrindstone.AdvancedGrindstone;
import net.ramen5914.advancedgrindstone.block.ModBlocks;

public class ModEnglishLangProvider extends LanguageProvider {
    public ModEnglishLangProvider(PackOutput output) {
        super(output, AdvancedGrindstone.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.ADVANCED_GRINDSTONE, "Advanced Grindstone");

        add(String.format("container.%s.advanced_grindstone_title", AdvancedGrindstone.MOD_ID), "Advanced Grindstone");
        add(String.format("itemgroup.%s.advanced_grindstone_tab", AdvancedGrindstone.MOD_ID), "Ramen's Additions");
    }
}
