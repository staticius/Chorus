package org.chorus.entity.data.profession

import cn.nukkit.block.BlockID
import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.TradeRecipeBuildUtils
import java.util.*

class ProfessionArmor :
    Profession(8, BlockID.BLAST_FURNACE, "entity.villager.armor", Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE) {
    override fun buildTrades(seed: Int): ListTag<CompoundTag> {
        val recipes: ListTag<CompoundTag> = ListTag()
        val random: Random = Random(seed.toLong())

        val enchantments: IntArray = intArrayOf(
            Enchantment.ID_DURABILITY,
            Enchantment.ID_THORNS,
            Enchantment.ID_PROTECTION_ALL,
            Enchantment.ID_PROTECTION_EXPLOSION,
            Enchantment.ID_PROTECTION_PROJECTILE,
            Enchantment.ID_PROTECTION_FIRE,
            Enchantment.ID_VANISHING_CURSE
        )
        val diamondLeggings: Item = Item.get(Item.DIAMOND_LEGGINGS)
        val diamondLeggingsEnchantment: Enchantment =
            Enchantment.getEnchantment(enchantments.get(random.nextInt(enchantments.size)))
        diamondLeggingsEnchantment.setLevel(1 + random.nextInt(diamondLeggingsEnchantment.getMaxLevel()))
        diamondLeggings.addEnchantment(diamondLeggingsEnchantment)

        val diamondChestplate: Item = Item.get(Item.DIAMOND_CHESTPLATE)
        val diamondChestplateEnchantment: Enchantment =
            Enchantment.getEnchantment(enchantments.get(random.nextInt(enchantments.size)))
        diamondChestplateEnchantment.setLevel(1 + random.nextInt(diamondChestplateEnchantment.getMaxLevel()))
        diamondChestplate.addEnchantment(diamondChestplateEnchantment)

        val diamondHelmet: Item = Item.get(Item.DIAMOND_HELMET)
        val diamondHelmetEnchantment: Enchantment =
            Enchantment.getEnchantment(enchantments.get(random.nextInt(enchantments.size)))
        diamondHelmetEnchantment.setLevel(1 + random.nextInt(diamondHelmetEnchantment.getMaxLevel()))
        diamondHelmet.addEnchantment(diamondHelmetEnchantment)

        val diamondBoots: Item = Item.get(Item.DIAMOND_BOOTS)
        val diamondBootsEnchantment: Enchantment =
            Enchantment.getEnchantment(enchantments.get(random.nextInt(enchantments.size)))
        diamondBootsEnchantment.setLevel(1 + random.nextInt(diamondBootsEnchantment.getMaxLevel()))
        diamondBoots.addEnchantment(diamondBootsEnchantment)

        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.COAL, 0, 15), Item.get(Item.EMERALD, 0, 1))
                .setMaxUses(16)
                .setRewardExp(1.toByte())
                .setTier(1)
                .setTraderExp(2)
                .setPriceMultiplierA(0.05f)
                .build()
        )
        when (random.nextInt(4)) {
            0 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 4), Item.get(Item.IRON_BOOTS, 0, 1))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            1 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 7), Item.get(Item.IRON_LEGGINGS, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            2 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 5), Item.get(Item.IRON_HELMET, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            3 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 9), Item.get(Item.IRON_CHESTPLATE, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.IRON_INGOT, 0, 4), Item.get(Item.EMERALD, 0, 1))
                .setMaxUses(12)
                .setRewardExp(1.toByte())
                .setTier(2)
                .setTraderExp(10)
                .setPriceMultiplierA(0.05f)
                .build()
        )
        when (random.nextInt(3)) {
            0 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 36), Item.get(BlockID.BELL, 0, 1))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(2)
                    .setTraderExp(5)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            1 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 3), Item.get(Item.CHAINMAIL_LEGGINGS, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(2)
                    .setTraderExp(5)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            2 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 1), Item.get(Item.CHAINMAIL_BOOTS, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(2)
                    .setTraderExp(5)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.LAVA_BUCKET), Item.get(Item.EMERALD, 0, 1))
                .setMaxUses(12)
                .setRewardExp(1.toByte())
                .setTier(3)
                .setTraderExp(20)
                .setPriceMultiplierA(0.05f)
                .build()
        )
            .add(
                TradeRecipeBuildUtils.of(Item.get(Item.DIAMOND, 0, 1), Item.get(Item.EMERALD, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(3)
                    .setTraderExp(30)
                    .setPriceMultiplierA(0.05f)
                    .build()
            )
        when (random.nextInt(3)) {
            0 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 4), Item.get(Item.CHAINMAIL_CHESTPLATE, 0, 1))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(3)
                    .setTraderExp(10)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            1 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 1), Item.get(Item.CHAINMAIL_HELMET, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(3)
                    .setTraderExp(10)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )

            2 -> recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 5), Item.get(Item.SHIELD, 0, 1))
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(3)
                    .setTraderExp(10)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        if (random.nextBoolean()) {
            recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 19 + random.nextInt(34 - 19)), diamondLeggings)
                    .setMaxUses(3)
                    .setRewardExp(1.toByte())
                    .setTier(4)
                    .setTraderExp(15)
                    .build()
            )
        } else {
            recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 13 + random.nextInt(28 - 13)), diamondBoots)
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(4)
                    .setTraderExp(15)
                    .build()
            )
        }
        if (random.nextBoolean()) {
            recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 21 + random.nextInt(36 - 21)), diamondChestplate)
                    .setMaxUses(3)
                    .setRewardExp(1.toByte())
                    .setTier(5)
                    .setTraderExp(30)
                    .build()
            )
        } else {
            recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 13 + random.nextInt(28 - 13)), diamondHelmet)
                    .setMaxUses(99)
                    .setRewardExp(1.toByte())
                    .setTier(5)
                    .setTraderExp(30)
                    .build()
            )
        }
        return recipes
    }
}
