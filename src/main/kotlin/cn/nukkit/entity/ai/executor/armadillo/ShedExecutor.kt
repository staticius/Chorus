package cn.nukkit.entity.ai.executor.armadillo

import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.utils.*

class ShedExecutor : EntityControl, IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        return false
    }

    override fun onStart(entity: EntityMob) {
        entity.level!!.dropItem(entity.position, Item.get(Item.ARMADILLO_SCUTE))
        entity.level!!.addSound(entity.position, Sound.MOB_ARMADILLO_SCUTE_DROP)
        entity.memoryStorage!!.put<Int>(
            CoreMemoryTypes.Companion.NEXT_SHED,
            entity.level!!.tick + Utils.rand(6000, 10800)
        )
    }
}
