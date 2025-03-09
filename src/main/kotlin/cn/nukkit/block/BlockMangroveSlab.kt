package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockMangroveSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenSlab(blockstate, BlockID.MANGROVE_DOUBLE_SLAB) {
    override val name: String
        get() = (if (isOnTop) "Upper " else "") + slabName + " Wood Slab"

    override fun getSlabName(): String {
        return "Mangrove"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MANGROVE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}