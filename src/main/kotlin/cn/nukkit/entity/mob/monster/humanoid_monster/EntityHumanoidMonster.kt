package cn.nukkit.entity.mob.monster.humanoid_monster

import cn.nukkit.entity.EntityCanAttack
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.item.Item
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag

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
