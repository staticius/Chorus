package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.BossEventPacket

abstract class EntityBoss(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt) {
    protected var blockBreakSound: Sound? = null

    override fun setHealth(health: Float) {
        super.setHealth(health)
        Server.broadcastPacket(getViewers().values, BossEventPacket(
            targetActorID = this.runtimeId,
            eventType = BossEventPacket.EventType.UPDATE_PERCENT,
            eventData = BossEventPacket.EventType.Companion.UpdatePercentData(
                healthPercent = health / maxHealth
            )
        ))
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
        if (player.locallyInitialized) {
            addBossbar(player)
        }
    }

    protected fun canBreakBlock(block: Block): Boolean {
        return when (block.id) {
            BlockID.BARRIER,
            BlockID.BEDROCK,
            BlockID.COMMAND_BLOCK,
            BlockID.CHAIN_COMMAND_BLOCK,
            BlockID.REPEATING_COMMAND_BLOCK,
            BlockID.CRYING_OBSIDIAN,
            BlockID.END_STONE,
            BlockID.END_PORTAL,
            BlockID.END_PORTAL_FRAME,
            BlockID.END_GATEWAY,
            BlockID.FIRE,
            BlockID.IRON_BARS,
            BlockID.JIGSAW,
            BlockID.OBSIDIAN,
            BlockID.REINFORCED_DEEPSLATE,
            BlockID.RESPAWN_ANCHOR,
            BlockID.SOUL_FIRE,
            BlockID.STRUCTURE_BLOCK -> false
            else -> true
        }
    }

    abstract fun addBossbar(player: Player)
}
