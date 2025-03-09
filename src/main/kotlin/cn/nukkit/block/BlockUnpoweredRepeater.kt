package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockUnpoweredRepeater @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockRedstoneRepeater(blockstate) {
    init {
        this.isPowered = false
    }

    override val name: String
        get() = "Unpowered Repeater"

    public override fun getPowered(): Block {
        return BlockPoweredRepeater().setPropertyValues(blockState!!.blockPropertyValues)
    }

    override val unpowered: Block
        get() = this

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.UNPOWERED_REPEATER,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.REPEATER_DELAY
        )
            get() = Companion.field
    }
}