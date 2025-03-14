package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import java.util.*
import kotlin.math.min

class BlockMelonBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), Natural {
    override val name: String
        get() = "Melon Block"

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 5.0

    override fun getDrops(item: Item): Array<Item> {
        val random = Random()
        var count = 3 + random.nextInt(5)

        val fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
        if (fortune != null && fortune.level >= 1) {
            count += random.nextInt(fortune.level + 1)
        }

        return arrayOf<Item?>(
            ItemMelonSlice(0, min(9.0, count.toDouble()).toInt())
        )
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MELON_BLOCK)

    }
}