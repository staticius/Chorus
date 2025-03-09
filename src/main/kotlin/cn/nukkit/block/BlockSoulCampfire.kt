package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.Entity
import cn.nukkit.item.Item
import cn.nukkit.item.Item.Companion.get

class BlockSoulCampfire @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCampfire(blockstate) {
    override val name: String
        get() = "Soul Campfire"

    override val lightLevel: Int
        get() = if (isExtinguished) 0 else 10

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(get(BlockID.SOUL_SOIL, 0, 1))
    }

    override fun getDamageEvent(entity: Entity): EntityDamageEvent? {
        return EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.FIRE, 2f)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SOUL_CAMPFIRE,
            CommonBlockProperties.EXTINGUISHED,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        )
            get() = Companion.field
    }
}
