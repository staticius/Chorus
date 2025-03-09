package org.chorus.block

open class BlockDeepslateRedstoneOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockRedstoneOre(blockstate) {
    override val name: String
        get() = "Deepslate Redstone Ore"

    override val hardness: Double
        get() = 4.5

    override fun getLitBlock(): Block {
        return BlockLitDeepslateRedstoneOre()
    }

    override fun getUnlitBlock(): Block {
        return BlockDeepslateRedstoneOre()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DEEPSLATE_REDSTONE_ORE)
            get() = Companion.field
    }
}