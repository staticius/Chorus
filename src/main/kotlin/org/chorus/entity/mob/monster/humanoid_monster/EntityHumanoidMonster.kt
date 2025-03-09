package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.entity.EntityCanAttack
import org.chorus.entity.mob.monster.EntityMonster
import org.chorus.item.Item
import org.chorus.level.format.IChunk
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityHumanoidMonster(chunk: IChunk?, nbt: CompoundTag?) : EntityMonster(chunk, nbt!!), EntityCanAttack {
    protected var itemInHand: Item? = null

    override fun initEntity() {
        super.initEntity()

        if (namedTag!!.contains(TAG_ITEM_IN_HAND)) {
            itemInHand = NBTIO.getItemHelper(namedTag!!.getCompound(TAG_ITEM_IN_HAND))
        }
    }

    override fun saveNBT() {
        super.saveNBT()

        if (itemInHand != null) {
            namedTag!!.putCompound(TAG_ITEM_IN_HAND, NBTIO.putItemHelper(itemInHand))
        }
    }

    companion object {
        private const val TAG_ITEM_IN_HAND = "ItemInHand"
    }
}
