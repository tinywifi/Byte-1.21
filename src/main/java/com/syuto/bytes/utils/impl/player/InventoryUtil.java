package com.syuto.bytes.utils.impl.player;

import net.minecraft.block.Block;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;

import static com.syuto.bytes.Byte.mc;

public class InventoryUtil {

    public static int getBestHotbarSlotToBreakBlock(Block block) {
        int slot = mc.player.getInventory().selectedSlot;
        float bestSpeed = getBreakSpeed(
                mc.player.getInventory().getStack(slot),
                block
        );

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            float breakSpeed = getBreakSpeed(itemStack, block);
            if (breakSpeed > bestSpeed) {
                slot = i;
                bestSpeed = breakSpeed;
            }
        }

        return slot;
    }

    public static float getBreakSpeed(ItemStack item, Block block) {
        float efficiencyMulti = item.isSuitableFor(block.getDefaultState()) ? (float) (Math.pow(getEnchantLevel(item, Enchantments.EFFICIENCY), 2) + 1) : 0;
        return item.getMiningSpeedMultiplier(block.getDefaultState()) + efficiencyMulti;
    }

    public static HashMap<RegistryKey<Enchantment>, Integer> getEnchants(
            ItemStack item
    ) {
        HashMap<RegistryKey<Enchantment>, Integer> enchantments =
                new HashMap<>();

        ItemEnchantmentsComponent enchantmentsComponent =
                EnchantmentHelper.getEnchantments(item);

        enchantmentsComponent
                .getEnchantments()
                .forEach(enchant -> {
                    enchantments.put(
                            enchant.getKey().get(),
                            enchantmentsComponent.getLevel(enchant)
                    );
                });

        return enchantments;
    }

    public static int getEnchantLevel(
            ItemStack item,
            RegistryKey<Enchantment> enchantment
    ) {
        return getEnchants(item).getOrDefault(enchantment, 0);
    }
}
