package org.chorus.entity.ai.executor

import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityZombiePigman
import cn.nukkit.inventory.*
import cn.nukkit.level.Sound

class PiglinTransformExecutor : EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        tick++
        if (tick >= 300) {
            transform(entity)
            return false
        }
        return true
    }


    override fun onStart(entity: EntityMob) {
        tick = -1
        entity.setDataFlag(EntityFlag.SHAKING)
    }

    override fun onStop(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.SHAKING, false)
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }

    private fun transform(entity: EntityMob) {
        entity.saveNBT()
        entity.close()
        val entityZombiePigman = EntityZombiePigman(entity.locator.chunk, entity.namedTag)
        entityZombiePigman.setPosition(entity.position)
        entityZombiePigman.setRotation(entity.rotation.yaw, entity.rotation.pitch)
        entityZombiePigman.spawnToAll()
        entityZombiePigman.level!!.addSound(entityZombiePigman.position, Sound.MOB_PIGLIN_CONVERTED_TO_ZOMBIFIED)
        val inventory: Inventory = entityZombiePigman.equipment
        for (i in 2..<inventory.size) {
            entityZombiePigman.level!!.dropItem(entityZombiePigman.position, inventory.getItem(i))
            inventory.clear(i)
        }
        // TODO
        entityZombiePigman.addEffect(Effect.Companion.get(EffectType.Companion.NAUSEA).setDuration(15))
    }
}


