package cn.nukkit.entity.projectile.abstract_arrow

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.entity.weather.EntityLightningBolt
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.ProjectileHitEvent
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.DoubleTag
import cn.nukkit.nbt.tag.IntTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.AddEntityPacket
import cn.nukkit.registry.Registries
import java.util.concurrent.*

/**
 * @author PetteriM1
 * @author GoodLucky777
 */
class EntityThrownTrident @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag, shootingEntity: Entity? = null) :
    EntityAbstractArrow(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.THROWN_TRIDENT
    }

    var alreadyCollided: Boolean = false
    protected var trident: Item? = null

    // Default Values
    protected var gravity: Float = 0.04f

    protected var drag: Float = 0.01f

    protected var pickupMode: Int = 0
    private var collisionPos: Vector3? = null
    private var stuckToBlockPos: BlockVector3? = null
    private var favoredSlot: Int = 0
    override var player: Boolean = false

    // Enchantment
    private var loyaltyLevel: Int = 0
    private var hasChanneling: Boolean = false
    private var riptideLevel: Int = 0
    private var impalingLevel: Int = 0

    override fun getLength(): Float {
        return 0.25f
    }

    public override fun getGravity(): Float {
        return 0.05f
    }

    public override fun getDrag(): Float {
        return 0.01f
    }

    override fun getOriginalName(): String {
        return NAME_TRIDENT
    }

    override fun initEntity() {
        super.setHasAge(false)
        super.initEntity()

        this.closeOnCollide = false

        this.pickupMode =
            (if (namedTag!!.contains(TAG_PICKUP)) namedTag!!.getByte(TAG_PICKUP) else EntityProjectile.Companion.PICKUP_ANY.toByte()).toInt()
        this.favoredSlot = if (namedTag!!.contains(TAG_FAVORED_SLOT)) namedTag!!.getInt(TAG_FAVORED_SLOT) else -1
        this.player = !namedTag!!.contains(TAG_PLAYER) || namedTag!!.getBoolean(TAG_PLAYER)

        if (namedTag!!.contains(TAG_CREATIVE)) {
            if (pickupMode == EntityProjectile.Companion.PICKUP_ANY && namedTag!!.getBoolean(TAG_CREATIVE)) {
                pickupMode = EntityProjectile.Companion.PICKUP_CREATIVE
            }
            namedTag!!.remove(TAG_CREATIVE)
        }

        if (namedTag!!.contains(TAG_TRIDENT)) {
            this.trident = NBTIO.getItemHelper(namedTag!!.getCompound(TAG_TRIDENT))
            this.loyaltyLevel = trident.getEnchantmentLevel(Enchantment.ID_TRIDENT_LOYALTY)
            this.hasChanneling = trident.hasEnchantment(Enchantment.ID_TRIDENT_CHANNELING)
            this.riptideLevel = trident.getEnchantmentLevel(Enchantment.ID_TRIDENT_RIPTIDE)
            this.impalingLevel = trident.getEnchantmentLevel(Enchantment.ID_TRIDENT_IMPALING)
        } else {
            this.trident = Item.AIR
            this.loyaltyLevel = 0
            this.hasChanneling = false
            this.riptideLevel = 0
            this.impalingLevel = 0
        }

        if (namedTag!!.contains("CollisionPos")) {
            val collisionPosList: ListTag<DoubleTag> = namedTag!!.getList(
                "CollisionPos",
                DoubleTag::class.java
            )
            collisionPos =
                Vector3(collisionPosList.get(0).data, collisionPosList.get(1).data, collisionPosList.get(2).data)
        } else {
            collisionPos = defaultCollisionPos.clone()
        }

        if (namedTag!!.contains("StuckToBlockPos")) {
            val stuckToBlockPosList: ListTag<IntTag> = namedTag!!.getList(
                "StuckToBlockPos",
                IntTag::class.java
            )
            stuckToBlockPos = BlockVector3(
                stuckToBlockPosList.get(0).data,
                stuckToBlockPosList.get(1).data,
                stuckToBlockPosList.get(2).data
            )
        } else {
            stuckToBlockPos = defaultStuckToBlockPos.clone()
        }
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.put(TAG_TRIDENT, NBTIO.putItemHelper(this.trident))
        namedTag!!.putByte(TAG_PICKUP, this.pickupMode)
        namedTag!!.putList(
            "CollisionPos", ListTag<DoubleTag>()
                .add(DoubleTag(collisionPos!!.x))
                .add(DoubleTag(collisionPos!!.y))
                .add(DoubleTag(collisionPos!!.z))
        )
        namedTag!!.putList(
            "StuckToBlockPos", ListTag<IntTag>()
                .add(IntTag(stuckToBlockPos!!.x))
                .add(IntTag(stuckToBlockPos!!.y))
                .add(IntTag(stuckToBlockPos!!.z))
        )
        namedTag!!.putInt(TAG_FAVORED_SLOT, this.favoredSlot)
        namedTag!!.putBoolean(TAG_PLAYER, this.player)
    }

    fun getItem(): Item {
        return if (this.trident != null) trident!!.clone() else Item.AIR
    }

    fun setItem(item: Item) {
        this.trident = item.clone()
        this.loyaltyLevel = trident!!.getEnchantmentLevel(Enchantment.ID_TRIDENT_LOYALTY)
        this.hasChanneling = trident!!.hasEnchantment(Enchantment.ID_TRIDENT_CHANNELING)
        this.riptideLevel = trident!!.getEnchantmentLevel(Enchantment.ID_TRIDENT_RIPTIDE)
        this.impalingLevel = trident!!.getEnchantmentLevel(Enchantment.ID_TRIDENT_IMPALING)
    }

    fun setCritical() {
        this.setCritical(true)
    }

    fun isCritical(): Boolean {
        return this.getDataFlag(EntityFlag.CRITICAL)
    }

    fun setCritical(value: Boolean) {
        this.setDataFlag(EntityFlag.CRITICAL, value)
    }

    override fun getResultDamage(): Int {
        var base: Int = super.getResultDamage()

        if (this.isCritical()) {
            base += ThreadLocalRandom.current().nextInt(base / 2 + 2)
        }

        return base
    }

    override fun getBaseDamage(): Double {
        return 8.0
    }


    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        if (this.isCollided && !this.hadCollision) {
            level!!.addSound(this.position, Sound.ITEM_TRIDENT_HIT_GROUND)
        }

        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.onGround || this.hadCollision) {
            this.setCritical(false)
        }

        if (this.noClip) {
            if (this.canReturnToShooter()) {
                val shooter: Entity? = this.shootingEntity
                val vector3: Vector3 = Vector3(
                    shooter!!.position.x - position.x,
                    shooter.position.y + shooter.getEyeHeight() - position.y,
                    shooter.position.z - position.z
                )
                val bVector: BVector3 = BVector3.fromPos(vector3)
                this.setRotation(bVector.getYaw(), bVector.getPitch())
                this.setMotion(getMotion().multiply(-1.0))
                hasUpdate = true
            } else {
                if (level!!.gameRules.getBoolean(GameRule.DO_ENTITY_DROPS) && !this.closed) {
                    level!!.dropItem(this.position, this.trident)
                }
                this.close()
            }
        }

        return hasUpdate
    }

    override fun spawnTo(player: Player) {
        val pk: AddEntityPacket = AddEntityPacket()
        pk.type = Registries.ENTITY.getEntityNetworkId(EntityID.Companion.THROWN_TRIDENT)
        pk.entityUniqueId = this.getId()
        pk.entityRuntimeId = this.getId()
        pk.x = position.x.toFloat()
        pk.y = position.y.toFloat()
        pk.z = position.z.toFloat()
        pk.speedX = motion.x.toFloat()
        pk.speedY = motion.y.toFloat()
        pk.speedZ = motion.z.toFloat()
        pk.yaw = rotation.yaw.toFloat()
        pk.pitch = rotation.pitch.toFloat()
        pk.entityData = this.entityDataMap
        player.dataPacket(pk)

        super.spawnTo(player)
    }


    override fun onCollideWithEntity(entity: Entity) {
        if (this.noClip || this.closed) {
            return
        }

        if (this.alreadyCollided) {
            return
        }

        server!!.pluginManager.callEvent(ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)))
        var damage: Float = getResultDamage().toFloat()
        if (this.impalingLevel > 0 && (entity.isTouchingWater() || (entity.level!!.isRaining() && entity.level!!.canBlockSeeSky(
                entity.position
            )))
        ) {
            damage = damage + (2.5f * impalingLevel.toFloat())
        }

        val ev: EntityDamageEvent
        if (this.shootingEntity == null) {
            ev = EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage)
        } else {
            ev = EntityDamageByChildEntityEvent(this.shootingEntity, this, entity, DamageCause.PROJECTILE, damage)
        }
        entity.attack(ev)
        level!!.addSound(this.position, Sound.ITEM_TRIDENT_HIT)
        this.hadCollision = true
        this.alreadyCollided = true
        this.setCollisionPos(this.position)
        this.setMotion(
            Vector3(
                getMotion().getX() * -0.01,
                getMotion().getY() * -0.1, getMotion().getZ() * -0.01
            )
        )

        if (this.hasChanneling) {
            if (level!!.isThundering() && level!!.canBlockSeeSky(this.position)) {
                val pos: Locator = this.getLocator()
                val lighting: EntityLightningBolt =
                    EntityLightningBolt(pos.getChunk(), Entity.Companion.getDefaultNBT(pos.position))
                lighting.spawnToAll()
                level!!.addSound(this.position, Sound.ITEM_TRIDENT_THUNDER)
            }
        }

        if (this.canReturnToShooter()) {
            level!!.addSound(this.position, Sound.ITEM_TRIDENT_RETURN)
            this.setNoClip(true)
            this.hadCollision = false
            this.setTridentRope(true)
        }
    }

    fun getPickupMode(): Int {
        return this.pickupMode
    }

    fun setPickupMode(pickupMode: Int) {
        this.pickupMode = pickupMode
    }

    override fun onCollideWithBlock(locator: Locator, motion: Vector3) {
        if (this.noClip) {
            return
        }

        for (collisionBlock: Block in level!!.getCollisionBlocks(getBoundingBox()!!.grow(0.1, 0.1, 0.1))) {
            this.setStuckToBlockPos(
                BlockVector3(
                    collisionBlock.position.getFloorX(),
                    collisionBlock.position.getFloorY(),
                    collisionBlock.position.getFloorZ()
                )
            )
            if (this.canReturnToShooter()) {
                level!!.addSound(this.position, Sound.ITEM_TRIDENT_RETURN)
                this.setNoClip(true)
                this.setTridentRope(true)
                return
            }
            onCollideWithBlock(locator, motion, collisionBlock)
        }
    }

    fun getCollisionPos(): Vector3? {
        return collisionPos
    }

    fun setCollisionPos(collisionPos: Vector3?) {
        this.collisionPos = collisionPos
    }

    fun getStuckToBlockPos(): BlockVector3? {
        return stuckToBlockPos
    }

    fun setStuckToBlockPos(stuckToBlockPos: BlockVector3?) {
        this.stuckToBlockPos = stuckToBlockPos
    }

    fun getFavoredSlot(): Int {
        return favoredSlot
    }

    fun setFavoredSlot(favoredSlot: Int) {
        this.favoredSlot = favoredSlot
    }

    fun isCreative(): Boolean {
        return getPickupMode() == EntityProjectile.Companion.PICKUP_CREATIVE
    }

    fun isPlayer(): Boolean {
        return player
    }

    fun setPlayer(player: Boolean) {
        this.player = player
    }

    fun getLoyaltyLevel(): Int {
        return loyaltyLevel
    }

    fun setLoyaltyLevel(loyaltyLevel: Int) {
        this.loyaltyLevel = loyaltyLevel
        if (loyaltyLevel > 0) {
            trident!!.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_TRIDENT_LOYALTY).setLevel(loyaltyLevel))
        } else {
            // TODO: this.trident.removeEnchantment(Enchantment.ID_TRIDENT_LOYALTY);
        }
    }

    fun hasChanneling(): Boolean {
        return hasChanneling
    }

    fun setChanneling(hasChanneling: Boolean) {
        this.hasChanneling = hasChanneling
        if (hasChanneling) {
            trident!!.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_TRIDENT_CHANNELING))
        } else {
            // TODO: this.trident.removeEnchantment(Enchantment.ID_TRIDENT_CHANNELING);
        }
    }

    fun getRiptideLevel(): Int {
        return riptideLevel
    }

    fun setRiptideLevel(riptideLevel: Int) {
        this.riptideLevel = riptideLevel
        if (riptideLevel > 0) {
            trident!!.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_TRIDENT_RIPTIDE).setLevel(riptideLevel))
        } else {
            // TODO: this.trident.removeEnchantment(Enchantment.ID_TRIDENT_RIPTIDE);
        }
    }

    fun getImpalingLevel(): Int {
        return impalingLevel
    }

    fun setImpalingLevel(impalingLevel: Int) {
        this.impalingLevel = impalingLevel
        if (impalingLevel > 0) {
            trident!!.addEnchantment(
                Enchantment.getEnchantment(Enchantment.ID_TRIDENT_IMPALING).setLevel(impalingLevel)
            )
        } else {
            // TODO: this.trident.removeEnchantment(Enchantment.ID_TRIDENT_IMPALING);
        }
    }

    fun getTridentRope(): Boolean {
        return this.getDataFlag(EntityFlag.RETURN_TRIDENT)
    }

    fun setTridentRope(tridentRope: Boolean) {
        if (tridentRope) {
            this.setDataProperty(EntityDataTypes.Companion.OWNER_EID, shootingEntity!!.getId())
        } else {
            this.setDataProperty(EntityDataTypes.Companion.OWNER_EID, -1)
        }
        this.setDataFlag(EntityFlag.RETURN_TRIDENT, tridentRope)
    }

    fun canReturnToShooter(): Boolean {
        if (this.loyaltyLevel <= 0) {
            return false
        }

        if (this.getCollisionPos() == defaultCollisionPos && this.getStuckToBlockPos() == defaultStuckToBlockPos) {
            return false
        }

        val shooter: Entity? = this.shootingEntity
        if (shooter != null) {
            if (shooter.isAlive() && shooter is Player) {
                return !(shooter.isSpectator())
            }
        }
        return false
    }

    companion object {
        private const val TAG_PICKUP: String = "pickup"
        private const val TAG_TRIDENT: String = "Trident"
        private const val TAG_FAVORED_SLOT: String = "favoredSlot"
        private const val TAG_CREATIVE: String = "isCreative"
        private const val TAG_PLAYER: String = "player"
        private const val NAME_TRIDENT: String = "Trident"
        private val defaultCollisionPos: Vector3 = Vector3(0.0, 0.0, 0.0)
        private val defaultStuckToBlockPos: BlockVector3 = BlockVector3(0, 0, 0)
    }
}
