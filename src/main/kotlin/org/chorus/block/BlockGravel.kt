package org.chorus.block

import org.chorus.item.*
import org.chorus.item.Item.Companion.get
import org.chorus.item.enchantment.Enchantment
import org.chorus.utils.random.ChorusRandom.nextInt


class BlockGravel : BlockFallable, Natural {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override val name: String
        get() = "Gravel"

    override fun getDrops(item: Item): Array<Item> {
        val enchantment = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
        var fortune = 0
        if (enchantment != null) {
            fortune = enchantment.level
        }

        val nukkitRandom: NukkitRandom = NukkitRandom()
        when (fortune) {
            0 -> {
                if (nukkitRandom.nextInt(0, 9) == 0) {
                    return arrayOf(get(ItemID.FLINT, 0, 1))
                }
            }

            1 -> {
                if (nukkitRandom.nextInt(0, 6) == 0) {
                    return arrayOf(get(ItemID.FLINT, 0, 1))
                }
            }

            2 -> {
                if (nukkitRandom.nextInt(0, 3) == 0) {
                    return arrayOf(get(ItemID.FLINT, 0, 1))
                }
            }

            else -> {
                return arrayOf(get(ItemID.FLINT, 0, 1))
            }
        }
        return arrayOf(toItem())
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAVEL)

    }
}
