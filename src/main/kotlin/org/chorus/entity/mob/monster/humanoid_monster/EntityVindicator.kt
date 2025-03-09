package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityHoglin
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import java.util.*
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function
import kotlin.collections.setOf

/**
 * @author PikyCZ
 */
class EntityVindicator(chunk: IChunk?, nbt: CompoundTag?) : EntityIllager(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.VINDICATOR
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(
                        Sound.MOB_VINDICATOR_IDLE,
                        if (isBaby()) 1.3f else 0.8f,
                        if (isBaby()) 1.7f else 1.2f,
                        1f,
                        1f
                    ), RandomSoundEvaluator(), 7, 1
                ),
                Behavior(
                    VindicatorMeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    VindicatorMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.5f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    3,
                    1
                ),
                Behavior(
                    VindicatorMeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.5f,
                        40,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.5f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                NearestPlayerSensor(16.0, 0.0, 0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }


    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.IRON_GOLEM, EntityID.Companion.SNOW_GOLEM, EntityID.Companion.VILLAGER, EntityID.Companion.WANDERING_TRADER -> true
            else -> super.attackTarget(entity) || isJohnny()
        }
    }

    override fun initEntity() {
        this.maxHealth = 24
        this.diffHandDamage = floatArrayOf(3.5f, 5f, 7.5f)
        super.initEntity()
        setItemInHand(Item.get(Item.IRON_AXE))
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Vindicator"
    }

    override fun getExperienceDrops(): Int {
        return Math.toIntExact(
            if (isBaby()) 1 else 5 + (equipment.contents.values.stream().filter { obj: Item -> obj.isArmor }
                .count() * ThreadLocalRandom.current().nextInt(1, 4)))
    }

    override fun getDrops(): Array<Item?> {
        val axe = Item.get(Item.IRON_AXE)
        axe.damage = ThreadLocalRandom.current().nextInt(1, axe.maxDurability)
        return arrayOf(
            axe,
            Item.get(Item.EMERALD, 0, ThreadLocalRandom.current().nextInt(2))
        )
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    protected class VindicatorMeleeAttackExecutor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) :
        MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, entity.memoryStorage!![memory].id)
            entity.setDataFlag(EntityFlag.ANGRY)
            entity.level!!.addLevelSoundEvent(
                entity.position,
                LevelSoundEventPacket.SOUND_ANGRY,
                -1,
                EntityID.Companion.VINDICATOR,
                false,
                false
            )
            Arrays.stream<Entity>(entity.level!!.entities).filter { entity1: Entity ->
                entity1 is EntityPiglin && entity1.position.distance(entity.position) < 16 && entity1.memoryStorage!!
                    .isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)
            }.forEach { entity1: Entity ->
                (entity1 as EntityPiglin).memoryStorage!!
                    .put<Entity>(
                        CoreMemoryTypes.Companion.ATTACK_TARGET, entity.memoryStorage!!
                            .get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET)
                    )
            }
            if (entity.memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is EntityHoglin) {
                entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME, entity.level!!.tick)
            }
        }

        override fun onStop(entity: EntityMob) {
            super.onStop(entity)
            entity.setDataFlag(EntityFlag.ANGRY, false)
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, 0L)
        }

        override fun onInterrupt(entity: EntityMob) {
            super.onInterrupt(entity)
            entity.setDataFlag(EntityFlag.ANGRY, false)
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, 0L)
        }
    }

    fun isJohnny(): Boolean {
        return nameTag == "Johnny" || (namedTag!!.exist("Johnny") && namedTag!!.getBoolean("Johnny"))
    }
}
