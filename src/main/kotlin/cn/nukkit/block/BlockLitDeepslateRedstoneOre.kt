package cn.nukkit.block

import cn.nukkit.item.*

class BlockLitDeepslateRedstoneOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDeepslateRedstoneOre(blockstate), IBlockOreRedstoneGlowing {
    override val name: String
        get() = "Glowing Deepslate Redstone Ore"

    override val lightLevel: Int
        get() = 9

    override fun toItem(): Item? {
        return super<IBlockOreRedstoneGlowing>.toItem()
    }

    override fun onUpdate(type: Int): Int {
        return super<IBlockOreRedstoneGlowing>.onUpdate(this, type)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIT_DEEPSLATE_REDSTONE_ORE)
            get() = Companion.field
    }
}