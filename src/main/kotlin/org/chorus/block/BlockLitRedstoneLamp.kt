package org.chorus.block

import cn.nukkit.event.redstone.RedstoneUpdateEvent
import cn.nukkit.item.*
import cn.nukkit.level.Level

class BlockLitRedstoneLamp @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockRedstoneLamp(blockstate) {
    override val name: String
        get() = "Lit Redstone Lamp"

    override val lightLevel: Int
        get() = 15

    override fun toItem(): Item? {
        return ItemBlock(get(BlockID.REDSTONE_LAMP))
    }

    override fun onUpdate(type: Int): Int {
        if (!level.server.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if ((type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) && !this.isGettingPower) {
            level.scheduleUpdate(this, 4)
            return 1
        }

        if (type == Level.BLOCK_UPDATE_SCHEDULED && !this.isGettingPower) {
            // Redstone event
            val ev = RedstoneUpdateEvent(this)
            level.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return 0
            }

            level.updateComparatorOutputLevelSelective(this.position, true)

            level.setBlock(this.position, get(BlockID.REDSTONE_LAMP), false, false)
        }
        return 0
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIT_REDSTONE_LAMP)
            get() = Companion.field
    }
}