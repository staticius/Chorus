package org.chorus.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntityCreakingHeart
import cn.nukkit.blockentity.BlockEntityCreakingHeart.heart
import cn.nukkit.blockentity.BlockEntityCreakingHeart.setLinkedCreaking
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.DoNothingExecutor
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.PlayerStaringSensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelEventGenericPacket
import cn.nukkit.network.protocol.LevelEventPacket
import lombok.Setter
import java.util.*
import java.util.Set
import java.util.concurrent.*
import kotlin.collections.setOf

class EntityCreaking(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.CREAKING
    }

    @Setter
    protected var creakingHeart: BlockEntityCreakingHeart? = null

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(DoNothingExecutor(), EntityCheckEvaluator(CoreMemoryTypes.Companion.STARING_PLAYER), 5, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_CREAKING_AMBIENT),
                    all(RandomSoundEvaluator(), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET)),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                PlayerStaringCreakingSensor(40.0, 70.0, true),
                NearestPlayerCreakingSensor(40.0, 0.0, 0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 1
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        if (namedTag!!.containsCompound("creakingHeart")) {
            val tag = namedTag!!.getCompound("creakingHeart")
            val vec = Vector3(tag.getInt("x").toDouble(), tag.getInt("y").toDouble(), tag.getInt("z").toDouble())
            if (level!!.getBlock(vec, true) is BlockCreakingHeart) {
                heart.getOrCreateBlockEntity().setLinkedCreaking(this)
            }
        }
        super.initEntity()
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.isCancelled) return false
        if (creakingHeart == null) return super.attack(source)
        if (this.isClosed || !this.isAlive) {
            return false
        }
        if (source is EntityDamageByEntityEvent && source.damager !is EntityCreeper) {
            memoryStorage!!.put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, source.damager)
        }
        val storage = memoryStorage
        if (storage != null) {
            storage.put<EntityDamageEvent>(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT, source)
            storage.put<Int>(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, level!!.tick)
        }

        val paleLogs = Arrays.stream<Block>(
            level!!.getCollisionBlocks(
                creakingHeart!!.levelBlock.boundingBox.grow(2.0, 2.0, 2.0)
            )
        ).filter { block: Block? -> block is BlockPaleOakLog }.toArray<Block> { _Dummy_.__Array__() }
        val maxResinSpawn = ThreadLocalRandom.current().nextInt(1, 3)
        var resinSpawned = 0
        logs@ for (log in paleLogs) {
            for (face in BlockFace.entries) {
                val side = log.getSide(face)
                if (side.isAir) {
                    val clump = Block.get(Block.RESIN_CLUMP) as BlockResinClump
                    clump.setPropertyValue(
                        CommonBlockProperties.MULTI_FACE_DIRECTION_BITS,
                        clump.getPropertyValue(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) or (1 shl face.opposite.duswneIndex)
                    )
                    side.level.setBlock(side.position, clump)
                    resinSpawned++
                    if (resinSpawned >= maxResinSpawn) break@logs
                }
            }
        }
        return true
    }

    fun sendParticleTrail() {
        val packet = LevelEventGenericPacket()
        packet.eventId = LevelEventPacket.EVENT_PARTICLE_CREAKING_HEART_TRIAL
        val tag = CompoundTag()
        tag.putInt("CreakingAmount", 1)
        tag.putFloat("CreakingX", position.x.toFloat())
        tag.putFloat("CreakingY", position.y.toFloat())
        tag.putFloat("CreakingZ", position.z.toFloat())
        tag.putInt("HeartAmount", 1)
        tag.putFloat("HeartX", creakingHeart!!.position.x.toFloat())
        tag.putFloat("HeartY", creakingHeart!!.position.y.toFloat())
        tag.putFloat("HeartZ", creakingHeart!!.position.z.toFloat())
        packet.tag = tag
        Server.broadcastPacket(this.viewers.values, packet)
    }

    fun spawnResin() {
    }

    override fun kill() {
        //ToDo: Creaking Death Animation
        super.kill()
        if (creakingHeart != null && creakingHeart!!.isBlockEntityValid) {
            creakingHeart!!.setLinkedCreaking(null)
        }
    }

    override fun saveNBT() {
        if (creakingHeart != null) {
            val tag = CompoundTag()
            tag.putInt("x", creakingHeart!!.position.floorX)
            tag.putInt("y", creakingHeart!!.position.floorY)
            tag.putInt("z", creakingHeart!!.position.floorZ)
            namedTag!!.putCompound("creakingHeart", tag)
        }
        super.saveNBT()
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (!(!level!!.isDay || level!!.isRaining || level!!.isThundering)) {
            this.kill()
        }
        if (creakingHeart != null) {
            if (position.distance(creakingHeart!!.position) > 32) {
                moveTarget = creakingHeart!!.position
                lookTarget = creakingHeart!!.position
            }
            if (memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME)) {
                if (level!!.tick - memoryStorage!!.get<Int>(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME) < 51) {
                    sendParticleTrail()
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun getHeight(): Float {
        return 2.5f
    }

    override fun getWidth(): Float {
        return 1f
    }

    override fun getExperienceDrops(): Int {
        return 0
    }

    override fun updateMovement() {
        super.updateMovement()
        if (creakingHeart != null && creakingHeart!!.isBlockEntityValid) {
            creakingHeart!!.heart!!.updateAroundRedstone(BlockFace.UP, BlockFace.DOWN)
        } else kill()
    }

    private inner class NearestPlayerCreakingSensor(range: Double, minRange: Double, period: Int) :
        NearestPlayerSensor(range, minRange, period) {
        override fun sense(entity: EntityMob) {
            val before = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)
            super.sense(entity)
            val after = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)
            if (before !== after) {
                if (before == null) {
                    entity.level!!.addSound(entity.position, Sound.MOB_CREAKING_ACTIVATE)
                } else if (after == null) {
                    entity.level!!.addSound(entity.position, Sound.MOB_CREAKING_DEACTIVATE)
                }
            }
        }
    }

    private inner class PlayerStaringCreakingSensor(range: Double, triggerDiff: Double, ignoreRotation: Boolean) :
        PlayerStaringSensor(range, triggerDiff, ignoreRotation) {
        override fun sense(entity: EntityMob) {
            val before = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.STARING_PLAYER)
            super.sense(entity)
            val after = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.STARING_PLAYER)
            if (before !== after) {
                if (before == null) {
                    entity.level!!.addSound(entity.position, Sound.MOB_CREAKING_FREEZE)
                } else if (after == null) {
                    entity.level!!.addSound(entity.position, Sound.MOB_CREAKING_UNFREEZE)
                }
            }
        }
    }
}
