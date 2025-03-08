package cn.nukkit.entity.mob.animal

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.BlockID
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.EntityEventPacket
import java.util.concurrent.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityAnimal(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt) {
    override fun initEntity() {
        super.initEntity()
    }

    override fun saveNBT() {
        super.saveNBT()
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        val superResult = super.onInteract(player, item, clickedPos)
        if (isBreedingItem(item)) {
            return useBreedingItem(player, item) && superResult
        }
        return superResult
    }

    protected open fun useBreedingItem(player: Player, item: Item): Boolean {
        memoryStorage!!.put<Player>(CoreMemoryTypes.Companion.LAST_FEED_PLAYER, player)
        memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, level!!.tick)
        sendBreedingAnimation(item)
        item.count--
        return player.inventory.setItemInHand(item)
    }

    protected fun sendBreedingAnimation(item: Item) {
        val pk = EntityEventPacket()
        pk.event = EntityEventPacket.EATING_ITEM
        pk.eid = this.getId()
        pk.data = item.fullId
        Server.broadcastPacket(this.viewers.values, pk)
    }

    /**
     * 可以导致繁殖的喂养物品
     *
     *
     * Feeding items that can lead to reproduction.
     *
     * @param item 物品
     * @return boolean 是否可以导致繁殖<br></br>Whether it can lead to reproduction
     */
    open fun isBreedingItem(item: Item): Boolean {
        return item.id == BlockID.WHEAT //default
    }

    override fun getStepHeight(): Double {
        return 0.5
    }

    override fun getExperienceDrops(): Int {
        return ThreadLocalRandom.current().nextInt(3) + 1
    }
}
