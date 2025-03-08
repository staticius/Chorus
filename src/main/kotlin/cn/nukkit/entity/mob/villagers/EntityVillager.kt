package cn.nukkit.entity.mob.villagers

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityDrowned
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityZombie
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityZombieVillager
import cn.nukkit.entity.projectile.abstract_arrow.EntityThrownTrident
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author Pub4Game
 * @since 21.06.2016
 */
class EntityVillager(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.VILLAGER
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.3f
        }
        return 0.6f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.95f
        }
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Villager"
    }

    public override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()

        if (!namedTag!!.contains("Profession")) {
            this.setProfession(PROFESSION_GENERIC)
        }
    }

    fun getProfession(): Int {
        return namedTag!!.getInt("Profession")
    }

    fun setProfession(profession: Int) {
        namedTag!!.putInt("Profession", profession)
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (getHealth() - source.finalDamage <= 1) {
            if (source is EntityDamageByEntityEvent) {
                if (source.damager is EntityThrownTrident) {
                    if (trident.shootingEntity is EntityDrowned) {
                        transform()
                        return true
                    }
                } else if (source.damager is EntityZombie) {
                    transform()
                    return true
                }
            }
        }
        return super.attack(source)
    }

    private fun transform() {
        this.close()
        val zombieVillager = EntityZombieVillager(this.locator.chunk, this.namedTag)
        zombieVillager.setPosition(this.position)
        zombieVillager.setRotation(rotation.yaw, rotation.pitch)
        zombieVillager.spawnToAll()
    }

    override fun getExperienceDrops(): Int {
        return 0
    }

    companion object {
        const val PROFESSION_FARMER: Int = 0
        const val PROFESSION_LIBRARIAN: Int = 1
        const val PROFESSION_PRIEST: Int = 2
        const val PROFESSION_BLACKSMITH: Int = 3
        const val PROFESSION_BUTCHER: Int = 4
        const val PROFESSION_GENERIC: Int = 5
    }
}
