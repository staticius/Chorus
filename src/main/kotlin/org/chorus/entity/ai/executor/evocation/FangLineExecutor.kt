package org.chorus.entity.ai.executor.evocation

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityEvocationFang
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager.SPELL
import cn.nukkit.level.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.BlockColor

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
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_CAST, tick)
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, tick)
            return false
        } else return true
    }


    override fun onStart(entity: EntityMob) {
        removeLookTarget(entity)
        startSpell(entity)
    }

    override fun onStop(entity: EntityMob) {
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        stopSpell(entity)
    }

    override fun onInterrupt(entity: EntityMob) {
        stopSpell(entity)
    }

    protected open fun startSpell(entity: EntityMob) {
        tick = 0
        entity.level!!.addSound(entity.position, Sound.MOB_EVOCATION_ILLAGER_PREPARE_SUMMON)
        entity.setDataProperty(EntityDataTypes.Companion.EVOKER_SPELL_CASTING_COLOR, BlockColor.PURPLE_BLOCK_COLOR.argb)
        entity.memoryStorage!!.put<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC, SPELL.CAST_LINE)
        entity.setDataFlag(EntityFlag.CASTING)
    }

    protected fun stopSpell(entity: EntityMob) {
        entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.LAST_MAGIC)
        entity.setDataFlag(EntityFlag.CASTING, false)
    }

    protected fun spell(entity: EntityLiving, distance: Int) {
        if (!entity.getDataFlag(EntityFlag.CASTING)) return
        var fangTransform = entity.transform
        val directionVector = entity.directionVector.multiply(0.8 * (distance + 1))
        fangTransform = fangTransform.add(directionVector.getX(), 0.0, directionVector.getZ())
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

        val fang: Entity = Entity.Companion.createEntity(
            EntityID.Companion.EVOCATION_FANG,
            transform.level.getChunk(transform.position.chunkX, transform.position.chunkZ),
            nbt
        )
        if (fang is EntityEvocationFang) {
            fang.evocationIllager = illager
        }
        if (fang != null) fang.spawnToAll()
    }

    companion object {
        //Values represent ticks
        private const val CAST_DURATION = 40
        private const val DELAY_PER_SUMMON = 1
        private const val SPAWN_COUNT = 16


        private const val DURATION = CAST_DURATION + (DELAY_PER_SUMMON * SPAWN_COUNT)
    }
}
