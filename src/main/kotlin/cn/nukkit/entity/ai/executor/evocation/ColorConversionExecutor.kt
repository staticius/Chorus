package cn.nukkit.entity.ai.executor.evocation

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntitySheep
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager.SPELL
import cn.nukkit.level.Sound
import cn.nukkit.utils.BlockColor
import cn.nukkit.utils.DyeColor

class ColorConversionExecutor : FangLineExecutor() {
    override fun execute(entity: EntityMob): Boolean {
        tick++
        if (tick == CAST_DURATION) {
            for (entity1 in entity.level!!.getNearbyEntities(
                entity.getBoundingBox()!!.grow(16.0, 16.0, 16.0)
            )) {
                if (entity1 is EntitySheep) {
                    if (entity1.getColor() == DyeColor.BLUE.woolData) {
                        entity1.setColor(DyeColor.RED.woolData)
                    }
                }
            }
        }
        if (tick >= CAST_DURATION) {
            val tick = entity.level!!.tick
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_CONVERSION, tick)
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, tick)
            return false
        } else return true
    }

    override fun startSpell(entity: EntityMob) {
        tick = 0
        entity.level!!.addSound(entity.position, Sound.MOB_EVOCATION_ILLAGER_PREPARE_WOLOLO)
        entity.setDataProperty(EntityDataTypes.Companion.EVOKER_SPELL_CASTING_COLOR, BlockColor.ORANGE_BLOCK_COLOR.argb)
        entity.memoryStorage!!.put<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC, SPELL.COLOR_CONVERSION)
        entity.setDataFlag(EntityFlag.CASTING)
    }

    companion object {
        //Values represent ticks
        private const val CAST_DURATION = 60
    }
}
