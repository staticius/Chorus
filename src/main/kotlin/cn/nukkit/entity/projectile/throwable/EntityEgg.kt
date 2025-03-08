package cn.nukkit.entity.projectile.throwable

import cn.nukkit.entity.*
import cn.nukkit.item.ItemEgg
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.particle.ItemBreakParticle
import cn.nukkit.nbt.tag.CompoundTag
import java.util.concurrent.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityEgg @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag, shootingEntity: Entity? = null) :
    EntityThrowable(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.EGG
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
        return 0.03f
    }

    override fun getDrag(): Float {
        return 0.01f
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.age > 1200 || this.isCollided) {
            this.kill()
            hasUpdate = true
        }

        return hasUpdate
    }

    override fun addHitEffect() {
        val particles: Int = ThreadLocalRandom.current().nextInt(10) + 5
        val egg: ItemEgg = ItemEgg()
        for (i in 0..<particles) {
            level!!.addParticle(ItemBreakParticle(this.position, egg))
        }
    }

    override fun getOriginalName(): String {
        return "Egg"
    }
}
