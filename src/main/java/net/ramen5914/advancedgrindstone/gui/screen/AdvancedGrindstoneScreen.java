package net.ramen5914.advancedgrindstone.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.ramen5914.advancedgrindstone.AdvancedGrindstone;
import net.ramen5914.advancedgrindstone.gui.menu.custom.AdvancedGrindstoneMenu;

@OnlyIn(Dist.CLIENT)
public class AdvancedGrindstoneScreen extends AbstractContainerScreen<AdvancedGrindstoneMenu> implements MenuAccess<AdvancedGrindstoneMenu> {
    private static final ResourceLocation ERROR_SPRITE = ResourceLocation.fromNamespaceAndPath(AdvancedGrindstone.MOD_ID, "container/advanced_grindstone/error");
    private static final ResourceLocation ADVANCED_GRINDSTONE_LOCATION = ResourceLocation.fromNamespaceAndPath(AdvancedGrindstone.MOD_ID, "textures/gui/container/advanced_grindstone.png");

    public AdvancedGrindstoneScreen(AdvancedGrindstoneMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(ADVANCED_GRINDSTONE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            guiGraphics.blitSprite(ERROR_SPRITE, i + 92, j + 31, 28, 21);
        }
    }
}
