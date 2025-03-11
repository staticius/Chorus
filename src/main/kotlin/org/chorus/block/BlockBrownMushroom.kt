package org.chorus.block

class BlockBrownMushroom @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockMushroom(blockstate) {
    override val name: String
        get() = "Brown Mushroom"

    override val lightLevel: Int
        get() = 1

    override fun getType(): MushroomType {
        return ObjectBigMushroom.MushroomType.BROWN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BROWN_MUSHROOM)

    }
}