package org.chorus.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.entity.Entity
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.item.Item
import cn.nukkit.level.Sound
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author CreeperFace
 */
open class ProjectileDispenseBehavior(protected val entityType: String) : DefaultDispenseBehavior() {
    override fun dispense(source: BlockDispenser, face: BlockFace, item: Item): Item? {
        val dispensePos = source.dispensePosition

        val nbt = Entity.getDefaultNBT(dispensePos)
        this.correctNBT(nbt, item)

        val projectile = Entity.createEntity(
            entityType,
            source.level.getChunk(dispensePos.chunkX, dispensePos.chunkZ),
            nbt
        ) as? EntityProjectile
            ?: return super.dispense(source, face, item)

        val motion = initMotion(face)

        projectile.setMotion(motion)
        projectile.inaccurate(accuracy)
        projectile.setMotion(projectile.getMotion().multiply(this.motion))

        projectile.updateRotation()

        projectile.spawnToAll()

        source.level.addSound(source.position, shootingSound)

        return null
    }

    protected open val shootingSound: Sound?
        get() = Sound.RANDOM_BOW

    protected open fun initMotion(face: BlockFace): Vector3? {
        return Vector3(face.xOffset.toDouble(), (face.yOffset + 0.1f).toDouble(), face.zOffset.toDouble())
            .normalize()
    }

    protected open val motion: Double
        get() = 1.1

    protected open val accuracy: Float
        get() = 6f

    /**
     * you can add extra data of projectile here
     *
     * @param nbt tag
     */
    protected fun correctNBT(nbt: CompoundTag, item: Item? = null) {
        if (item != null) {
            if (item.id === Item.SPLASH_POTION || item.id === Item.LINGERING_POTION) {
                nbt.putInt("PotionId", item.damage)
            }
        }
    }
}
