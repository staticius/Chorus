package cn.nukkit.block

import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock

class BlockCrimsonRoots @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHanging(blockstate), FlowerPotBlock, Natural {
    override val name: String
        get() = "Crimson Roots"

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_ROOTS)
            get() = Companion.field
    }
}