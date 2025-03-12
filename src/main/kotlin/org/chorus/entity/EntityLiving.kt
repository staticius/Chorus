package org.chorus.entity

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.effect.*
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.entity.weather.EntityWeather
import org.chorus.event.entity.*
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.network.protocol.AnimatePacket
import org.chorus.network.protocol.EntityEventPacket
import org.chorus.scoreboard.manager.IScoreboardManager
import org.chorus.utils.TickCachedBlockIterator
import java.util.*

abstract class EntityLiving(chunk: IChunk?, nbt: CompoundTag?) : Entity(chunk, nbt), EntityDamageable {
    protected var attackTime: Int = 0
    protected var invisible: Boolean = false
    protected var movementSpeed: Float = DEFAULT_SPEED
    protected var turtleTicks: Int = 0
    private var attackTimeByShieldKb: Boolean = false
    private var attackTimeBefore: Int = 0

    override fun getGravity(): Float {
        return 0.08f
    }

    override fun getDrag(): Float {
        return 0.02f
    }

    override fun initEntity() {
        super.initEntity()

        if (namedTag!!.contains("HealF")) {
            namedTag!!.putFloat("Health", namedTag!!.getShort("HealF").toFloat())
            namedTag!!.remove("HealF")
        }

        if (!namedTag!!.contains("Health") || namedTag!!.get("Health") !is FloatTag) {
            namedTag!!.putFloat("Health", getMaxHealth().toFloat())
        }

        setHealth(namedTag!!.getFloat("Health"))
    }

