package org.chorus.block

import cn.nukkit.item.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class BlockUnknown @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    Block(null) {
    init {
        this.blockState = blockstate
    }

    override val name: String
        get() = "Unknown"

    override fun toItem(): Item? {
        return ItemBlock(this.clone())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.UNKNOWN)
            get() = Companion.field
    }
}
