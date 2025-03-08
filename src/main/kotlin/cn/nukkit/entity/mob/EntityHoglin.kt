package cn.nukkit.entity.mob

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.BlockID
import cn.nukkit.block.BlockPortal
import cn.nukkit.block.BlockRespawnAnchor
import cn.nukkit.block.BlockWarpedFungus
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.BlockSensor
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.utils.*
import java.util.Set

/**
 * @author Erik Miller | EinBexiii
 */
class EntityHoglin(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.HOGLIN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ), 1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    EntityBreedingExecutor<EntityHoglin>(EntityHoglin::class.java, 16, 100, 0.5f),
                    IBehaviorEvaluator { entity: EntityMob ->
                        entity.getMemoryStorage()!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE)
                    }, 9, 1
                ),
                Behavior(
                    HoglinTransformExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob -> entity.level!!.getDimension() != Level.DIMENSION_NETHER },
                        IBehaviorEvaluator { entity: EntityMob? -> !isImmobile() },
                        IBehaviorEvaluator { entity: EntityMob -> !entity.namedTag!!.getBoolean("IsImmuneToZombification") }
                    ), 8, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_HOGLIN_ANGRY, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isAngry() }), 7, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_HOGLIN_AMBIENT, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isAngry() }), 6, 1
                ),
                Behavior(
                    HoglinMeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, 40, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        not(IBehaviorEvaluator { entity: EntityMob? ->
                            getMemoryStorage()!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is Player && isBreedingItem(
                                player.getInventory().getItemInHand()
                            )
                        })
                    ), 5, 1
                ),
                Behavior(
                    HoglinMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.5f, 40, false, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        not(IBehaviorEvaluator { entity: EntityMob? ->
                            getMemoryStorage()!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER) is Player && isBreedingItem(
                                player.getInventory().getItemInHand()
                            )
                        })
                    ), 4, 1
                ),
                Behavior(
                    HoglinFleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK)
                    ), 3, 1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.PARENT, 0.7f, true, 64f, 3f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.PARENT),
                        DistanceEvaluator(CoreMemoryTypes.Companion.PARENT, 5.0),
                        IBehaviorEvaluator { entity: EntityMob? -> isBaby() }
                    ), 2, 1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                BlockSensor(BlockPortal::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 8, 2, 20),
                BlockSensor(BlockWarpedFungus::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 8, 2, 20),
                BlockSensor(BlockRespawnAnchor::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 8, 2, 20)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    fun isAngry(): Boolean {
        return getDataFlag(EntityFlag.ANGRY)
    }

    override fun initEntity() {
        this.setMaxHealth(40)
        this.diffHandDamage = floatArrayOf(1f, 1f, 1f)
        super.initEntity()
    }

    override fun getDiffHandDamage(): FloatArray? {
        if (isBaby()) {
            return super.getDiffHandDamage()
        } else return floatArrayOf(
            Utils.rand(2.5f, 5f),
            Utils.rand(3f, 8f),
            Utils.rand(4.5f, 12f),
        )
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.85f
        }
        return 1.4f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.85f
        }
        return 1.4f
    }

    fun isBreedingItem(item: Item): Boolean {
        return item.getId() == BlockID.CRIMSON_FUNGUS
    }

    override fun getOriginalName(): String {
        return "Hoglin"
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get((if (this.isOnFire()) Item.COOKED_PORKCHOP else Item.PORKCHOP), 0, Utils.rand(1, 3)))
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        val superResult: Boolean = super.onInteract(player, item, clickedPos)
        if (isBreedingItem(item)) {
            getMemoryStorage()!!.put<Player>(CoreMemoryTypes.Companion.LAST_FEED_PLAYER, player)
            getMemoryStorage()!!.put<Int>(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, level!!.getTick())
            sendBreedingAnimation(item)
            item.count--
            return player.getInventory().setItemInHand(item) && superResult
        }
        return superResult
    }

    protected fun sendBreedingAnimation(item: Item) {
        val pk: EntityEventPacket = EntityEventPacket()
        pk.event = EntityEventPacket.EATING_ITEM
        pk.eid = this.getId()
        pk.data = item.getFullId()
        Server.broadcastPacket(getViewers().values, pk)
    }

    override fun getExperienceDrops(): Int {
        return if (isBaby()) 0 else Utils.rand(1, 3)
    }

    protected class HoglinFleeFromTargetExecutor(memory: MemoryType<out IVector3?>) :
        FleeFromTargetExecutor(memory, 0.5f, true, 8f) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            if (entity.position.distance(entity.getMemoryStorage()!!.get(getMemory()).getVector3()) < 8) {
                entity.level!!.addSound(entity.position, Sound.MOB_HOGLIN_RETREAT)
            }
        }
    }

    protected class HoglinMeleeAttackExecutor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) :
        MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            entity.setDataProperty(
                EntityDataTypes.Companion.TARGET_EID,
                entity.getMemoryStorage()!!.get(memory).getId()
            )
            entity.setDataFlag(EntityFlag.ANGRY)
            entity.level!!.addLevelSoundEvent(
                entity.position,
                LevelSoundEventPacket.SOUND_ANGRY,
                -1,
                EntityID.Companion.HOGLIN,
                false,
                false
            )
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
}
