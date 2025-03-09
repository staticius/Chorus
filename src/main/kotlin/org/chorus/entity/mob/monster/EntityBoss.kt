package org.chorus.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BossEventPacket

abstract class EntityBoss(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt) {
    protected var blockBreakSound: Sound? = null

    override fun setHealth(health: Float) {
        super.setHealth(health)
        val pkBoss = BossEventPacket()
        pkBoss.bossEid = this.id
        pkBoss.type = BossEventPacket.TYPE_HEALTH_PERCENT
        pkBoss.healthPercent = health / maxHealth
        Server.broadcastPacket(viewers.values, pkBoss)
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
        if (player.locallyInitialized) {
            addBossbar(player)
        }
    }

    protected fun canBreakBlock(block: Block): Boolean {
        return when (block.id) {
            Block.BARRIER, Block.BEDROCK, Block.COMMAND_BLOCK, Block.CHAIN_COMMAND_BLOCK, Block.REPEATING_COMMAND_BLOCK, Block.CRYING_OBSIDIAN, Block.END_STONE, Block.END_PORTAL, Block.END_PORTAL_FRAME, Block.END_GATEWAY, Block.FIRE, Block.IRON_BARS, Block.JIGSAW, Block.OBSIDIAN, Block.REINFORCED_DEEPSLATE, Block.RESPAWN_ANCHOR, Block.SOUL_FIRE, Block.STRUCTURE_BLOCK -> false
            else -> true
        }
    }

    abstract fun addBossbar(player: Player)
}
