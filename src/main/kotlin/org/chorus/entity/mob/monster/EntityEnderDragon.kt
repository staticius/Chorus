package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LiftController
import org.chorus.entity.ai.controller.SpaceMoveController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckEmptyEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.enderdragon.CircleMovementExecutor
import org.chorus.entity.ai.executor.enderdragon.PerchingExecutor
import org.chorus.entity.ai.executor.enderdragon.StrafeExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.data.Node
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.route.posevaluator.IPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.item.EntityEnderCrystal
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.*
import org.chorus.network.protocol.types.EntityLink
import org.chorus.plugin.InternalPlugin
import java.util.*
import java.util.Set
import kotlin.collections.ArrayList
import kotlin.collections.first
import kotlin.collections.iterator
import kotlin.collections.setOf
import kotlin.collections.toTypedArray
import kotlin.math.cos
import kotlin.math.sin

class EntityEnderDragon(chunk: IChunk?, nbt: CompoundTag) : EntityBoss(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.ENDER_DRAGON
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_ENDERDRAGON_GROWL), RandomSoundEvaluator(), 2, 1)
            ),
            Set.of<IBehavior>(
                Behavior(
                    PerchingExecutor(),
                    { entity: EntityMob? -> memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.FORCE_PERCHING) },
                    5,
                    1
                ),
                Behavior(
                    StrafeExecutor(), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.LAST_ENDER_CRYSTAL_DESTROY)
                    ), 4, 1
                ),
                Behavior(
                    CircleMovementExecutor(CoreMemoryTypes.Companion.STAY_NEARBY, 1f, true, 82, 12, 5), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.STAY_NEARBY),
                        MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY)
                    ), 3, 1
                ),
                Behavior(
                    CircleMovementExecutor(CoreMemoryTypes.Companion.STAY_NEARBY, 1f, true, 48, 8, 4), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.STAY_NEARBY),
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY)
                    ), 2, 1
                )
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(512.0, 0.0, 20),
                NearestEntitySensor(
                    EntityEnderCrystal::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    192.0,
                    0.0,
                    10
                )
            ),
            Set.of<IController>(SpaceMoveController(), LookController(), LiftController()),
            EnderDragonRouteFinder(EnderDragonPosEvaluator(), this),
            this
        )
    }

    override fun createAddEntityPacket(): DataPacket {
        return AddActorPacket(
            targetActorID = this.uniqueId,
            targetRuntimeID = this.runtimeId,
            actorType = this.getIdentifier(),
            position = this.position.asVector3f(),
            velocity = this.motion.asVector3f(),
            rotation = this.rotation.asVector2f(),
            yHeadRotation = this.rotation.yaw.toFloat(),
            yBodyRotation = this.rotation.yaw.toFloat(),
            attributeList = run {
                this.attributes.values.add(
                    Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(200f).setValue(200f)
                )
                this.attributes.values.toTypedArray()
            },
            actorData = this.entityDataMap,
            syncedProperties = this.propertySyncData(),
            actorLinks = Array(passengers.size) { i ->
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

    override fun attack(source: EntityDamageEvent): Boolean {
        if (deathTicks != -1) return false
        when (source.cause) {
            DamageCause.SUFFOCATION, DamageCause.MAGIC -> {
                return false
            }
        }
        return super.attack(source)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        //Hack -> Ensures that Ender Dragon is always ticked.
        level!!.scheduler.scheduleTask(
            InternalPlugin.INSTANCE
        ) { this.scheduleUpdate() }
        if (deathTicks != -1) {
            if (deathTicks <= 0) {
                kill()
            } else deathTicks--
            return true
        }
        if (currentTick % 2 == 0) {
            if (currentTick % (if (position.toHorizontal().distance(Vector2.ZERO) < 1) 10 else 20) == 0) {
                level!!.addLevelSoundEvent(
                    this.position, LevelSoundEventPacket.SOUND_FLAP, -1,
                    this.getIdentifier(), false, false
                )
            }
            for (e in level!!.entities) {
                if (e is EntityEnderCrystal) {
                    if (e.position.distance(this.position) <= 28) {
                        val health = this.getHealth()
                        if (!(health > this.maxHealth) && health != 0f) {
                            this.heal(0.2f)
                        }
                    }
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    private var deathTicks = -1

    override fun kill() {
        if (deathTicks == -1) {
            deathTicks = 190
            level!!.addLevelSoundEvent(
                this.position,
                LevelSoundEventPacket.SOUND_DEATH,
                -1,
                getIdentifier(),
                false,
                false
            )
            val packet = EntityEventPacket()
            packet.event = EntityEventPacket.ENDER_DRAGON_DEATH
            packet.eid = getRuntimeID()
            Server.broadcastPacket(viewers.values, packet)
            isImmobile = true
        } else {
            super.kill()
            close()
            if (!isRevived()) {
                level!!.setBlock(
                    Vector3(0.0, (level!!.getHighestBlockAt(Vector2.ZERO) + 1).toDouble(), 0.0), Block.get(
                        Block.DRAGON_EGG
                    )
                )
            }
            for (i in -2..2) {
                for (j in -1..1) {
                    if (!(i == 0 && j == 0)) {
                        level!!.setBlock(Vector3(i.toDouble(), 63.0, j.toDouble()), Block.get(Block.END_PORTAL))
                        level!!.setBlock(Vector3(j.toDouble(), 63.0, i.toDouble()), Block.get(Block.END_PORTAL))
                    }
                }
            }
            for (i in 0..19) {
                val origin = Vector3.ZERO
                val angleIncrement = 360.0 / 20
                val angle = Math.toRadians(i * angleIncrement)
                val particleX = origin.getX() + cos(angle) * 96
                val particleZ = origin.getZ() + sin(angle) * 96
                val dest =
                    level!!.getBlock(Vector3(particleX, 75.0, particleZ))
                if (dest !is BlockEndGateway) {
                    Arrays.stream(BlockFace.entries.toTypedArray()).forEach { face: BlockFace? ->
                        level!!.setBlock(
                            dest.up().getSide(face).position, Block.get(
                                Block.BEDROCK
                            )
                        )
                    }
                    Arrays.stream(BlockFace.entries.toTypedArray()).forEach { face: BlockFace? ->
                        level!!.setBlock(
                            dest.down().getSide(face).position, Block.get(
                                Block.BEDROCK
                            )
                        )
                    }
                    level!!.setBlock(dest.position, Block.get(Block.END_GATEWAY))
                    break
                } else continue
            }
        }
    }

    override fun getWidth(): Float {
        return 13f
    }

    override fun getHeight(): Float {
        return 4f
    }

    public override fun initEntity() {
        this.diffHandDamage = floatArrayOf(6f, 10f, 15f)
        this.maxHealth = 200
        super.initEntity()
        memoryStorage!!.set<Vector3>(CoreMemoryTypes.Companion.STAY_NEARBY, Vector3(0.0, 84.0, 0.0))
        isActive = false
        noClip = true
    }

    override fun playerApplyNameTag(player: Player, item: Item): Boolean {
        return false
    }

    override fun getOriginalName(): String {
        return "Ender Dragon"
    }

    override fun isBoss(): Boolean {
        return true
    }

    override fun addBossbar(player: Player) {
        player.dataPacket(BossEventPacket(
            targetActorID = this.runtimeId,
            eventType = BossEventPacket.EventType.ADD,
            eventData = BossEventPacket.EventType.Companion.AddData(
                name = this.getName(),
                filteredName = this.getName(),
                color = 5,
                healthPercent = health / maxHealth,
                darkenScreen = 0,
                overlay = 0
            )
        ))
    }

    override fun getExperienceDrops(): Int {
        return if (isRevived()) 500 else 12000
    }

    private inner class EnderDragonRouteFinder(blockEvaluator: IPosEvaluator?, entity: EntityMob) :
        SimpleSpaceAStarRouteFinder(blockEvaluator, entity) {
        override fun search(): Boolean {
            val superRes = super.search()
            if (superRes && memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.MOVE_TARGET)) {
                this.nodes = ArrayList<Node?>(setOf<Node>(nodes.first()))
                nodes.add(Node(memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.MOVE_TARGET), null, 0, 0))
            }
            return superRes
        }
    }

    private inner class EnderDragonPosEvaluator : FlyingPosEvaluator() {
        override fun isPassable(entity: EntityMob, vector3: Vector3): Boolean {
            return true
        }
    }

    override fun move(dx: Double, dy: Double, dz: Double): Boolean {
        val superRes = super.move(dx, dy, dz)
        if (superRes) {
            Arrays.stream(level!!.getCollisionBlocks(getBoundingBox())).filter { block: Block? ->
                canBreakBlock(
                    block!!
                )
            }.forEach { block: Block? -> level!!.breakBlock(block) }
        }
        return superRes
    }

    fun isRevived(): Boolean {
        return if (namedTag!!.contains("Revived")) {
            namedTag!!.getBoolean("Revived")
        } else false
    }

    protected inner class LookController : IController {
        override fun control(entity: EntityMob): Boolean {
            val target = entity.memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.LOOK_TARGET)
            val toPlayerVector = Vector3(
                entity.position.x - target.x,
                entity.position.y - target.y,
                entity.position.z - target.z
            ).normalize()
            entity.headYaw = (BVector3.getYawFromVector(toPlayerVector))
            entity.rotation.yaw = (BVector3.getYawFromVector(toPlayerVector))
            entity.rotation.pitch = (BVector3.getPitchFromVector(toPlayerVector))
            return true
        }
    }
}