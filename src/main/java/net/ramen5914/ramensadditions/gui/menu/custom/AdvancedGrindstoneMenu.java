package net.ramen5914.ramensadditions.gui.menu.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.ramen5914.ramensadditions.ModTags;
import net.ramen5914.ramensadditions.block.ModBlocks;
import net.ramen5914.ramensadditions.gui.menu.ModMenuTypes;

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
    private List<Holder<Enchantment>> enchantOptions = new ArrayList<>();

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
            return this.removeNonCursesFrom(inputItem.copy());
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
        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(
                item, enchantments -> enchantments.removeIf(enchantmentHolder -> enchantmentHolder.equals(enchantOptions.get(this.state)))
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
    public ItemStack quickMoveStack(Player player, int index) {

        // New Empty itemStack
        ItemStack clickedStackCopy = ItemStack.EMPTY;

        // Index index of the slot you shift clicked
        // Slot is the slot at that index
        Slot slot = this.slots.get(index);

        // If the slot is valid and has an item then:
        if (slot != null && slot.hasItem()) {
            // ItemStack1 becomes the slots held item
            ItemStack clickedStack = slot.getItem();

            // ItemStack also becomes the slots held item, but as a copy
            clickedStackCopy = clickedStack.copy();

            // ItemStack2 is inputSlot 0
            ItemStack inputStack = this.inputSlots.getItem(0);
            // ItemStack3 is inputSlot 1
            ItemStack catalystStack = this.inputSlots.getItem(1);

            // If you shift click the output slot
            if (index == 2) {
                // Moves the output stack to the player inventory, starting at the rightmost hotbar slot
                // If the move is unsuccessful, returns an empty stack.
                if (!this.moveItemStackTo(clickedStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                // I think this might be unnecessary but whatever
                slot.onQuickCraft(clickedStack, clickedStackCopy);
            // If the shift clicked slot is not either of the input slots
            } else if (index != 0 && index != 1) {
                // if both the inputStack and the catalyst stack are not empty
                if (!inputStack.isEmpty() && !catalystStack.isEmpty()) {
                    // if the slot clicked is one of the inventory slots (not hotbar)
                    if (index >= 3 && index < 30) {
                        // Moves the clicked stack to the hotbar starting from the left
                        // if the move is unsuccessful, returns an empty stack
                        if (!this.moveItemStackTo(clickedStack, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    // if the slot clicked is in the hotbar and moving the clicked stack to the inventory fails,
                    } else if (index >= 30 && index < 39 && !this.moveItemStackTo(clickedStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                // if either the inputStack or the catalystStack is empty (or both),
                // and if moving the clickedStack to the inputSlot or catalystSlot fails
                // returns and empty stack
                } else if (!this.moveItemStackTo(clickedStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            // if the shift clicked slot is anything else, moves the clicked stack to any inventory slot
            // if the move fails, returns an empty stack;
            } else if (!this.moveItemStackTo(clickedStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            // if the clicked stack is empty, this doesnt really do anything.
            if (clickedStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            // if its not empty, run setChanged() function.
            } else {
                slot.setChanged();
            }

            // if the clickedStack count equals the copy's count, return an empty stack
            if (clickedStack.getCount() == clickedStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            // runs the onTake method on the slot, which is just setChanged in this case.
            slot.onTake(player, clickedStack);
        }

        return clickedStackCopy;
    }

    public void setEnchantOptions(List<Holder<Enchantment>> enchantOptions) {
        this.enchantOptions = enchantOptions;
    }

    public void setState(int state) {
        this.state = state;
        this.inputSlots.setChanged();
    }
}
