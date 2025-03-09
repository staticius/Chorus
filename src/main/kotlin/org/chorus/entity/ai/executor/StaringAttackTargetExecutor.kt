package org.chorus.entity.ai.executor

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.Sound

class StaringAttackTargetExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        if (entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)) {
            if (!entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.STARING_PLAYER) && EntityCheckEvaluator(
                    CoreMemoryTypes.Companion.STARING_PLAYER
                ).evaluate(entity)
            ) {
                entity.memoryStorage!!.put<Entity>(
                    CoreMemoryTypes.Companion.ATTACK_TARGET, entity.memoryStorage!!
                        .get<Player>(CoreMemoryTypes.Companion.STARING_PLAYER)
                )
                entity.level!!.addSound(entity.position, Sound.MOB_ENDERMEN_STARE)
            } else if (!entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_ENDERMITE)) {
                entity.memoryStorage!!.put<Entity>(
                    CoreMemoryTypes.Companion.ATTACK_TARGET, entity.memoryStorage!!
                        .get<Entity>(CoreMemoryTypes.Companion.NEAREST_ENDERMITE)
                )
            }
        } else {
            if (entity.memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is Player) {
                if (!player.isOnline()) {
                    entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.STARING_PLAYER)
                    entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.ATTACK_TARGET)
                }
            }
        }
        if (entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)) {
            if (entity.getDataFlag(EntityFlag.ANGRY)) {
                entity.setDataFlag(EntityFlag.ANGRY, false)
            }
        } else {
            if (EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET).evaluate(entity)) {
                if (!entity.getDataFlag(EntityFlag.ANGRY)) {
                    entity.setDataFlag(EntityFlag.ANGRY)
                }
            } else entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.ATTACK_TARGET)
        }
        return true
    }
}
