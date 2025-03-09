package org.chorus.event.entity

import cn.nukkit.entity.EntityLiving
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class EntityDeathEvent @JvmOverloads constructor(entity: EntityLiving?, drops: Array<Item>? = Item.EMPTY_ARRAY) :
    EntityEvent() {
    private var drops: Array<Item>?

    init {
        this.entity = entity
        this.drops = drops
    }

    fun getDrops(): Array<Item>? {
        return drops
    }

    fun setDrops(drops: Array<Item>?) {
        var drops = drops
        if (drops == null) {
            drops = Item.EMPTY_ARRAY
        }

        this.drops = drops
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
