package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item

class BlockMonsterEgg @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    var monsterEggStoneType: MonsterEggStoneType
        get() = getPropertyValue(CommonBlockProperties.MONSTER_EGG_STONE_TYPE)
        set(value) {
            setPropertyValue(
                CommonBlockProperties.MONSTER_EGG_STONE_TYPE,
                value
            )
        }

    override val name: String
        get() = monsterEggStoneType.name + " Monster Egg"

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.75

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MONSTER_EGG, CommonBlockProperties.MONSTER_EGG_STONE_TYPE)
            get() = Companion.field
    }
}
