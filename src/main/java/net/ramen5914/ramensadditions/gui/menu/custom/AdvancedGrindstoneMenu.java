package net.ramen5914.ramensadditions.gui.menu.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.ramen5914.ramensadditions.ModTags;
import net.ramen5914.ramensadditions.block.ModBlocks;
import net.ramen5914.ramensadditions.gui.menu.ModMenuTypes;
import net.ramen5914.ramensadditions.util.EnchantmentComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedGrindstoneMenu extends AbstractContainerMenu {
    public static final int MAX_NAME_LENGTH = 35;
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private int state = 0;
    private final Container resultSlots = new ResultContainer();

    final Container inputSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            AdvancedGrindstoneMenu.this.slotsChanged(this);
        }
    };

    private final ContainerLevelAccess access;

    public AdvancedGrindstoneMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public AdvancedGrindstoneMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(ModMenuTypes.ADVANCED_GRINDSTONE.get(), containerId);

        this.access = access;

        this.addSlot(new Slot(this.inputSlots, INPUT_SLOT, 34, 30) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EnchantmentHelper.hasAnyEnchantments(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(this.inputSlots, ADDITIONAL_SLOT, 95, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModTags.Items.DISENCHANTING_CATALYSTS);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(this.resultSlots, RESULT_SLOT, 144, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                access.execute((level, blockPos) -> level.levelEvent(1042, blockPos, 0));

                AdvancedGrindstoneMenu.this.inputSlots.setItem(0, ItemStack.EMPTY);
                AdvancedGrindstoneMenu.this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 107 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 165));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        if (container == this.inputSlots) {
            this.createResult();
        }
    }

    private void createResult() {
        this.resultSlots.setItem(0, this.computeResult(this.inputSlots.getItem(0), this.inputSlots.getItem(1)));
        this.broadcastChanges();
    }

    private ItemStack computeResult(ItemStack inputItem, ItemStack catalyst) {
        if (inputItem.isEmpty() || catalyst.isEmpty() || inputItem.getCount() > 1) {
            return ItemStack.EMPTY;
        } else {
            if (this.state == 0) {
                return this.removeAllEnchantsFrom(inputItem.copy());
            } else {
                return this.removeEnchantFrom(inputItem.copy());
            }
        }
    }

    private ItemStack removeAllEnchantsFrom(ItemStack item) {
        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(
                item, enchantments -> enchantments.removeIf(enchantmentHolder -> true)
        );

        if (item.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) {
            item = item.transmuteCopy(Items.BOOK);
        }

        return item;
    }

    private ItemStack removeEnchantFrom(ItemStack item) {
        List<Holder<Enchantment>> options = new ArrayList<>(EnchantmentHelper.getEnchantmentsForCrafting(item).entrySet().stream().map(Map.Entry::getKey).toList());
        options.sort(new EnchantmentComparator());

        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(
                item, enchantments -> enchantments.removeIf(enchantmentHolder -> enchantmentHolder.equals(options.get(this.state - 1)))
        );
        
        if (item.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) {
            item = item.transmuteCopy(Items.BOOK);
        }

        int i = 0;

        for (int j = 0; j < itemEnchantments.size(); j++) {
            i = AnvilMenu.calculateIncreasedRepairCost(i);
        }

        item.set(DataComponents.REPAIR_COST, i);
        return item;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.inputSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.ADVANCED_GRINDSTONE.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int state) {
        if (state != this.state) {
            this.state = state;
            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack clickedStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack clickedStack = slot.getItem();
            clickedStackCopy = clickedStack.copy();

            ItemStack inputStack = this.inputSlots.getItem(0);
            ItemStack catalystStack = this.inputSlots.getItem(1);

            if (index == 2) {
                if (!this.moveItemStackTo(clickedStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(clickedStack, clickedStackCopy);
            } else if (index != 0 && index != 1) {
                if (!inputStack.isEmpty() && !catalystStack.isEmpty()) {
                    if (index >= 3 && index < 30) {
                        if (!this.moveItemStackTo(clickedStack, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.moveItemStackTo(clickedStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(clickedStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(clickedStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (clickedStack.getCount() == clickedStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, clickedStack);
        }
        return clickedStackCopy;
    }
}
