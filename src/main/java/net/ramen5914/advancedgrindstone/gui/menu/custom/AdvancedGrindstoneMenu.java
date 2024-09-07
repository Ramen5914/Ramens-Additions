package net.ramen5914.advancedgrindstone.gui.menu.custom;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
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
import net.ramen5914.advancedgrindstone.block.ModBlocks;
import net.ramen5914.advancedgrindstone.gui.menu.ModMenuTypes;

public class AdvancedGrindstoneMenu extends AbstractContainerMenu {
    public static final int MAX_NAME_LENGTH = 35;
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final Container resultSlots = new ResultContainer();
    final Container repairSlots = new SimpleContainer(2) {
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

        this.addSlot(new Slot(this.repairSlots, 0, 49, 19) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EnchantmentHelper.hasAnyEnchantments(stack);
            }
        });

        this.addSlot(new Slot(this.repairSlots, 1, 49, 40) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EnchantmentHelper.hasAnyEnchantments(stack);
            }
        });

        this.addSlot(new Slot(this.resultSlots, 2, 129, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        if (container == this.repairSlots) {
            this.createResult();
        }
    }

    private void createResult() {
        this.resultSlots.setItem(0, this.computeResult(this.repairSlots.getItem(0), this.repairSlots.getItem(1)));
        this.broadcastChanges();
    }

    private ItemStack computeResult(ItemStack inputItem, ItemStack additionalItem) {
        boolean flag = !inputItem.isEmpty() || !additionalItem.isEmpty();
        if (!flag) {
            return ItemStack.EMPTY;
        } else if (inputItem.getCount() <= 1 && additionalItem.getCount() <= 1) {
            boolean flag1 = !inputItem.isEmpty() && !additionalItem.isEmpty();
            if (!flag1) {
                ItemStack itemStack = !inputItem.isEmpty() ? inputItem : additionalItem;
                return !EnchantmentHelper.hasAnyEnchantments(itemStack) ? ItemStack.EMPTY : this.removeNonCursesFrom(itemStack.copy());
            } else {
                return this.mergeItems(inputItem, additionalItem);
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    private ItemStack mergeItems(ItemStack inputItem, ItemStack additionalItem) {
        if (!inputItem.is(additionalItem.getItem())) {
            return ItemStack.EMPTY;
        } else {
            int i = Math.max(inputItem.getMaxDamage(), additionalItem.getMaxDamage());
            int j = inputItem.getMaxDamage() - inputItem.getDamageValue();
            int k = additionalItem.getMaxDamage() - additionalItem.getDamageValue();
            int l = j + k + i * 5 / 100;
            int i1 = 1;
            if (!inputItem.isDamageableItem() || !inputItem.isRepairable()) {
                if (inputItem.getMaxStackSize() < 2 || !ItemStack.matches(inputItem, additionalItem)) {
                    return ItemStack.EMPTY;
                }

                i1 = 2;
            }

            ItemStack itemStack = inputItem.copyWithCount(i1);
            if (itemStack.isDamageableItem()) {
                itemStack.set(DataComponents.MAX_DAMAGE, i);
                itemStack.setDamageValue(Math.max(i - l, 0));
                if (!additionalItem.isRepairable()) itemStack.setDamageValue(inputItem.getDamageValue());
            }

            this.mergeEnchantsFrom(itemStack, additionalItem);
            return this.removeNonCursesFrom(itemStack);
        }
    }

    private void mergeEnchantsFrom(ItemStack inputItem, ItemStack additionalItem) {
        EnchantmentHelper.updateEnchantments(inputItem, mutable -> {
            ItemEnchantments itemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(additionalItem);

            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (!holder.is(EnchantmentTags.CURSE) || mutable.getLevel(holder) == 0) {
                    mutable.upgrade(holder, entry.getIntValue());
                }
            }
        });
    }

    private ItemStack removeNonCursesFrom(ItemStack item) {
        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(
                item, mutable -> mutable.removeIf(enchantmentHolder -> !enchantmentHolder.is(EnchantmentTags.CURSE))
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
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.repairSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.ADVANCED_GRINDSTONE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.repairSlots.getItem(0);
            ItemStack itemstack3 = this.repairSlots.getItem(1);
            if (index == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 0 && index != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (index >= 3 && index < 30) {
                        if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
