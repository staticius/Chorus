package org.chorus.block

import org.chorus.item.Item

class BlockLitRedstoneOre @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockRedstoneOre(blockstate), IBlockOreRedstoneGlowing {
    override val name: String
        get() = "Glowing Redstone Ore"

    override val lightLevel: Int
        get() = 9

    override fun toItem(): Item? {
        return super<IBlockOreRedstoneGlowing>.toItem()
    }

    override fun onUpdate(type: Int): Int {
        return super<IBlockOreRedstoneGlowing>.onUpdate(this, type)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIT_REDSTONE_ORE)

    }
}