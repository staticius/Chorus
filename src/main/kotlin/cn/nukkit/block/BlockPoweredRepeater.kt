package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPoweredRepeater @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockRedstoneRepeater(blockstate) {
    init {
        isPowered = true
    }

    override val name: String
        get() = "Powered Repeater"

    public override fun getPowered(): Block {
        return this
    }

    public override fun getUnpowered(): Block {
        val blockUnpoweredRepeater = BlockUnpoweredRepeater()
        return blockUnpoweredRepeater.setPropertyValues(blockState!!.blockPropertyValues)
    }

    override val lightLevel: Int
        get() = 7

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POWERED_REPEATER,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.REPEATER_DELAY
        )
            get() = Companion.field
    }
}