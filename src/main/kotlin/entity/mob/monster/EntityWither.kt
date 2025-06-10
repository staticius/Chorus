package org.chorus_oss.chorus.entity.mob.monster

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.block.BlockBedrock
import org.chorus_oss.chorus.block.BlockSoulSand
import org.chorus_oss.chorus.block.BlockWitherSkeletonSkull
import org.chorus_oss.chorus.entity.*
import org.chorus_oss.chorus.entity.ai.behavior.Behavior
import org.chorus_oss.chorus.entity.ai.behavior.IBehavior
import org.chorus_oss.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus_oss.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus_oss.chorus.entity.ai.controller.IController
import org.chorus_oss.chorus.entity.ai.controller.LiftController
import org.chorus_oss.chorus.entity.ai.controller.LookController
import org.chorus_oss.chorus.entity.ai.controller.SpaceMoveController
import org.chorus_oss.chorus.entity.ai.evaluator.*
import org.chorus_oss.chorus.entity.ai.executor.*
import org.chorus_oss.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus_oss.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus_oss.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus_oss.chorus.entity.ai.sensor.ISensor
import org.chorus_oss.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus_oss.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus_oss.chorus.entity.data.EntityDataTypes
import org.chorus_oss.chorus.entity.data.EntityFlag
import org.chorus_oss.chorus.entity.mob.EntityMob
import org.chorus_oss.chorus.event.entity.EntityDamageEvent
import org.chorus_oss.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus_oss.chorus.event.entity.EntityExplosionPrimeEvent
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemID
import org.chorus_oss.chorus.level.Explosion
import org.chorus_oss.chorus.level.GameRule
import org.chorus_oss.chorus.level.Sound
import org.chorus_oss.chorus.level.format.IChunk
import org.chorus_oss.chorus.math.BlockFace
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.nbt.tag.FloatTag
import org.chorus_oss.chorus.nbt.tag.ListTag
import org.chorus_oss.chorus.network.protocol.*
import org.chorus_oss.chorus.network.protocol.types.EntityLink
import java.util.function.Function

