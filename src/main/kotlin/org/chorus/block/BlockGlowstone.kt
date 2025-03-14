package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemGlowstoneDust
import org.chorus.item.enchantment.Enchantment
import org.chorus.math.MathHelper.clamp
import java.util.*

class BlockGlowstone : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Glowstone"

    override val resistance: Double
        get() = 1.5

    override val hardness: Double
        get() = 0.3

    override val lightLevel: Int
        get() = 15

    override fun getDrops(item: Item): Array<Item> {
        val random = Random()
        var count = 2 + random.nextInt(3)

        val fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
        if (fortune != null && fortune.level >= 1) {
            count += random.nextInt(fortune.level + 1)
        }

        return arrayOf(
            ItemGlowstoneDust(0, clamp(count, 1, 4))
        )
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GLOWSTONE)

    }
}
