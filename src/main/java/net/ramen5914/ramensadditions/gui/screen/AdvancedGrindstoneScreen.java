package net.ramen5914.ramensadditions.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.ramen5914.ramensadditions.RamensAdditions;
import net.ramen5914.ramensadditions.gui.menu.custom.AdvancedGrindstoneMenu;
import net.ramen5914.ramensadditions.gui.widget.Label;
import net.ramen5914.ramensadditions.gui.widget.SelectionScrollInput;
import net.ramen5914.ramensadditions.util.CustomFunctions;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class AdvancedGrindstoneScreen extends AbstractContainerScreen<AdvancedGrindstoneMenu> implements MenuAccess<AdvancedGrindstoneMenu> {
    private static final ResourceLocation ERROR_SPRITE = ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "container/advanced_grindstone/error");
    public static final ResourceLocation ADVANCED_GRINDSTONE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(RamensAdditions.MOD_ID, "textures/gui/container/advanced_grindstone.png");

    private int startIndex = 0;

    SelectionScrollInput enchantmentSelector;
    Label enchantmentLabel;

    public AdvancedGrindstoneScreen(AdvancedGrindstoneMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageHeight = 189;

        this.inventoryLabelY = 95;
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        initGatherOptions();
    }

    @Override
    protected void init() {
        super.init();

        RamensAdditions.LOGGER.info("INIT HERE");

        initGatherOptions();

        addRenderableWidget(enchantmentSelector);
        addRenderableWidget(enchantmentLabel);
    }

    private void initGatherOptions() {
        removeWidget(enchantmentSelector);
        removeWidget(enchantmentLabel);

        enchantmentSelector = null;

        List<Pair<Component, Boolean>> options;
        if (this.menu.getSlot(0).getItem() != ItemStack.EMPTY) {
            ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(menu.getSlot(0).getItem());

            options = enchantments.entrySet().stream().map(holderEntry -> {
                String enchantName = holderEntry.getKey().value().description().getString();
                Integer enchantLevel = holderEntry.getValue();

                Boolean isCurse = holderEntry.getKey().is(EnchantmentTags.CURSE);

                if (enchantLevel > 1) {
                    return new Pair<>((Component) Component.literal(String.format("%s %s", enchantName, CustomFunctions.intToRomanNumeral(enchantLevel))), isCurse);
                } else {
                    return new Pair<>((Component) Component.literal(enchantName), isCurse);
                }
            }).collect(Collectors.toList());

            options.addFirst(new Pair<>(Component.literal("All"), false));
        } else {
            options = List.of(new Pair<>(Component.literal(""), false));
        }

        enchantmentLabel = new Label(getGuiLeft() + 16, getGuiTop() + 81, Component.empty()).withShadow();
        enchantmentLabel.text = Component.literal("");

        enchantmentSelector = new SelectionScrollInput(getGuiLeft() + 11, getGuiTop() + 77, 154, 18)
                .forOptions(options)
                .writingTo(enchantmentLabel)
                .calling(i -> {
                    startIndex = i;
                    enchantmentLabel.text = options.get(i).getA();
                })
                .withRange(0, options.size())
                .setState(startIndex);

        if (menu.getSlot(0).getItem() == ItemStack.EMPTY) {
            enchantmentSelector.visible = false;
        } else {
            enchantmentSelector.visible = true;
        }

        enchantmentSelector.onChanged();
        addRenderableWidget(enchantmentSelector);

        addRenderableWidget(enchantmentLabel);
//        enchantmentSelector = new SelectionScrollInput(getGuiLeft() + 11, getGuiTop() + 76, 154, 18)
//                .writingTo(enchantmentLabel)
//                .addHint(Component.literal("Hint"))
//                .titled(Component.literal("Title"))
//                .setState(enchantToRemove)
//                .calling(this::initGatherOptions);
//        enchantmentLabel = new Label(getGuiLeft() + 15, getGuiTop() + 80, Component.empty()).withShadow();
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

        guiGraphics.blit(ADVANCED_GRINDSTONE_BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            guiGraphics.blitSprite(ERROR_SPRITE, i + 92, j + 31, 28, 21);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void removeWidget(GuiEventListener listener) {
        if (listener != null) {
            super.removeWidget(listener);
        }
    }
}
