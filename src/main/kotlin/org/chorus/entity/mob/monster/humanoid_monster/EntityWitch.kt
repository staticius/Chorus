package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.Player
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.PotionThrowExecutor
import org.chorus.entity.ai.executor.UsePotionExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.entity.mob.EntityMob
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set
import java.util.concurrent.*

/**
 * @author PikyCZ
 */
class EntityWitch(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.WITCH
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_WITCH_AMBIENT), RandomSoundEvaluator(), 2, 1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<IBehavior>(
                Behavior(
                    UsePotionExecutor(0.3f, 30, 20), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME),
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.level!!.tick - memoryStorage!!.get<Int>(
                                CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME
                            ) <= 1
                        }
                    ), 4, 1),
                Behavior(
                    PotionThrowExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 15, true, 30, 20),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    PotionThrowExecutor(CoreMemoryTypes.Companion.NEAREST_GOLEM, 0.3f, 15, true, 30, 20),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_GOLEM),
                    2,
                    1
                ),
                Behavior(
                    PotionThrowExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, true, 30, 20),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(16.0, 0.0, 20),
                NearestEntitySensor(EntityGolem::class.java, CoreMemoryTypes.Companion.NEAREST_GOLEM, 42.0, 0.0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 26
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Witch"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item> {
        val itemId = when (ThreadLocalRandom.current().nextInt(7)) {
            0 -> Item.STICK
            1 -> Item.SPIDER_EYE
            2 -> Item.GLOWSTONE_DUST
            3 -> Item.GUNPOWDER
            4 -> Item.REDSTONE
            5 -> Item.SUGAR
            else -> Item.GLASS_BOTTLE
        }
        return arrayOf(
            Item.get(itemId, 0, ThreadLocalRandom.current().nextInt(5))
        )
    }
}
