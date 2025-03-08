package cn.nukkit.entity.data.profession

import cn.nukkit.block.BlockID
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.DyeColor
import cn.nukkit.utils.TradeRecipeBuildUtils
import java.util.*

class ProfessionLeather : Profession(12, BlockID.CAULDRON, "entity.villager.leather", Sound.BUCKET_FILL_WATER) {
    override fun buildTrades(seed: Int): ListTag<CompoundTag> {
        val recipes: ListTag<CompoundTag> = ListTag()
        val random: Random = Random(seed.toLong())

        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.LEATHER, 0, 6), Item.get(Item.EMERALD))
                .setMaxUses(16)
                .setRewardExp(1.toByte())
                .setTier(1)
                .setTraderExp(2)
                .setPriceMultiplierA(0.05f)
                .build()
        )
        if (random.nextBoolean()) {
            recipes.add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 7),
                    (Item.get(Item.LEATHER_CHESTPLATE) as ItemLeatherChestplate).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        } else {
            recipes.add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 3),
                    (Item.get(Item.LEATHER_LEGGINGS) as ItemLeatherLeggings).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(1)
                    .setTraderExp(1)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.FLINT, 0, 26), Item.get(Item.EMERALD))
                .setMaxUses(12)
                .setRewardExp(1.toByte())
                .setTier(2)
                .setTraderExp(10)
                .setPriceMultiplierA(0.05f)
                .build()
        )
        if (random.nextBoolean()) {
            recipes.add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 5),
                    (Item.get(Item.LEATHER_HELMET) as ItemLeatherHelmet).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(2)
                    .setTraderExp(5)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        } else {
            recipes.add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 4),
                    (Item.get(Item.LEATHER_BOOTS) as ItemLeatherBoots).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(2)
                    .setTraderExp(5)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        recipes.add(
            TradeRecipeBuildUtils.of(Item.get(Item.RABBIT_HIDE, 0, 9), Item.get(Item.EMERALD))
                .setMaxUses(12)
                .setRewardExp(1.toByte())
                .setTier(3)
                .setTraderExp(20)
                .setPriceMultiplierA(0.05f)
                .build()
        )
            .add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 7),
                    (Item.get(Item.LEATHER_CHESTPLATE) as ItemLeatherChestplate).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(3)
                    .setTraderExp(10)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
            .add(
                TradeRecipeBuildUtils.of(Item.get(Item.TURTLE_HELMET, 0, 4), Item.get(Item.EMERALD))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(4)
                    .setTraderExp(30)
                    .setPriceMultiplierA(0.05f)
                    .build()
            )
            .add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 6), Item.get(Item.LEATHER_HORSE_ARMOR))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(4)
                    .setTraderExp(15)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        if (random.nextBoolean()) {
            recipes.add(
                TradeRecipeBuildUtils.of(Item.get(Item.EMERALD, 0, 6), Item.get(Item.SADDLE))
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(5)
                    .setTraderExp(30)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        } else {
            recipes.add(
                TradeRecipeBuildUtils.of(
                    Item.get(Item.EMERALD, 0, 5),
                    (Item.get(Item.LEATHER_HELMET) as ItemLeatherHelmet).setColor(
                        DyeColor.entries.get(
                            random.nextInt(DyeColor.entries.size)
                        )
                    )
                )
                    .setMaxUses(12)
                    .setRewardExp(1.toByte())
                    .setTier(5)
                    .setTraderExp(30)
                    .setPriceMultiplierA(0.2f)
                    .build()
            )
        }
        return recipes
    }
}
