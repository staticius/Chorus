package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockInfestedDeepslate @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Infested Deepslate"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 0.75

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.INFESTED_DEEPSLATE, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
