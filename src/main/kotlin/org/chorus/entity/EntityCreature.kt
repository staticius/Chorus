package org.chorus.entity

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag

/**
 * 实体生物
 *
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityCreature(chunk: IChunk?, nbt: CompoundTag?) : EntityLiving(chunk, nbt), EntityNameable,
    EntityAgeable {
    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        return super<EntityNameable>.onInteract(player, item, clickedPos)
    }
}
