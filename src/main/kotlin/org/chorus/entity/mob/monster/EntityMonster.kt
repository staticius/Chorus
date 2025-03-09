package org.chorus.entity.mob.monster

import org.chorus.entity.EntityCanAttack
import org.chorus.entity.mob.EntityMob
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import kotlin.math.max

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityMonster(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityCanAttack {
    protected var spawnedByNight: Boolean = true

    override fun initEntity() {
        super.initEntity()

        spawnedByNight = namedTag!!.getBoolean(TAG_SPAWNED_BY_NIGHT)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (getServer()!!.difficulty == 0) {
            this.close()
            return true
        } else return super.onUpdate(currentTick)
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putBoolean(TAG_SPAWNED_BY_NIGHT, spawnedByNight)
    }

    override fun setOnFire(seconds: Int) {
        var seconds = seconds
        var level = 0

        for (armor in equipment.armor) {
            val fireProtection = armor.getEnchantment(Enchantment.ID_PROTECTION_FIRE)

            if (fireProtection != null && fireProtection.level > 0) {
                level = max(level.toDouble(), fireProtection.level.toDouble()).toInt()
            }
        }

        seconds = (seconds * (1 - level * 0.15)).toInt()

        super.setOnFire(seconds)
    }

    companion object {
        private const val TAG_SPAWNED_BY_NIGHT = "SpawnedByNight"
    }
}
