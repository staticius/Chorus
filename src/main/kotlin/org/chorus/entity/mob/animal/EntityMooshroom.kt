package org.chorus.entity.mob.animal

import org.chorus.Player
import org.chorus.block.BlockID
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.evaluator.PassByTimeEvaluator
import org.chorus.entity.ai.evaluator.ProbabilityEvaluator
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestFeedingPlayerSensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityMob
import org.chorus.item.*
import org.chorus.level.ParticleEffect
import org.chorus.level.format.IChunk
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author BeYkeRYkt (Nukkit Project)
 */
class EntityMooshroom(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.MOOSHROOM
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
                    FlatRandomRoamExecutor(0.25f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    4,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityMooshroom>(EntityMooshroom::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    3,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.25f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    2,
                    1
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.1f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(NearestFeedingPlayerSensor(8.0, 0.0), NearestPlayerSensor(8.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        if (isBaby()) {
            return 0.45f
        }
        return 0.9f
    }

    override fun getHeight(): Float {
        if (isBaby()) {
            return 0.65f
        }
        return 1.3f
    }

    override fun getOriginalName(): String {
        return "Mooshroom"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.LEATHER), Item.get(ItemID.BEEF))
    }


    override fun initEntity() {
        this.maxHealth = 10
        super.initEntity()
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (super.onInteract(player, item, clickedPos)) {
            return true
        }

        if (item.id === ItemID.SHEARS && item.useOn(this)) {
            this.close()
            //TODO 不同颜色的牛掉落不同的蘑菇
            level!!.dropItem(this.position, Item.get(BlockID.RED_MUSHROOM, 0, 5))
            level!!.addParticleEffect(
                position.add(0.0, this.getHeight().toDouble(), 0.0),
                ParticleEffect.LARGE_EXPLOSION_LEVEL
            )
            val cow = EntityCow(this.locator.chunk, this.namedTag)
            cow.setPosition(this.position)
            cow.setRotation(rotation.yaw, rotation.pitch)
            cow.spawnToAll()
            level!!.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    this,
                    this.vector3, VibrationType.SHEAR
                )
            )
            return true
        } else if (item.id === ItemID.BUCKET && item.damage == 0) {
            item.count--
            player.inventory.addItem(Item.get(ItemID.BUCKET, 1))
            return true
        } else if (item.id === ItemID.BOWL && item.damage == 0) {
            item.count--
            player.inventory.addItem(Item.get(ItemID.MUSHROOM_STEW))
            return true
        }

        return false
    }
}
