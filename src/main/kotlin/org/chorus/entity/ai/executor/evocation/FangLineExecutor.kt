package org.chorus.entity.ai.executor.evocation

import org.chorus.entity.*
import org.chorus.entity.ai.executor.EntityControl
import org.chorus.entity.ai.executor.IBehaviorExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.monster.EntityEvocationFang
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager.SPELL
import org.chorus.level.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import org.chorus.utils.BlockColor

open class FangLineExecutor : EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        if (tick == CAST_DURATION) {
            entity.rotation.yaw = (entity.headYaw)
        } else if (tick > CAST_DURATION) {
            spell(entity, tick - CAST_DURATION)
        }
        tick++
        if (tick >= DURATION) {
            val tick = entity.level!!.tick
            entity.memoryStorage[CoreMemoryTypes.LAST_ATTACK_CAST] = tick
            entity.memoryStorage[CoreMemoryTypes.LAST_ATTACK_TIME] = tick
            return false
        } else return true
    }


    override fun onStart(entity: EntityMob) {
        removeLookTarget(entity)
        startSpell(entity)
    }

    override fun onStop(entity: EntityMob) {
        entity.setMovementSpeed(EntityLiving.DEFAULT_SPEED)
        entity.setEnablePitch(false)
        stopSpell(entity)
    }

    override fun onInterrupt(entity: EntityMob) {
        stopSpell(entity)
    }

    protected open fun startSpell(entity: EntityMob) {
        tick = 0
        entity.level!!.addSound(entity.position, Sound.MOB_EVOCATION_ILLAGER_PREPARE_SUMMON)
        entity.setDataProperty(EntityDataTypes.EVOKER_SPELL_CASTING_COLOR, BlockColor.PURPLE_BLOCK_COLOR.argb)
        entity.memoryStorage[CoreMemoryTypes.LAST_MAGIC] = SPELL.CAST_LINE
        entity.setDataFlag(EntityFlag.CASTING)
    }

    protected fun stopSpell(entity: EntityMob) {
        entity.memoryStorage.clear(CoreMemoryTypes.LAST_MAGIC)
        entity.setDataFlag(EntityFlag.CASTING, false)
    }

    protected fun spell(entity: EntityLiving, distance: Int) {
        if (!entity.getDataFlag(EntityFlag.CASTING)) return
        var fangTransform = entity.getTransform()
        val directionVector = entity.getDirectionVector().multiply(0.8 * (distance + 1))
        fangTransform = fangTransform.add(directionVector.x, 0.0, directionVector.z)
        spawn(entity as EntityEvocationIllager, fangTransform)
    }

    protected fun spawn(illager: EntityEvocationIllager?, transform: Transform) {
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(transform.position.x))
                    .add(FloatTag(transform.position.y))
                    .add(FloatTag(transform.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((transform.rotation.yaw)))
                    .add(FloatTag(0f))
            )

        val fang = Entity.Companion.createEntity(
            EntityID.EVOCATION_FANG,
            transform.level.getChunk(transform.position.chunkX, transform.position.chunkZ),
            nbt
        )
        if (fang is EntityEvocationFang) {
            fang.evocationIllager = illager
        }
        fang?.spawnToAll()
    }

    companion object {
        //Values represent ticks
        private const val CAST_DURATION = 40
        private const val DELAY_PER_SUMMON = 1
        private const val SPAWN_COUNT = 16


        private const val DURATION = CAST_DURATION + (DELAY_PER_SUMMON * SPAWN_COUNT)
    }
}
