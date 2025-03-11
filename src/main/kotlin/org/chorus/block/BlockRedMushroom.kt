package org.chorus.block

class BlockRedMushroom @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockMushroom(blockstate) {
    override val name: String
        get() = "Red Mushroom"

    override val lightLevel: Int
        get() = 1

    override val type: MushroomType
        get() = ObjectBigMushroom.MushroomType.RED

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_MUSHROOM)

    }
}