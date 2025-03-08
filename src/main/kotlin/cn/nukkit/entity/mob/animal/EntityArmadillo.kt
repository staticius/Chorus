package cn.nukkit.entity.mob.animal

import cn.nukkit.Player
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.executor.armadillo.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestFeedingPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import lombok.Getter
import java.util.Set

class EntityArmadillo(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt) {
    @Getter
    private var rollState = RollState.UNROLLED

    override fun getIdentifier(): String {
        return EntityID.Companion.ARMADILLO
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    UnrollingExecutor(),
                    { entity: EntityMob? -> getRollState() == RollState.ROLLED_UP_UNROLLING }, 8, 1
                ),
                Behavior(
                    PeekExecutor(), any(
                        IBehaviorEvaluator { entity: EntityMob? -> getRollState() == RollState.ROLLED_UP_PEEKING },
                        all(
                            IBehaviorEvaluator { entity: EntityMob? -> getRollState() == RollState.ROLLED_UP_RELAXING },
                            ProbabilityEvaluator(1, 0xFFF)
                        )
                    ), 7, 1
                ),
                Behavior(RollUpExecutor(), RollupEvaluator(), 6, 1),
                Behavior(RelaxingExecutor(), ProbabilityEvaluator(1, 0xFFFF), 5, 1),
                Behavior(
                    ShedExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> getRollState() == RollState.UNROLLED },
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.NEXT_SHED, 0)
                    ), 5, 1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityArmadillo>(EntityArmadillo::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    7,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.4f, true), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob? -> getRollState() == RollState.UNROLLED }
                    ), 2, 1),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100), all(
                        ProbabilityEvaluator(4, 10),
                        IBehaviorEvaluator { entity: EntityMob? -> getRollState() == RollState.UNROLLED }
                    ), 1, 1, 100),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 20, false, -1, true, 40),
                    { entity: EntityMob? -> getRollState() == RollState.UNROLLED }, 1, 1
                )
            ),
            Set.of<ISensor>(NearestFeedingPlayerSensor(8.0, 0.0), NearestPlayerSensor(8.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (player.inventory.getUnclonedItem(player.inventory.heldItemIndex) is ItemBrush) {
            level!!.dropItem(this.position, Item.get(Item.ARMADILLO_SCUTE))
            level!!.addSound(player.position, Sound.MOB_ARMADILLO_BRUSH)
            brush.incDamage(16)
            if (brush.getDamage() >= brush.getMaxDurability()) {
                player.level.addSound(player.position, Sound.RANDOM_BREAK)
                player.inventory.clear(player.inventory.heldItemIndex)
            }
        }
        return super.onInteract(player, item, clickedPos)
    }

    override fun initEntity() {
        this.maxHealth = 12
        super.initEntity()
        setMovementSpeed(0.5f)
        setRollState(RollState.UNROLLED)
        memoryStorage!!.put<Int>(
            CoreMemoryTypes.Companion.NEXT_SHED,
            level!!.tick + Utils.rand(6000, 10800)
        )
    }

    override fun getWidth(): Float {
        if (isBaby()) {
            return 0.42f
        }
        return 0.7f
    }

    override fun getHeight(): Float {
        if (isBaby()) {
            return 0.39f
        }
        return 0.65f
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (getRollState() != RollState.UNROLLED) {
            source.damage = (source.damage - 1) / 2f
        }
        return super.attack(source)
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id == Item.SPIDER_EYE
    }

    fun setRollState(state: RollState) {
        this.rollState = state
        setEnumEntityProperty(PROPERTY_STATE, state.getState())
        sendData(viewers.values.toArray<Player?> { _Dummy_.__Array__() })
        setDataFlag(EntityFlag.BODY_ROTATION_BLOCKED, rollState != RollState.UNROLLED)
    }


    enum class RollState(@field:Getter private val state: String) {
        UNROLLED("unrolled"),
        ROLLED_UP("rolled_up"),
        ROLLED_UP_PEEKING("rolled_up_peeking"),
        ROLLED_UP_RELAXING("rolled_up_relaxing"),
        ROLLED_UP_UNROLLING("rolled_up_unrolling")
    }

    class RollupEvaluator : IBehaviorEvaluator {
        override fun evaluate(entity: EntityMob): Boolean {
            return AnyMatchEvaluator(
                PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 1),
                IBehaviorEvaluator { entity1: EntityMob? ->
                    for (other in entity.level!!.getCollidingEntities(
                        entity.getBoundingBox()!!.grow(7.0, 2.0, 7.0)
                    )) {
                        if (other is EntityMonster) {
                            if (other.isUndead()) return@IBehaviorEvaluator true
                        } else if (other is Player) {
                            if (other.isSprinting()) return@IBehaviorEvaluator true
                        }
                    }
                    false
                }
            ).evaluate(entity)
        }
    }

    companion object {
        private const val PROPERTY_STATE = "minecraft:armadillo_state"
    }
}
