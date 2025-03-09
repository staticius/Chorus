package org.chorus.entity.item

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.projectile.SlenderProjectile
import org.chorus.event.entity.EntityDamageByChildEntityEvent
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.event.entity.ProjectileHitEvent
import org.chorus.event.player.PlayerFishEvent
import org.chorus.item.*
import org.chorus.item.enchantment.Enchantment
import org.chorus.item.randomitem.Fishing
import org.chorus.level.MovingObjectPosition
import org.chorus.level.format.IChunk
import org.chorus.level.particle.BubbleParticle
import org.chorus.level.particle.WaterParticle
import org.chorus.math.*
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.AddEntityPacket
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.EntityEventPacket
import java.util.concurrent.*
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @author PetteriM1
 */
class EntityFishingHook @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag?, shootingEntity: Entity? = null) :
    SlenderProjectile(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.FISHING_HOOK
    }

    var waitChance: Int = 120
    var waitTimer: Int = 240
    var attracted: Boolean = false
    var attractTimer: Int = 0
    var caught: Boolean = false
    var caughtTimer: Int = 0
    var canCollide: Boolean = true

    var fish: Vector3? = null

    @JvmField
    var rod: Item? = null

    init {
        // https://github.com/PowerNukkit/PowerNukkit/issues/267
        if (this.age > 0) {
            this.close()
        }
    }

    override fun getLength(): Float {
        return 0.2f
    }

    public override fun getGravity(): Float {
        return 0.05f
    }

    public override fun getDrag(): Float {
        return 0.04f
    }

    override fun canCollide(): Boolean {
        return this.canCollide
    }

    override fun onUpdate(currentTick: Int): Boolean {
        var hasUpdate: Boolean
        val target: Long = getDataProperty<Long>(EntityDataTypes.Companion.TARGET_EID!!)
        if (target != 0L) {
            val entity: Entity? = level!!.getEntity(target)
            if (entity == null || !entity.isAlive()) {
                setDataProperty(EntityDataTypes.Companion.TARGET_EID, 0L)
            } else {
                val offset: Vector3f? = entity.getMountedOffset(this)
                setPosition(
                    Vector3(
                        entity.position.x + offset!!.south,
                        entity.position.y + offset.up,
                        entity.position.z + offset.west
                    )
                )
            }
            return false
        }

        hasUpdate = super.onUpdate(currentTick)

        val inWater: Boolean = this.isInsideOfWater()
        if (inWater) { //防止鱼钩沉底 水中的阻力
            motion.x = 0.0
            motion.y -= getGravity() * -0.04
            motion.z = 0.0
            hasUpdate = true
        }

        if (inWater) {
            if (this.waitTimer == 240) {
                this.waitTimer = this.waitChance shl 1
            } else if (this.waitTimer == 360) {
                this.waitTimer = this.waitChance * 3
            }
            if (!this.attracted) {
                if (this.waitTimer > 0) {
                    --this.waitTimer
                }
                if (this.waitTimer == 0) {
                    val random: ThreadLocalRandom = ThreadLocalRandom.current()
                    if (random.nextInt(100) < 90) {
                        this.attractTimer = (random.nextInt(40) + 20)
                        this.spawnFish()
                        this.caught = false
                        this.attracted = true
                    } else {
                        this.waitTimer = this.waitChance
                    }
                }
            } else if (!this.caught) {
                if (this.attractFish()) {
                    this.caughtTimer = (ThreadLocalRandom.current().nextInt(20) + 30)
                    this.fishBites()
                    this.caught = true
                }
            } else {
                if (this.caughtTimer > 0) {
                    --this.caughtTimer
                }
                if (this.caughtTimer == 0) {
                    this.attracted = false
                    this.caught = false
                    this.waitTimer = this.waitChance * 3
                }
            }
        }
        return hasUpdate
    }

    override fun updateMotion() {
        //正确的浮力
        if (this.isInsideOfWater() && this.getY() < this.getWaterHeight() - 2) {
            motion.x = 0.0
            motion.y += getGravity().toDouble()
            motion.z = 0.0
        } else if (this.isInsideOfWater() && this.getY() >= this.getWaterHeight() - 2) { //防止鱼钩上浮超出水面
            motion.x = 0.0
            motion.z = 0.0
            motion.y = 0.0
        } else { //处理不在水中的情况
            super.updateMotion()
        }
    }

    fun getWaterHeight(): Int {
        for (y in position.getFloorY()..level!!.getMaxHeight()) {
            val id: String =
                level!!.getBlockIdAt(position.getFloorX(), y, position.getFloorZ())
            if (id == Block.AIR) {
                return y
            }
        }
        return position.getFloorY()
    }

    fun fishBites() {
        val viewers: Collection<Player> = getViewers().values

        val pk: EntityEventPacket = EntityEventPacket()
        pk.eid = this.getId()
        pk.event = EntityEventPacket.FISH_HOOK_HOOK
        Server.broadcastPacket(viewers, pk)

        val bubblePk: EntityEventPacket = EntityEventPacket()
        bubblePk.eid = this.getId()
        bubblePk.event = EntityEventPacket.FISH_HOOK_BUBBLE
        Server.broadcastPacket(viewers, bubblePk)

        val teasePk: EntityEventPacket = EntityEventPacket()
        teasePk.eid = this.getId()
        teasePk.event = EntityEventPacket.FISH_HOOK_TEASE
        Server.broadcastPacket(viewers, teasePk)

        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        for (i in 0..4) {
            level!!.addParticle(
                BubbleParticle(
                    position.setComponents(
                        position.x + random.nextDouble() * 0.5 - 0.25,
                        getWaterHeight().toDouble(),
                        position.z + random.nextDouble() * 0.5 - 0.25
                    )
                )
            )
        }
    }

    fun spawnFish() {
        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        this.fish = Vector3(
            position.x + (random.nextDouble() * 1.2 + 1) * (if (random.nextBoolean()) -1 else 1),
            getWaterHeight().toDouble(),
            position.z + (random.nextDouble() * 1.2 + 1) * (if (random.nextBoolean()) -1 else 1)
        )
    }

    fun attractFish(): Boolean {
        val multiply: Double = 0.1
        fish!!.setComponents(
            fish!!.x + (position.x - fish!!.x) * multiply,
            fish!!.y,
            fish!!.z + (position.z - fish!!.z) * multiply
        )
        if (ThreadLocalRandom.current().nextInt(100) < 85) {
            level!!.addParticle(WaterParticle(this.fish))
        }
        val dist: Double = abs(
            sqrt(position.x * position.x + position.z * position.z) - sqrt(
                fish!!.x * fish!!.x + fish!!.z * fish!!.z
            )
        )
        return dist < 0.15
    }

    fun reelLine() {
        if (shootingEntity is Player && this.caught) {
            val item: Item = Fishing.getFishingResult(this.rod)
            val experience: Int = ThreadLocalRandom.current().nextInt(3) + 1
            val pos: Vector3 = Vector3(
                position.x,
                getWaterHeight().toDouble(), position.z
            ) //实体生成在水面上
            val motion: Vector3 = shootingEntity.position.subtract(pos).multiply(0.1)
            motion.y += sqrt(shootingEntity.position.distance(pos)) * 0.08

            val event: PlayerFishEvent = PlayerFishEvent(shootingEntity, this, item, experience, motion)
            getServer()!!.pluginManager.callEvent(event)

            if (!event.isCancelled) {
                val itemEntity: EntityItem? = Entity.Companion.createEntity(
                    EntityID.Companion.ITEM,
                    level!!.getChunk(
                        position.x.toInt() shr 4,
                        position.z.toInt() shr 4, true
                    ),
                    Entity.Companion.getDefaultNBT(
                        pos,
                        event.motion, ThreadLocalRandom.current().nextFloat() * 360,
                        0f
                    ).putCompound("Item", NBTIO.putItemHelper(event.loot))
                        .putShort("Health", 5)
                        .putShort("PickupDelay", 1)
                ) as EntityItem?

                if (itemEntity != null) {
                    itemEntity.setOwner(shootingEntity.getName())
                    itemEntity.spawnToAll()
                    shootingEntity.level.dropExpOrb(shootingEntity.position, event.experience)
                }
            }
        } else if (this.shootingEntity != null) {
            val eid: Long = this.getDataProperty<Long>(EntityDataTypes.Companion.TARGET_EID!!)
            val targetEntity: Entity? = level!!.getEntity(eid)
            if (targetEntity != null && targetEntity.isAlive()) { // 钓鱼竿收杆应该牵引被钓生物
                targetEntity.setMotion(
                    shootingEntity!!.position.subtract(targetEntity.position).divide(8.0).add(0.0, 0.3, 0.0)
                )
            }
        }
        this.close()
    }

    override fun createAddEntityPacket(): DataPacket {
        val pk: AddEntityPacket = AddEntityPacket()
        pk.entityRuntimeId = this.getId()
        pk.entityUniqueId = this.getId()
        pk.type = getNetworkId()
        pk.x = position.x.toFloat()
        pk.y = position.y.toFloat()
        pk.z = position.z.toFloat()
        pk.speedX = motion.x.toFloat()
        pk.speedY = motion.y.toFloat()
        pk.speedZ = motion.z.toFloat()
        pk.yaw = rotation.yaw.toFloat()
        pk.pitch = rotation.pitch.toFloat()

        var ownerId: Long = -1
        if (this.shootingEntity != null) {
            ownerId = shootingEntity!!.getId()
        }
        entityDataMap.put(EntityDataTypes.Companion.OWNER_EID, ownerId)
        pk.entityData = entityDataMap
        return pk
    }


    override fun onCollideWithEntity(entity: Entity) {
        server!!.pluginManager.callEvent(ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)))
        val damage: Float = getResultDamage().toFloat()

        val ev: EntityDamageEvent
        if (this.shootingEntity == null) {
            ev = EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage)
        } else {
            ev = EntityDamageByChildEntityEvent(this.shootingEntity, this, entity, DamageCause.PROJECTILE, damage)
        }

        if (entity.attack(ev)) {
            this.setTarget(entity.getId())
        }
    }

    fun checkLure() {
        if (rod != null) {
            val ench: Enchantment? = rod!!.getEnchantment(Enchantment.ID_LURE)
            if (ench != null) {
                this.waitChance = 120 - (25 * ench.getLevel())
            }
        }
    }

    fun setTarget(eid: Long) {
        this.setDataProperty(EntityDataTypes.Companion.TARGET_EID, eid)
        this.canCollide = eid == 0L
    }

    override fun getOriginalName(): String {
        return "Fishing Hook"
    }
}
