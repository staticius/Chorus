package cn.nukkit.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.PotionThrowExecutor
import cn.nukkit.entity.ai.executor.UsePotionExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
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

    override fun getDrops(): Array<Item?> {
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