class EntityWither(chunk: IChunk?, nbt: CompoundTag) : EntityBoss(chunk, nbt), EntityFlyable, EntitySmite {
    override fun getEntityIdentifier(): String {
        return EntityID.WITHER
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_WITHER_AMBIENT), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 11, 1),
                Behavior(
                    WitherDashExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 1f, true, 64f, 0f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        any(
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_DASH, 400),
                            IBehaviorEvaluator { entity: EntityMob? -> getDataFlag(EntityFlag.CAN_DASH) }
                        ),
                        DistanceEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET, 65.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> health <= maxHealth / 2f },
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 10, 1),
                Behavior(
                    WitherDashExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 1f, true, 64f, 0f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        any(
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_DASH, 400),
                            IBehaviorEvaluator { entity: EntityMob? -> getDataFlag(EntityFlag.CAN_DASH) }
                        ),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 65.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> health <= maxHealth / 2f },
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 9, 1),
                Behavior(
                    WitherDashExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        1f,
                        true,
                        64f,
                        0f
                    ),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        any(
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_DASH, 400),
                            IBehaviorEvaluator { entity: EntityMob? -> getDataFlag(EntityFlag.CAN_DASH) }
                        ),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 65.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> health <= maxHealth / 2f },
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ),
                    8,
                    1),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.7f, true, 64f, 16f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET, 65.0, 17.0),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }), 7, 1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.7f, true, 64f, 16f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 65.0, 17.0),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 6, 1),
                Behavior(
                    MoveToTargetExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.7f,
                        true,
                        64f,
                        16f
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 65.0, 17.0),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 5, 1),
                Behavior(
                    WitherShootExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 100),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 4, 1),
                Behavior(
                    WitherShootExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 100),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 3, 1),
                Behavior(
                    WitherShootExecutor(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 100),
                        IBehaviorEvaluator { entity: EntityMob? -> age >= 200 }
                    ), 2, 1),
                Behavior(
                    SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10),
                    { entity: EntityMob? -> age >= 200 }, 1, 1
                )
            ),
            setOf<ISensor>(
                NearestPlayerSensor(64.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 64.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) })
            ),
            setOf<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    private var exploded = false
    private var deathTicks = -1

    override fun kill() {
        if (deathTicks == -1) {
            deathTicks = 190
            level!!.addLevelSoundEvent(
                this.position,
                LevelSoundEventPacket.SOUND_DEATH,
                -1,
                EntityID.WITHER,
                false,
                false
            )
            val packet = EntityEventPacket()
            packet.event = EntityEventPacket.DEATH_ANIMATION
            packet.eid = getRuntimeID()
            Server.broadcastPacket(viewers.values, packet)
            setImmobile(true)
        } else {
            if (!this.exploded && this.lastDamageCause != null && DamageCause.SUICIDE != lastDamageCause!!.cause) {
                this.exploded = true
                this.explode()
            }
            super.kill()
        }
    }

    override fun setHealthSafe(health: Float) {
        val healthBefore = this.health
        val halfHealth = maxHealth / 2f
        super.setHealthSafe(health)
        if (health <= halfHealth && healthBefore > halfHealth) {
            if (!isInvulnerable()) {
                this.explode()
                setInvulnerable(176)
            }
        }
    }

    fun setInvulnerable(ticks: Int) {
        this.setDataProperty(EntityDataTypes.Companion.WITHER_INVULNERABLE_TICKS, ticks)
    }

    fun isInvulnerable(): Boolean {
        return getDataProperty<Int>(EntityDataTypes.Companion.WITHER_INVULNERABLE_TICKS) > 0
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (age < 200 || deathTicks != -1) return false
        if (source.cause == DamageCause.ENTITY_EXPLOSION) {
            return false
        }
        return super.attack(source)
    }

    override fun getWidth(): Float {
        return 1.0f
    }

    override fun getHeight(): Float {
        return 3.0f
    }

    override fun createAddEntityPacket(): DataPacket {
        return AddActorPacket(
            targetActorID = this.uniqueId,
            targetRuntimeID = this.runtimeId,
            actorType = this.getEntityIdentifier(),
            position = this.position.asVector3f(),
            velocity = this.motion.asVector3f(),
            rotation = this.rotation.asVector2f(),
            yHeadRotation = this.rotation.yaw.toFloat(),
            yBodyRotation = this.rotation.yaw.toFloat(),
            attributeList = run {
                this.attributes.values.add(
                    Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(getMaxDiffHealth().toFloat())
                        .setValue(getMaxDiffHealth().toFloat())
                )
                this.attributes.values.toList()
            },
            actorData = this.entityDataMap,
            syncedProperties = this.propertySyncData(),
            actorLinks = List(passengers.size) { i ->
                EntityLink(
                    this.getRuntimeID(),
                    passengers[i].getRuntimeID(),
                    if (i == 0) EntityLink.Type.RIDER else EntityLink.Type.PASSENGER,
                    immediate = false,
                    riderInitiated = false
                )
            }
        )
    }

    private fun getMaxDiffHealth(): Int {
        return when (Server.instance.getDifficulty()) {
            2 -> 450
            3 -> 600
            else -> 300
        }
    }

    override fun initEntity() {
        this.maxHealth = getMaxDiffHealth()
        super.initEntity()
        this.blockBreakSound = Sound.MOB_WITHER_BREAK_BLOCK
        this.setInvulnerable(200)
        this.setHealthSafe(1f)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (!closed) {
            if (deathTicks != -1) {
                if (deathTicks <= 0) {
                    kill()
                } else deathTicks--
            }
            if (isInvulnerable()) {
                this.setDataProperty(
                    EntityDataTypes.Companion.WITHER_INVULNERABLE_TICKS, getDataProperty<Int>(
                        EntityDataTypes.Companion.WITHER_INVULNERABLE_TICKS
                    ) - 1
                )
            }
            if (this.age == 200) {
                this.explode()
                setHealthSafe(maxHealth.toFloat())
                level!!.addSound(this.position, Sound.MOB_WITHER_SPAWN)
            } else if (age < 200) {
                heal(maxHealth / 200f)
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun attackTarget(entity: Entity): Boolean {
        if (entity is EntityWither) return false
        return entity is EntityMob
    }

    override fun addBossbar(player: Player) {
        player.dataPacket(
            BossEventPacket(
                targetActorID = this.runtimeId,
                eventType = BossEventPacket.EventType.ADD,
                eventData = BossEventPacket.EventType.Companion.AddData(
                    name = this.getEntityName(),
                    filteredName = this.getEntityName(),
                    color = 6,
                    darkenScreen = 1,
                    healthPercent = 0f,
                    overlay = 0
                )
            )
        )
    }

    override fun getOriginalName(): String {
        return "Wither"
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun isBoss(): Boolean {
        return true
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.NETHER_STAR))
    }

    override fun getExperienceDrops(): Int {
        return 50
    }

    override fun move(dx: Double, dy: Double, dz: Double): Boolean {
        if ((age % 40 == 0 || getDataFlag(EntityFlag.CAN_DASH) && age > 200)) {
            val blocks = level!!.getCollisionBlocks(getBoundingBox().grow(1.0, 1.0, 1.0))
            if (blocks.size > 0) {
                if (blockBreakSound != null) level!!.addSound(this.position, blockBreakSound!!)
                for (collisionBlock in blocks) {
                    if (collisionBlock !is BlockBedrock) {
                        level!!.breakBlock(collisionBlock)
                    }
                }
            }
        }
        return super.move(dx, dy, dz)
    }

    private fun explode() {
        val ev = EntityExplosionPrimeEvent(this, 7.0)
        Server.instance.pluginManager.callEvent(ev)
        if (!ev.cancelled) {
            val explosion = Explosion(this.locator, ev.force.toFloat().toDouble(), this)
            if (ev.isBlockBreaking && level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA()
            }
            explosion.explodeB()
        }
    }

    companion object {
        @JvmStatic
        fun checkAndSpawnWither(block: Block): Boolean {
            var check = block
            var skullFace: BlockFace? = null
            if (block.level.gameRules.getBoolean(GameRule.DO_MOB_SPAWNING)) {
                for (face in setOf<BlockFace>(BlockFace.UP, BlockFace.NORTH, BlockFace.EAST)) {
                    val skulls = BooleanArray(5)
                    ints@ for (i in -2..2) {
                        skulls[i + 2] = block.getSide(face, i) is BlockWitherSkeletonSkull
                    }
                    var inrow = 0
                    for (i in skulls.indices) {
                        if (skulls[i]) {
                            inrow++
                            if (inrow == 2) check = block.getSide(face, i - 2)
                        } else if (inrow < 3) {
                            inrow = 0
                        }
                    }
                    if (inrow >= 3) {
                        skullFace = face
                    }
                }
                if (skullFace == null) return false
                if (check is BlockWitherSkeletonSkull) {
                    faces@ for (blockFace in BlockFace.entries) {
                        for (i in 1..2) {
                            if (check.getSide(blockFace, i) !is BlockSoulSand) {
                                continue@faces
                            }
                        }
                        faces1@ for (face in setOf<BlockFace>(BlockFace.UP, BlockFace.NORTH, BlockFace.EAST)) {
                            for (i in -1..1) {
                                if (check.getSide(blockFace).getSide(face, i) !is BlockSoulSand) {
                                    continue@faces1
                                }
                            }

                            for (i in 0..2) {
                                val location = check.getSide(blockFace, i)
                                location.level.breakBlock(location)
                            }
                            for (i in -1..1) {
                                val location = check.getSide(blockFace).getSide(face, i)
                                location.level.breakBlock(location)
                                location.level.breakBlock(location.getSide(blockFace.getOpposite()))
                            }
                            val pos = check.getSide(blockFace, 2)
                            val nbt = CompoundTag()
                                .putList(
                                    "Pos", ListTag<FloatTag>()
                                        .add(FloatTag(pos.position.x + 0.5))
                                        .add(FloatTag(pos.position.y))
                                        .add(FloatTag(pos.position.z + 0.5))
                                )
                                .putList(
                                    "Motion", ListTag<FloatTag>()
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                )
                                .putList(
                                    "Rotation", ListTag<FloatTag>()
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                )

                            val wither = createEntity(
                                EntityID.WITHER,
                                check.level.getChunk(check.position.chunkX, check.position.chunkZ),
                                nbt
                            )
                            wither?.spawnToAll()
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}
