package org.chorus.entity.ai.executor

import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityWarden
import cn.nukkit.level.Sound

class WardenSniffExecutor(//gt
    protected var duration: Int, protected var angerAddition: Int
) : IBehaviorExecutor {
    protected var endTime: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        if (entity.level!!.tick >= this.endTime) {
            sniff(entity)
            return false
        } else {
            return true
        }
    }

    override fun onStart(entity: EntityMob) {
        this.endTime = entity.level!!.tick + this.duration
        entity.setDataFlag(EntityFlag.SNIFFING, true)
        entity.setDataFlagExtend(EntityFlag.SNIFFING, true)
        entity.level!!.addSound(entity.position.clone(), Sound.MOB_WARDEN_SNIFF)
    }

    override fun onStop(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.SNIFFING, false)
        entity.setDataFlagExtend(EntityFlag.SNIFFING, false)
    }

    override fun onInterrupt(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.SNIFFING, false)
        entity.setDataFlagExtend(EntityFlag.SNIFFING, false)
    }

    protected fun sniff(entity: EntityMob) {
        if (entity !is EntityWarden) return
        for (other in entity.level!!.entities) {
            if (!entity.isValidAngerEntity(other, true)) continue
            entity.addEntityAngerValue(other, this.angerAddition)
        }
    }
}
