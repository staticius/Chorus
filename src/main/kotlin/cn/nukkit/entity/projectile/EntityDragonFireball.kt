package cn.nukkit.entity.projectile

import cn.nukkit.entity.*
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.item.EntityAreaEffectCloud
import cn.nukkit.entity.mob.monster.EntityEnderDragon
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.*

class EntityDragonFireball(chunk: IChunk?, nbt: CompoundTag?) : EntityProjectile(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.DRAGON_FIREBALL
    }

    override fun getHeight(): Float {
        return 0.3125f
    }

    override fun getWidth(): Float {
        return 0.3125f
    }

    override fun getLength(): Float {
        return 0.3125f
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

    override fun onCollideWithEntity(entity: Entity) {
        if (entity !is EntityEnderDragon) onCollide()
    }

    override fun onCollideWithBlock(locator: Locator, motion: Vector3) {
        onCollide()
    }

    protected fun onCollide() {
        this.close()
        val entity: EntityAreaEffectCloud? = Entity.Companion.createEntity(
            EntityID.Companion.AREA_EFFECT_CLOUD,
            getLocator().getChunk(),
            CompoundTag().putList(
                "Pos", ListTag<Tag>()
                    .add(FloatTag(position.south))
                    .add(FloatTag(position.up))
                    .add(FloatTag(position.west))
            )
                .putList(
                    "Rotation", ListTag<Tag>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putList(
                    "Motion", ListTag<Tag>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putInt("Duration", 2400)
                .putFloat("InitialRadius", 6f)
                .putFloat("Radius", 6f)
                .putFloat("RadiusChangeOnPickup", 0f)
                .putFloat("RadiusPerTick", 0f)
        ) as EntityAreaEffectCloud?

        val effects: List<Effect?>? = PotionType.Companion.get(PotionType.Companion.HARMING_STRONG.id).getEffects(false)
        for (effect: Effect? in effects!!) {
            if (effect != null && entity != null) {
                entity.cloudEffects!!.add(effect.setVisible(false).setAmbient(false))
                entity.spawnToAll()
            }
        }
        entity!!.spawnToAll()
    }

    override fun getOriginalName(): String {
        return "Dragon FireBall"
    }
}