    override fun setHealth(health: Float) {
        val wasAlive: Boolean = this.isAlive()
        super.setHealth(health)
        if (this.isAlive() && !wasAlive) {
            val pk: EntityEventPacket = EntityEventPacket()
            pk.eid = this.getId()
            pk.event = EntityEventPacket.RESPAWN
            Server.broadcastPacket(hasSpawned.values(), pk)
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag!!.putFloat("Health", this.getHealth())
    }

    fun hasLineOfSight(entity: Entity?): Boolean {
        //todo
        return true
    }

    fun collidingWith(ent: Entity) { // can override (IronGolem|Bats)
        ent.applyEntityCollision(this)
    }


    override fun attack(source: EntityDamageEvent): Boolean {
        if (this.noDamageTicks > 0 && source.cause != DamageCause.SUICIDE) { //ignore it if the cause is SUICIDE
            return false
        } else if (this.attackTime > 0 && !attackTimeByShieldKb) {
            val lastCause: EntityDamageEvent? = this.getLastDamageCause()
            if (lastCause != null && lastCause.getDamage() >= source.getDamage()) {
                return false
            }
        }

        if (isBlocking() && this.blockedByShield(source)) {
            return false
        }

        if (super.attack(source)) {
            if (source is EntityDamageByEntityEvent) {
                var damager: Entity = source.damager
                if (source is EntityDamageByChildEntityEvent) {
                    damager = source.getChild()
                }

                //Critical hit
                if (damager is Player && !damager.onGround) {
                    val animate: AnimatePacket = AnimatePacket()
                    animate.action = AnimatePacket.Action.CRITICAL_HIT
                    animate.eid = getId()

                    level!!.addChunkPacket(damager.getChunkX(), damager.getChunkZ(), animate)
                    level!!.addSound(this.position, Sound.GAME_PLAYER_ATTACK_STRONG)

                    source.setDamage(source.getDamage() * 1.5f)
                }

                if (damager.isOnFire() && damager !is Player) {
                    this.setOnFire(2 * Server.instance.getDifficulty())
                }

                val deltaX: Double = position.x - damager.position.x
                val deltaZ: Double = position.z - damager.position.z
                this.knockBack(damager, source.getDamage().toDouble(), deltaX, deltaZ, source.knockBack.toDouble())
            }

            val pk: EntityEventPacket = EntityEventPacket()
            pk.eid = this.getId()
            pk.event =
                if (this.getHealth() <= 0) EntityEventPacket.DEATH_ANIMATION else EntityEventPacket.HURT_ANIMATION
            Server.broadcastPacket(hasSpawned.values(), pk)

            this.attackTime = source.attackCooldown
            this.attackTimeByShieldKb = false
            this.scheduleUpdate()

            return true
        } else {
            return false
        }
    }

    fun knockBack(attacker: Entity?, damage: Double, x: Double, z: Double) {
        this.knockBack(attacker, damage, x, z, 0.4)
    }

    open fun knockBack(attacker: Entity?, damage: Double, x: Double, z: Double, base: Double) {
        var f: Double = Math.sqrt(x * x + z * z)
        if (f <= 0) {
            return
        }

        f = 1 / f

        val motion: Vector3 = Vector3(
            motion.x,
            motion.y, motion.z
        )

        motion.x /= 2.0
        motion.y /= 2.0
        motion.z /= 2.0
        motion.x += x * f * base
        motion.y += base
        motion.z += z * f * base

        if (motion.y > base) {
            motion.y = base
        }

        this.setMotion(motion)
    }

    override fun kill() {
        if (!this.isAlive()) {
            return
        }
        super.kill()
        val ev: EntityDeathEvent = EntityDeathEvent(this, this.getDrops())
        Server.instance.pluginManager.callEvent(ev)

        val manager: IScoreboardManager = Server.instance.scoreboardManager
        //测试环境中此项会null，所以说需要判空下
        manager.onEntityDead(this)

        if (level!!.gameRules.getBoolean(GameRule.DO_ENTITY_DROPS)) {
            for (item: Item? in ev.getDrops()) {
                level!!.dropItem(this.position, item)
            }
            level!!.dropExpOrb(this.position, getExperienceDrops())
        }
    }

    override fun entityBaseTick(): Boolean {
        return this.entityBaseTick(1)
    }

    override fun entityBaseTick(tickDiff: Int): Boolean {
        var isBreathing: Boolean = !this.isInsideOfWater()

        if (this is Player) {
            if (isBreathing && player.getInventory().getHelmet() is ItemTurtleHelmet) {
                turtleTicks = 200
            } else if (turtleTicks > 0) {
                isBreathing = true
                turtleTicks--
            }

            if (player.isCreative() || player.isSpectator()) {
                isBreathing = true
            }
        }

        this.setDataFlag(EntityFlag.BREATHING, isBreathing)

        var hasUpdate: Boolean = super.entityBaseTick(tickDiff)

        if (this.isAlive()) {
            if (this.isInsideOfSolid()) {
                hasUpdate = true
                this.attack(EntityDamageEvent(this, DamageCause.SUFFOCATION, 1f))
            }

            if (this.isOnLadder() || this.hasEffect(EffectType.LEVITATION) || this.hasEffect(EffectType.SLOW_FALLING)) {
                this.resetFallDistance()
            }

            if (!this.hasEffect(EffectType.WATER_BREATHING) && !this.hasEffect(EffectType.CONDUIT_POWER) && this.isInsideOfWater()) {
                if (this is EntitySwimmable || (this is Player && (this.isCreative() || this.isSpectator()))) {
                    this.setAirTicks(400)
                } else {
                    if (turtleTicks == 0 || turtleTicks == 200) {
                        hasUpdate = true
                        var airTicks: Int = this.getAirTicks() - tickDiff

                        if (airTicks <= -20) {
                            airTicks = 0
                            this.attack(EntityDamageEvent(this, DamageCause.DROWNING, 2f))
                        }

                        setAirTicks(airTicks)
                    }
                }
            } else {
                if (this is EntitySwimmable) {
                    hasUpdate = true
                    var airTicks: Int = getAirTicks() - tickDiff

                    if (airTicks <= -20) {
                        airTicks = 0
                        this.attack(EntityDamageEvent(this, DamageCause.SUFFOCATION, 2f))
                    }

                    setAirTicks(airTicks)
                } else {
                    val airTicks: Int = getAirTicks()

                    if (airTicks < 400) {
                        setAirTicks(Math.min(400, airTicks + tickDiff * 5))
                    }
                }
            }
        }

        if (this.attackTime > 0) {
            this.attackTime -= tickDiff
            if (this.attackTime <= 0) {
                attackTimeByShieldKb = false
            }
            hasUpdate = true
        }

        //吐槽：性能不要了是吧放EntityLiving这里
        //逻辑迁移到EntityVehicle去了
//        if (this.riding == null) {
//            for (Entity entity : level.fastNearbyEntities(this.boundingBox.grow(0.20000000298023224, 0.0D, 0.20000000298023224), this)) {
//                if (entity instanceof EntityRideable) {
//                    this.collidingWith(entity);
//                }
//            }
//        }

        // Used to check collisions with magma / cactus blocks
        // Math.round处理在某些条件下 出现x.999999的坐标条件,这里选择四舍五入
        val block: Block = level!!.getTickCachedBlock(
            position.getFloorX(), (Math.round(
                position.y
            ) - 1).toInt(),
            position.getFloorZ()
        )
        if (block is BlockMagma || block is BlockCactus) block.onEntityCollide(this)

        return hasUpdate
    }

    /**
     * Defines the drops after the entity's death
     */
    open fun getDrops(): Array<Item?> {
        return Item.EMPTY_ARRAY
    }

    open fun getExperienceDrops(): Int {
        return 0
    }

    fun getLineOfSight(maxDistance: Int): Array<Block> {
        return this.getLineOfSight(maxDistance, 0)
    }

    fun getLineOfSight(maxDistance: Int, maxLength: Int): Array<Block> {
        return this.getLineOfSight(maxDistance, maxLength, arrayOf())
    }

    fun getLineOfSight(maxDistance: Int, maxLength: Int, transparent: Array<String?>?): Array<Block> {
        var maxDistance: Int = maxDistance
        var transparent: Array<String?>? = transparent
        if (maxDistance > 120) {
            maxDistance = 120
        }

        if (transparent != null && transparent.size == 0) {
            transparent = null
        }

        val blocks: MutableList<Block> = ArrayList()

        val itr: TickCachedBlockIterator = TickCachedBlockIterator(
            this.level, this.position, this.getDirectionVector(),
            getEyeHeight().toDouble(), maxDistance
        )

        while (itr.hasNext()) {
            val block: Block = itr.next()
            blocks.add(block)

            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.removeAt(0)
            }

            val id: String = block.getId()

            if (transparent == null) {
                if (!block.isAir()) {
                    break
                }
            } else {
                if (Arrays.binarySearch(transparent, id) < 0) {
                    break
                }
            }
        }

        return blocks.toArray(Block.EMPTY_ARRAY)
    }

