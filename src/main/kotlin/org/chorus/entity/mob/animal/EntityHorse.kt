package org.chorus.entity.mob.animal

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.EntityAI
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.PassByTimeEvaluator
import cn.nukkit.entity.ai.evaluator.ProbabilityEvaluator
import cn.nukkit.entity.ai.executor.AnimalGrowExecutor
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.LookAtTargetExecutor
import cn.nukkit.entity.ai.executor.TameHorseExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestFeedingPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.custom.CustomEntity
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.block.FarmLandDecayEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.EntityFallEvent
import cn.nukkit.inventory.*
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.AddEntityPacket
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.network.protocol.UpdateAttributesPacket
import cn.nukkit.network.protocol.types.EntityLink
import cn.nukkit.utils.*
import java.util.Set
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil

/**
 * @author PikyCZ
 */
open class EntityHorse(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable, EntityVariant,
    EntityMarkVariant, EntityRideable, EntityOwnable, InventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.Companion.HORSE
    }

    private var attributeMap: MutableMap<String?, Attribute>? = null
    private var horseInventory: HorseInventory? = null
    private val jumping = AtomicBoolean(false)

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.7f
        }
        return 1.4f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.8f
        }
        return 1.6f
    }

    public override fun initEntity() {
        attributeMap = HashMap()
        if (namedTag!!.containsList("Attributes")) {
            for (nbt in namedTag!!.getList<CompoundTag>("Attributes", CompoundTag::class.java).all) {
                attributeMap[nbt.getString("Name")] = Attribute.Companion.fromNBT(nbt)
            }
        } else {
            for (attribute in randomizeAttributes()) {
                attributeMap[attribute.name] = attribute
            }
        }
        this.maxHealth =
            ceil(attributeMap.get("minecraft:health")!!.maxValue.toDouble()).toInt()
        super.initEntity()

        this.horseInventory = HorseInventory(this)
        val inventoryTag: ListTag<CompoundTag>
        if (namedTag!!.containsList("Inventory")) {
            inventoryTag = namedTag!!.getList("Inventory", CompoundTag::class.java)
            val item0 = NBTIO.getItemHelper(inventoryTag[0])
            if (item0.isNull) {
                this.setDataFlag(EntityFlag.SADDLED, false)
                this.setDataFlag(EntityFlag.WASD_CONTROLLED, false)
                this.setDataFlag(EntityFlag.CAN_POWER_JUMP, false)
            } else {
                this.inventory.setItem(0, item0)
            }
            this.inventory.setItem(1, NBTIO.getItemHelper(inventoryTag[1]))
        } else {
            this.setDataFlag(EntityFlag.SADDLED, false)
            this.setDataFlag(EntityFlag.WASD_CONTROLLED, false)
            this.setDataFlag(EntityFlag.CAN_POWER_JUMP, false)
        }
        this.setDataFlag(EntityFlag.HAS_GRAVITY, true)
        this.setDataFlag(EntityFlag.CAN_CLIMB, true)
        this.setDataFlag(EntityFlag.HAS_COLLISION, true)

        if (!hasVariant()) {
            this.setVariant(randomVariant())
        }
        if (!hasMarkVariant()) {
            this.setMarkVariant(randomMarkVariant())
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        val inventoryTag = ListTag<CompoundTag>()
        if (this.inventory != null) {
            val item0 = this.inventory.getItem(0)
            val item1 = this.inventory.getItem(1)
            inventoryTag.add(NBTIO.putItemHelper(item0, 0))
            inventoryTag.add(NBTIO.putItemHelper(item1, 1))
        }
        namedTag!!.putList("Inventory", inventoryTag)

        val compoundTagListTag = ListTag<CompoundTag>()
        for (attribute in attributeMap!!.values) {
            compoundTagListTag.add(Attribute.Companion.toNBT(attribute))
        }
        namedTag!!.putList("Attributes", compoundTagListTag)
    }

    override fun setHealth(health: Float) {
        super.setHealth(health)
        if (this.isAlive) {
            val attr = attributeMap!!["minecraft:health"]
                .setDefaultValue(maxHealth.toFloat())
                .setMaxValue(maxHealth.toFloat())
                .setValue(if (health > 0) (if (health < maxHealth) health else maxHealth.toFloat()) else 0f)
            val pk = UpdateAttributesPacket()
            pk.entries = arrayOf(attr)
            pk.entityId = this.getId()
            Server.broadcastPacket(this.viewers.values.toArray<Player>(Player.EMPTY_ARRAY), pk)
        }
    }

    override fun setMaxHealth(maxHealth: Int) {
        super.setMaxHealth(maxHealth)
        val attr = attributeMap!!["minecraft:health"]
            .setMaxValue(maxHealth.toFloat())
            .setDefaultValue(maxHealth.toFloat())
            .setValue(if (health > 0) (if (health < getMaxHealth()) health else getMaxHealth().toFloat()) else 0f)
        if (this.isAlive) {
            val pk = UpdateAttributesPacket()
            pk.entries = arrayOf(attr)
            pk.entityId = this.getId()
            Server.broadcastPacket(this.viewers.values.toArray<Player>(Player.EMPTY_ARRAY), pk)
        }
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.LEATHER), getHorseArmor(), getSaddle())
    }

    override fun getOriginalName(): String {
        return "Horse"
    }

    override fun getAllVariant(): IntArray {
        return VARIANTS
    }

    override fun getAllMarkVariant(): IntArray {
        return MARK_VARIANTS
    }

    fun getJumping(): AtomicBoolean {
        return jumping
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //todo breed
                Behavior(
                    AnimalGrowExecutor(),  //todo：Growth rate
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.ENTITY_SPAWN_TIME, 20 * 60 * 20, Int.MAX_VALUE),
                        IBehaviorEvaluator { entity: EntityMob -> entity is EntityAnimal && entity.isBaby() }
                    ),
                    1, 1, 1200
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    TameHorseExecutor(0.4f, 12, 40, true, 100, true, 10, 35), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.RIDER_NAME),
                        IBehaviorEvaluator { e: EntityMob? -> !this.hasOwner(false) }
                    ), 4, 1),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10),
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

    override fun asyncPrepare(currentTick: Int) {
        if (this.getRider() == null || this.owner == null || getSaddle().isNull) {
            isActive = level!!.isHighLightChunk(chunkX, chunkZ)
            if (!this.isImmobile) {
                val behaviorGroup = getBehaviorGroup() ?: return
                behaviorGroup.collectSensorData(this)
                behaviorGroup.evaluateCoreBehaviors(this)
                behaviorGroup.evaluateBehaviors(this)
                behaviorGroup.tickRunningCoreBehaviors(this)
                behaviorGroup.tickRunningBehaviors(this)
                behaviorGroup.updateRoute(this)
                behaviorGroup.applyController(this)
                if (EntityAI.checkDebugOption(EntityAI.DebugOption.BEHAVIOR)) behaviorGroup.debugTick(
                    this
                )
            }
            this.needsRecalcMovement =
                level!!.tickRateOptDelay == 1 || ((currentTick + tickSpread) and (level!!.tickRateOptDelay - 1)) == 0
            this.calculateOffsetBoundingBox()
            if (!this.isImmobile) {
                handleGravity()
                if (needsRecalcMovement) {
                    handleCollideMovement(currentTick)
                }
                addTmpMoveMotionXZ(previousCollideMotion)
                handleFloatingMovement()
                handleGroundFrictionMovement()
                handlePassableBlockFrictionMovement()
            }
        }
    }

    override fun fall(fallDistance: Float) {
        var fallDistance = fallDistance
        if (this.hasEffect(EffectType.Companion.SLOW_FALLING)) {
            return
        }

        val floorLocation = position.floor()
        val down = level!!.getBlock(floorLocation.down())

        val event = EntityFallEvent(this, down, fallDistance)
        server!!.pluginManager.callEvent(event)
        if (event.isCancelled) {
            return
        }
        fallDistance = event.fallDistance

        if ((!this.isPlayer || level!!.gameRules
                .getBoolean(GameRule.FALL_DAMAGE)) && down.useDefaultFallDamage()
        ) {
            val jumpBoost =
                if (this.hasEffect(EffectType.Companion.JUMP_BOOST)) getEffect(EffectType.Companion.JUMP_BOOST).level else 0
            val damage = fallDistance - 3 - jumpBoost - getClientMaxJumpHeight()

            if (damage > 0) {
                this.attack(EntityDamageEvent(this, DamageCause.FALL, damage))
            }
        }

        down.onEntityFallOn(this, fallDistance)

        if (fallDistance > 0.75) { //todo: moving these into their own classes (method "onEntityFallOn()")
            if (down.id == Block.FARMLAND) {
                if (onPhysicalInteraction(down, false)) {
                    return
                }
                val farmEvent = FarmLandDecayEvent(this, down)
                server!!.pluginManager.callEvent(farmEvent)
                if (farmEvent.isCancelled) return
                level!!.setBlock(down.position, BlockDirt(), false, true)
                return
            }

            val floor = level!!.getTickCachedBlock(floorLocation)
            if (floor is BlockTurtleEgg) {
                if (onPhysicalInteraction(floor, ThreadLocalRandom.current().nextInt(10) >= 3)) {
                    return
                }
                level!!.useBreakOn(this.position, null, null, true)
            }
        }
    }

    override fun onUpdate(currentTick: Int): Boolean {
        val b = super.onUpdate(currentTick)
        if (currentTick % 2 == 0) {
            if (this.jumping != null && jumping.get() && this.isOnGround) {
                this.setDataFlag(EntityFlag.STANDING, false)
                jumping.set(false)
            }
        }
        return b
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return super.canCollideWith(entity) && entity !== this.getRider()
    }

    fun onInput(clientLoc: Transform) {
        if (this.getRider() == null || this.owner == null || getSaddle().isNull) return
        //每次输入乘骑玩家位置之前都要确保motion为0,避免onGround不更新
        motion.x = 0.0
        motion.y = 0.0
        motion.z = 0.0
        this.moveTarget = null
        this.lookTarget = null
        this.move(
            clientLoc.position.x - position.x,
            clientLoc.position.y - position.y,
            clientLoc.position.z - position.z
        )
        rotation.yaw = clientLoc.rotation.yaw
        this.headYaw = clientLoc.headYaw
        broadcastMovement(false)
    }

    override fun getOwnerName(): String? {
        val ownerName = super<EntityOwnable>.getOwnerName()
        if (ownerName == null) {
            this.setDataProperty(EntityDataTypes.Companion.CONTAINER_TYPE, 0)
            this.setDataProperty(EntityDataTypes.Companion.CONTAINER_SIZE, 0)
        } else {
            //添加两个metadata这个才能交互物品栏
            this.setDataProperty(EntityDataTypes.Companion.CONTAINER_TYPE, 12)
            this.setDataProperty(EntityDataTypes.Companion.CONTAINER_SIZE, 2)
        }
        return ownerName
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        mountEntity(player)
        return false
    }

    override fun mountEntity(entity: Entity): Boolean {
        this.memoryStorage.put<String>(CoreMemoryTypes.Companion.RIDER_NAME, entity.name)
        super.mountEntity(entity, EntityLink.Type.RIDER)
        return true
    }

    override fun dismountEntity(entity: Entity): Boolean {
        this.memoryStorage.clear(CoreMemoryTypes.Companion.RIDER_NAME)
        return super.dismountEntity(entity)
    }

    override fun getMountedOffset(entity: Entity?): Vector3f {
        return Vector3f(0f, 2.42f, 0f)
    }

    override fun getInventory(): HorseInventory {
        return horseInventory!!
    }

    fun getRider(): Entity? {
        val name = memoryStorage.get<String>(CoreMemoryTypes.Companion.RIDER_NAME)
        return if (name != null) {
            Server.getInstance().getPlayerExact(name)
        } else null //todo other entity
    }

    fun getClientMaxJumpHeight(): Float {
        return attributeMap!!["minecraft:horse.jump_strength"]!!.value
    }

    /**
     * @see HorseInventory.setSaddle
     */
    fun setSaddle(item: Item?) {
        this.inventory.saddle = item
    }

    /**
     * @see HorseInventory.setHorseArmor
     */
    fun setHorseArmor(item: Item?) {
        this.inventory.horseArmor = item
    }

    /**
     * @see HorseInventory.getSaddle
     */
    fun getSaddle(): Item {
        return this.inventory.saddle
    }

    /**
     * @see HorseInventory.getHorseArmor
     */
    fun getHorseArmor(): Item {
        return this.inventory.horseArmor
    }

    /**
     * 播放驯服失败的动画
     *
     *
     * Play an animation of a failed tamer
     */
    fun playTameFailAnimation() {
        level!!.addLevelSoundEvent(
            this.position, LevelSoundEventPacket.SOUND_MAD, -1, "minecraft:horse",
            this.isBaby(), false
        )
        this.setDataFlag(EntityFlag.STANDING)
    }

    /**
     * 停止播放驯服失败的动画
     *
     *
     * Stop playing the animation that failed to tame
     */
    fun stopTameFailAnimation() {
        this.setDataFlag(EntityFlag.STANDING, false)
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
        val attr = attributeMap!!["minecraft:health"]
            .setDefaultValue(maxHealth.toFloat())
            .setMaxValue(maxHealth.toFloat())
            .setValue(if (health > 0) (if (health < maxHealth) health else maxHealth.toFloat()) else 0f)
        val pk = UpdateAttributesPacket()
        pk.entries = arrayOf(attr)
        pk.entityId = this.getId()
        player.dataPacket(pk)
    }

    protected fun generateRandomMaxHealth(): Float {
        return 15.0f + Utils.rand(0, 8).toFloat() + Utils.rand(0, 9).toFloat()
    }

    protected fun generateRandomJumpStrength(): Float {
        return (0.4f + Utils.random.nextDouble() * 0.2 + Utils.random.nextDouble() * 0.2 + Utils.random.nextDouble() * 0.2).toFloat()
    }

    protected fun generateRandomSpeed(): Float {
        return ((0.45f + Utils.random.nextDouble() * 0.3 + Utils.random.nextDouble() * 0.3 + Utils.random.nextDouble() * 0.3) * 0.25).toFloat()
    }

    protected fun randomizeAttributes(): Array<Attribute?> {
        val attributes = arrayOfNulls<Attribute>(3)
        attributes[0] = Attribute.Companion.getAttribute(Attribute.Companion.MOVEMENT_SPEED)!!
            .setValue(generateRandomSpeed())
        val maxHealth = generateRandomMaxHealth()
        attributes[1] = Attribute.Companion.getAttribute(Attribute.Companion.MAX_HEALTH)!!
            .setMinValue(0f).setMaxValue(maxHealth).setDefaultValue(maxHealth).setValue(maxHealth)
        attributes[2] = Attribute.Companion.getAttribute(Attribute.Companion.HORSE_JUMP_STRENGTH)!!
            .setValue(generateRandomJumpStrength())
        val compoundTagListTag = ListTag<CompoundTag>()
        compoundTagListTag.add(Attribute.Companion.toNBT(attributes[0]!!)).add(
            Attribute.Companion.toNBT(
                attributes[1]!!
            )
        ).add(Attribute.Companion.toNBT(attributes[2]!!))
        namedTag!!.putList("Attributes", compoundTagListTag)
        return attributes
    }

    override fun createAddEntityPacket(): DataPacket {
        val addEntity = AddEntityPacket()
        addEntity.type = this.networkId
        addEntity.entityUniqueId = this.getId()
        if (this is CustomEntity) {
            addEntity.id = this.getIdentifier()
        }
        addEntity.entityRuntimeId = this.getId()
        addEntity.yaw = rotation.yaw.toFloat()
        addEntity.headYaw = rotation.yaw.toFloat()
        addEntity.pitch = rotation.pitch.toFloat()
        addEntity.x = position.x.toFloat()
        addEntity.y = position.y.toFloat() + this.baseOffset
        addEntity.z = position.z.toFloat()
        addEntity.speedX = motion.x.toFloat()
        addEntity.speedY = motion.y.toFloat()
        addEntity.speedZ = motion.z.toFloat()
        addEntity.entityData = this.entityDataMap
        addEntity.attributes = attributeMap!!.values.toArray<Attribute>(Attribute.Companion.EMPTY_ARRAY)
        addEntity.links = arrayOfNulls(passengers.size)
        for (i in addEntity.links.indices) {
            addEntity.links[i] = EntityLink(
                this.getId(),
                passengers[i]!!.id, if (i == 0) EntityLink.Type.RIDER else EntityLink.Type.PASSENGER, false, false
            )
        }

        return addEntity
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id == Item.GOLDEN_APPLE || item.id == Item.GOLDEN_CARROT
    }

    companion object {
        private val VARIANTS = intArrayOf(0, 1, 2, 3, 4, 5, 6)
        private val MARK_VARIANTS = intArrayOf(0, 1, 2, 3, 4)
    }
}
