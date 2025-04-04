package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.entity.Entity
import org.chorus.event.entity.EntityDamageByBlockEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get

class BlockSoulCampfire @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCampfire(blockstate) {
    override val name: String
        get() = "Soul Campfire"

    override val lightLevel: Int
        get() = if (isExtinguished) 0 else 10

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(get(BlockID.SOUL_SOIL, 0, 1))
    }

    override fun getDamageEvent(entity: Entity): EntityDamageEvent? {
        return EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.FIRE, 2f)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SOUL_CAMPFIRE,
            CommonBlockProperties.EXTINGUISHED,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        )
    }
}
