package cn.nukkit.block

import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool

abstract class BlockDoubleWoodenSlab(blockstate: BlockState?) : BlockDoubleSlabBase(blockstate) {
    override val name: String
        get() = "Double $slabName Wood Slab"

    override val resistance: Double
        get() = 15.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun isCorrectTool(item: Item): Boolean {
        return true
    }
}