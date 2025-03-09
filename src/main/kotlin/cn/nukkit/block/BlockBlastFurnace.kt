package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBlastFurnace @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLitBlastFurnace(blockstate) {
    override val name: String
        get() = "Blast Furnace"

    override val lightLevel: Int
        get() = 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BLAST_FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}
