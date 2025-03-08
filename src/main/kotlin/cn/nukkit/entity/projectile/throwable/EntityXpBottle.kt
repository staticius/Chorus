package cn.nukkit.entity.projectile.throwable

import cn.nukkit.entity.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.particle.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import java.util.concurrent.*

/**
 * @author xtypr
 */
class EntityXpBottle @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag, shootingEntity: Entity? = null) :
    EntityThrowable(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.XP_BOTTLE
    }


    override fun getWidth(): Float {
        return 0.25f
    }

    override fun getLength(): Float {
        return 0.25f
    }

    override fun getHeight(): Float {
        return 0.25f
    }

    override fun getGravity(): Float {
        return 0.1f
    }

    override fun getDrag(): Float {
        return 0.01f
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.age > 1200) {
            this.kill()
            hasUpdate = true
        }

        if (this.isCollided) {
            this.kill()
            this.dropXp()
            hasUpdate = true
        }

        return hasUpdate
    }

    override fun onCollideWithEntity(entity: Entity) {
        this.kill()
        this.dropXp()
    }

    fun dropXp() {
        val particle2: Particle = SpellParticle(this.position, 0x00385dc6)
        level!!.addParticle(particle2)

        level!!.addLevelSoundEvent(this.position, LevelSoundEventPacket.SOUND_GLASS)

        level!!.dropExpOrb(this.position, ThreadLocalRandom.current().nextInt(3, 12))
    }

    override fun addHitEffect() {
        level!!.addSound(this.position, Sound.RANDOM_GLASS)
    }

    override fun getOriginalName(): String {
        return "Bottle o' Enchanting"
    }
}
