package org.chorus.entity.ai.executor.evocation

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityVex
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager.SPELL
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.BlockColor
import java.util.concurrent.*

class VexSummonExecutor : FangLineExecutor() {
    override fun execute(entity: EntityMob): Boolean {
        tick++
        if (tick == CAST_DURATION) {
            for (i in 0..<VEX_COUNT) {
                var count = 0
                for (entity1 in entity.level!!.getNearbyEntities(
                    entity.getBoundingBox()!!.grow(15.0, 15.0, 15.0)
                )) {
                    if (entity1 is EntityVex) count++
                }
                if (count < 8) {
                    summon(entity)
                }
            }
        }
        if (tick >= CAST_DURATION) {
            val tick = entity.level!!.tick
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_SUMMON, tick)
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, tick)
            return false
        } else return true
    }

    override fun startSpell(entity: EntityMob) {
        tick = 0
        entity.level!!.addSound(entity.position, Sound.MOB_EVOCATION_ILLAGER_PREPARE_SUMMON)
        entity.setDataProperty(EntityDataTypes.Companion.EVOKER_SPELL_CASTING_COLOR, BlockColor.WHITE_BLOCK_COLOR.argb)
        entity.memoryStorage!!.put<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC, SPELL.SUMMON)
        entity.setDataFlag(EntityFlag.CASTING)
    }

    protected fun summon(entity: EntityMob) {
        if (!entity.getDataFlag(EntityFlag.CASTING)) return
        val vexTransform = entity.transform
        vexTransform.position.x += ThreadLocalRandom.current().nextFloat(2f).toDouble()
        vexTransform.position.y += ThreadLocalRandom.current().nextFloat(2f).toDouble()
        vexTransform.position.z += ThreadLocalRandom.current().nextFloat(2f).toDouble()
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(vexTransform.position.x))
                    .add(FloatTag(vexTransform.position.y))
                    .add(FloatTag(vexTransform.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (entity.headYaw > 180) 360 else 0).toFloat() - entity.headYaw))
                    .add(FloatTag(0f))
            )

        val vexEntity: Entity = Entity.Companion.createEntity(
            EntityID.Companion.VEX,
            entity.level!!.getChunk(entity.position.chunkX, entity.position.chunkZ),
            nbt
        )
        if (vexEntity is EntityVex) {
            vexEntity.illager = entity as EntityEvocationIllager
        }
        if (vexEntity != null) vexEntity.spawnToAll()
    }

    companion object {
        //Values represent ticks
        private const val CAST_DURATION = 100
        private const val VEX_COUNT = 3
    }
}