    fun getTargetBlock(maxDistance: Int): Block? {
        return getTargetBlock(maxDistance, arrayOf())
    }

    fun getTargetBlock(maxDistance: Int, transparent: Array<String?>?): Block? {
        try {
            val blocks: Array<Block> = this.getLineOfSight(maxDistance, 1, transparent)
            val block: Block = blocks.get(0)
            if (block != null) {
                if (transparent != null && transparent.size != 0) {
                    if (Arrays.binarySearch(transparent, block.getId()) < 0) {
                        return block
                    }
                } else {
                    return block
                }
            }
        } catch (ignored: Exception) {
        }
        return null
    }

    fun getMovementSpeed(): Float {
        return this.movementSpeed
    }

    /**
     * 设置该有生命实体的移动速度
     *
     *
     * Set the movement speed of this Entity.
     *
     * @param speed 速度大小<br></br>Speed value
     */
    open fun setMovementSpeed(speed: Float) {
        this.movementSpeed = ChorusMath.round(speed.toDouble(), 2).toFloat()
    }

    fun getAirTicks(): Int {
        return getDataProperty<Short>(EntityDataTypes.Companion.AIR_SUPPLY).toInt()
    }

    fun setAirTicks(ticks: Int) {
        this.setDataProperty(EntityDataTypes.Companion.AIR_SUPPLY, ticks)
    }

    protected fun blockedByShield(source: EntityDamageEvent): Boolean {
        var damager: Entity? = null
        if (source is EntityDamageByChildEntityEvent) {
            damager = source.getChild()
        } else if (source is EntityDamageByEntityEvent) {
            damager = source.damager
        }
        if (damager == null || damager is EntityWeather || !this.isBlocking()) {
            return false
        }

        val entityPos: Vector3 = damager.position
        val direction: Vector3 = this.getDirectionVector()
        val normalizedVector: Vector3 = position.subtract(entityPos).normalize()
        val blocked: Boolean = (normalizedVector.x * direction!!.x) + (normalizedVector.z * direction.z) < 0.0
        val knockBack: Boolean = damager !is EntityProjectile
        val event: EntityDamageBlockedEvent = EntityDamageBlockedEvent(this, source, knockBack, true)
        if (!blocked || !source.canBeReducedByArmor()) {
            event.setCancelled()
        }

        Server.instance.pluginManager.callEvent(event)
        if (event.isCancelled) {
            return false
        }

        if (event.knockBackAttacker && damager is EntityLiving) {
            val deltaX: Double = damager.position.getX() - position.getX()
            val deltaZ: Double = damager.position.getZ() - position.getZ()
            damager.knockBack(this, 0.0, deltaX, deltaZ)
            damager.attackTime = 10
            damager.attackTimeByShieldKb = true
        }

        onBlock(damager, source, event.animation)
        return true
    }

    protected open fun onBlock(entity: Entity?, event: EntityDamageEvent?, animate: Boolean) {
        if (animate) {
            level!!.addSound(this.position, Sound.ITEM_SHIELD_BLOCK)
        }
    }

    fun isBlocking(): Boolean {
        return this.getDataFlag(EntityFlag.BLOCKING)
    }

    fun setBlocking(value: Boolean) {
        this.setDataFlagExtend(EntityFlag.BLOCKING, value)
    }

    open fun isPersistent(): Boolean {
        return namedTag!!.containsByte("Persistent") && namedTag!!.getBoolean("Persistent")
    }

    open fun setPersistent(persistent: Boolean) {
        namedTag!!.putBoolean("Persistent", persistent)
    }

    fun preAttack(player: Player?) {
        if (attackTimeByShieldKb) {
            attackTimeBefore = attackTime
            attackTime = 0
        }
    }

    fun postAttack(player: Player?) {
        if (attackTimeByShieldKb && attackTime == 0) {
            attackTime = attackTimeBefore
        }
    }

    fun getAttackTime(): Int {
        return attackTime
    }

    fun isAttackTimeByShieldKb(): Boolean {
        return attackTimeByShieldKb
    }

    fun getAttackTimeBefore(): Int {
        return attackTimeBefore
    }

    companion object {
        const val DEFAULT_SPEED: Float = 0.1f
    }
}
