package org.chorus.entity.projectile.throwable

import org.chorus.entity.*
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.effect.*
import org.chorus.entity.item.EntityAreaEffectCloud
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.*

class EntityLingeringPotion : EntitySplashPotion {
    override fun getIdentifier(): String {
        return EntityID.LINGERING_POTION
    }

    constructor(chunk: IChunk?, nbt: CompoundTag) : super(chunk, nbt)

    constructor(chunk: IChunk?, nbt: CompoundTag, shootingEntity: Entity?) : super(chunk, nbt, shootingEntity)

    override fun initEntity() {
        super.initEntity()
        setDataFlag(EntityFlag.LINGERING, true)
    }

    override fun splash(collidedWith: Entity?) {
        super.splash(collidedWith)
        saveNBT()
        val pos: ListTag<*> = namedTag!!.getList("Pos", CompoundTag::class.java).copy() as ListTag<*>
        val entity: EntityAreaEffectCloud? = Entity.Companion.createEntity(
            EntityID.AREA_EFFECT_CLOUD,
            getLocator().chunk,
            CompoundTag().putList("Pos", pos)
                .putList(
                    "Rotation", ListTag<FloatTag>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putList(
                    "Motion", ListTag<FloatTag>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putShort("PotionId", potionId)
        ) as EntityAreaEffectCloud?

        val effects: List<Effect?> = PotionType.Companion.get(potionId).getEffects(true)
        for (effect: Effect? in effects) {
            if (effect != null && entity != null) {
                entity.cloudEffects!!.add(effect /*.setDuration(1)*/.setVisible(false).setAmbient(false))
                entity.spawnToAll()
            }
        }
    }

    override fun getOriginalName(): String {
        return "Lingering Potion"
    }
}
