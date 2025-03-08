package cn.nukkit.entity.item

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.EntityEventPacket
import java.util.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class EntityElytraFirework(chunk: IChunk?, nbt: CompoundTag, private var followingPlayer: Player?) :
    EntityFireworksRocket(chunk, nbt) {
    private val lifetime: Int
    private var fireworkAge: Int = 0

    init {
        this.lifetime = 20 + RANDOM.nextInt(13)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        } else {
            val tickDiff: Int = currentTick - this.lastUpdate
            if (tickDiff <= 0 && !this.justCreated) {
                return true
            } else {
                this.lastUpdate = currentTick
                var hasUpdate: Boolean = this.entityBaseTick(tickDiff)
                if (this.isAlive()) {
                    motion.south = followingPlayer!!.motion.south
                    motion.up = followingPlayer!!.motion.up
                    motion.west = followingPlayer!!.motion.west
                    this.teleport(followingPlayer!!.getNextPosition().position.add(followingPlayer!!.getMotion()))

                    this.move(motion.south, motion.up, motion.west)
                    this.updateMovement()
                    val f: Float = sqrt(motion.south * motion.south + motion.west * motion.west).toFloat()
                    rotation.yaw = (atan2(motion.south, motion.west) * 57.29577951308232).toFloat().toDouble()
                    rotation.pitch = (atan2(motion.up, f.toDouble()) * 57.29577951308232).toFloat().toDouble()
                    if (this.fireworkAge == 0) {
                        level!!.addLevelSoundEvent(this.position, 56)
                    }

                    ++this.fireworkAge
                    hasUpdate = true
                    if (this.fireworkAge >= this.lifetime) {
                        val pk: EntityEventPacket = EntityEventPacket()
                        pk.data = 0
                        pk.event = 25
                        pk.eid = this.getId()
                        level!!.addLevelSoundEvent(this.position, 58, -1, 72)
                        Server.broadcastPacket(getViewers().values, pk)
                        this.kill()
                    }
                }
                return hasUpdate || !this.onGround || abs(motion.south) > 1.0E-5 || abs(
                    motion.up
                ) > 1.0E-5 || abs(motion.west) > 1.0E-5
            }
        }
    }

    fun getFollowingPlayer(): Player? {
        return followingPlayer
    }

    fun setFollowingPlayer(followingPlayer: Player?) {
        this.followingPlayer = followingPlayer
    }

    companion object {
        private val RANDOM: Random = Random()
    }
}
