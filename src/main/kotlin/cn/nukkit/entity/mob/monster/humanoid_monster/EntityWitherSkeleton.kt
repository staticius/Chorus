package cn.nukkit.entity.mob.monster.humanoid_monster

import cn.nukkit.block.BlockID
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
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
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.utils.*
import java.util.List
import java.util.Set
import java.util.function.Function

/**
 * @author PikyCZ
 */
class EntityWitherSkeleton(chunk: IChunk?, nbt: CompoundTag?) : EntitySkeleton(chunk, nbt), EntityWalkable,
    EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.WITHER_SKELETON
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(
                    IBehaviorExecutor { entity: EntityMob? ->
                        val storage = memoryStorage
                        if (storage!!.notEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)) return@IBehaviorExecutor false
                        var attackTarget: Entity? = null
                        if (storage.notEmpty(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET) && storage.get<Entity>(
                                CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET
                            ).isAlive
                        ) {
                            attackTarget = storage.get<Entity>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET)
                        }
                        storage.put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, attackTarget)
                        false
                    },
                    { entity: EntityMob? -> true }, 20
                )
            ),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_WITHER_AMBIENT), RandomSoundEvaluator(), 5, 1),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 10, Effect.Companion.get(
                            EffectType.Companion.WITHER
                        ).setDuration(200)
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 4, 1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 10, Effect.Companion.get(
                            EffectType.Companion.WITHER
                        ).setDuration(200)
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }


    //凋零骷髅会攻击距离他16格范围内的玩家、雪傀儡、小海龟、铁傀儡、猪灵或猪灵蛮兵
    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.SNOW_GOLEM, EntityID.Companion.IRON_GOLEM, EntityID.Companion.TURTLE, EntityID.Companion.PIGLIN, EntityID.Companion.PIGLIN_BRUTE -> true
            else -> false
        }
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(5f, 8f, 12f)
        super.initEntity()
        // 判断凋零骷髅是否手持石剑如果没有就给它石剑
        if (this.getItemInHand() !== Item.get(Item.STONE_SWORD)) {
            this.setItemInHand(Item.get(Item.STONE_SWORD))
        }
        // 设置凋零骷髅空闲状态播放空闲声音
        this.setDataProperty(EntityDataTypes.Companion.AMBIENT_SOUND_EVENT_NAME, LevelSoundEventPacket.SOUND_AMBIENT)
    }

    override fun getWidth(): Float {
        return 0.7f
    }

    override fun getHeight(): Float {
        return 2.4f
    }

    override fun getOriginalName(): String {
        return "Wither Skeleton"
    }

    //掉落剑的概率为8.5% 掉落头的概率为2.5%
    override fun getDrops(): Array<Item?> {
        val drops: MutableList<Item> = ArrayList()
        drops.add(Item.get(Item.BONE, 0, Utils.rand(0, 2)))
        if (Utils.rand(0, 2) == 0) {
            drops.add(Item.get(Item.COAL, 0, 1))
        }
        //掉落石剑的概率为8.5%
        if (Utils.rand(0, 200) <= 17) {
            drops.add(Item.get(Item.STONE_SWORD, Utils.rand(0, 131), 1))
        }
        //掉落头的概率为2.5%
        if (Utils.rand(0, 40) == 1) {
            drops.add(Item.get(BlockID.SKULL, 1, 1))
        }
        return drops.toArray(Item.EMPTY_ARRAY)
    }
}
