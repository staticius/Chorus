package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockSoulLantern @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockLantern(blockstate) {
    override val name: String
        get() = "Soul Lantern"

    override val lightLevel: Int
        get() = 10

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SOUL_LANTERN, CommonBlockProperties.HANGING)
            get() = Companion.field
    }
}
