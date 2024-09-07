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

        add("container.advancedgrindstone.advanced_grindstone_title", "Advanced Grindstone");
    }
}
