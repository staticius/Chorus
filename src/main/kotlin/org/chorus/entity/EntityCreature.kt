package org.chorus.entity

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.level.format.IChunk
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag

abstract class EntityCreature(chunk: IChunk?, nbt: CompoundTag?) : EntityLiving(chunk, nbt), EntityNameable,
    EntityAgeable {
    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        return super<EntityNameable>.onInteract(player, item, clickedPos)
    }
}
