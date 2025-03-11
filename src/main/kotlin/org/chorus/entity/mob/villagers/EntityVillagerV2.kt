package org.chorus.entity.mob.villagers

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.executor.villager.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.BlockSensor
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.data.profession.Profession
import org.chorus.entity.item.EntityItem
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityAnimal
import org.chorus.entity.mob.monster.humanoid_monster.EntityZombie
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.*
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.*
import org.chorus.network.protocol.EntityEventPacket
import org.chorus.network.protocol.TakeItemEntityPacket
import org.chorus.network.protocol.UpdateTradePacket
import org.chorus.registry.Registries
import org.chorus.utils.*
import org.chorus.utils.random.NukkitRandom
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import java.util.*
import java.util.Set
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.collections.remove
import kotlin.collections.set
import kotlin.collections.toTypedArray
import kotlin.math.max
import kotlin.math.min

class EntityVillagerV2(chunk: IChunk?, nbt: CompoundTag?) : EntityMob(chunk, nbt!!), InventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.Companion.VILLAGER_V2
    }

    private val tradeNetId: MutableList<Int> = ArrayList()

    fun getTradeNetIds(): MutableList<Int> {
        if (tradeNetId == null) return ArrayList()
        return tradeNetId
    }

    fun getRecipes(): ListTag<CompoundTag?> {
        return ListTag<CompoundTag?>(
            Tag.TAG_COMPOUND.toInt(),
            TradeRecipeBuildUtils.RECIPE_MAP.entries.stream()
                .filter { t: Map.Entry<Int, CompoundTag?> -> getTradeNetIds().contains(t.key) }.toList().stream()
                .map<CompoundTag?> { java.util.Map.Entry.value }.toList()
        )
    }

    var tierExpRequirement: IntArray = intArrayOf(0, 10, 70, 150, 250)

    protected var tradeInventory: TradeInventory? = null

    private var inventory: EntityEquipmentInventory? = null

    protected var canTrade: Boolean? = null

    protected var displayName: String? = null

    override var tradeTier: Int = 0

    protected var maxTradeTier: Int = 0

    protected var tradeExp: Int = 0

    protected var tradeSeed: Int = 0

    /**
     * 0 generic
     * 1 farmer
     * 2 fisherman
     * 3 shepherd
     * 4 fletcher
     * 5 librarian
     * 6 cartographer
     * 7 cleric
     * 8 armor
     * 9 weapon
     * 10 tool
     * 11 butcher
     * 12 butcher
     * 13 mason
     * 14 nitwit
     */
    protected var profession: Int = 0

    init {
        applyProfession()
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(
                    DoorExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? ->
                            val block = memoryStorage!!.get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK_2)
                            if (block == null || moveDirectionEnd == null) return@all false
                            level!!.raycastBlocks(this.position, moveDirectionEnd, true, false, 0.5).contains(block)
                        }
                    ), 4, 1),
                Behavior(
                    WillingnessExecutor(),
                    all(
                        IBehaviorEvaluator { entity: EntityMob? -> getFoodPoints() >= 12 },
                        IBehaviorEvaluator { entity: EntityMob? -> !isBaby() },
                        IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.WILLING) },
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ), 3, 1, 1, false
                ),  //生长
                Behavior(
                    AnimalGrowExecutor(),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.ENTITY_SPAWN_TIME, 20 * 60 * 20, Int.MAX_VALUE),
                        IBehaviorEvaluator { entity: EntityMob -> entity is EntityAnimal && entity.isBaby() }
                    ), 2, 1, 1200
                ),
                Behavior(
                    PlaySoundExecutor(
                        Sound.MOB_VILLAGER_IDLE,
                        if (isBaby()) 1.3f else 0.8f,
                        if (isBaby()) 1.7f else 1.2f,
                        1f,
                        1f
                    ), RandomSoundEvaluator(), 1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    { entity: EntityMob? ->
                        moveTarget = null
                        lookTarget = getTradeInventory()!!.viewers.stream().findFirst().get().position
                        true
                    },
                    { entity: EntityMob? -> getTradeInventory() != null && !getTradeInventory()!!.viewers.isEmpty() },
                    9,
                    1
                ),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_ZOMBIE, 0.5f, true, 8f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_ZOMBIE),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_ZOMBIE, 8.0),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.NEAREST_ZOMBIE) && memoryStorage!!.get<Entity>(
                                CoreMemoryTypes.Companion.NEAREST_ZOMBIE
                            ) is EntityMob && i.getMemoryStorage()
                                .notEmpty(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET) && i.getMemoryStorage()
                                .get<Entity>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET) === this
                        },
                        IBehaviorEvaluator { entity: EntityMob? ->
                            memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.NEAREST_ZOMBIE) && level!!.raycastBlocks(
                                this.position,
                                memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.NEAREST_ZOMBIE).position
                            ).isEmpty()
                        }
                    ), 8, 1),
                Behavior(
                    SleepExecutor(), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.OCCUPIED_BED),
                        DistanceEvaluator(CoreMemoryTypes.Companion.OCCUPIED_BED, 2.0),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 100),
                        IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 12000 && entity.level!!.dayTime < Level.TIME_FULL }
                    ), 7, 1),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.OCCUPIED_BED, 0.3f, true), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.OCCUPIED_BED),
                        any(
                            IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 12000 && entity.level!!.dayTime < Level.TIME_FULL },
                            all(
                                IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 11000 && entity.level!!.dayTime < 12000 },
                                not(DistanceEvaluator(CoreMemoryTypes.Companion.OCCUPIED_BED, 5.0))
                            )
                        )
                    ), 6, 1
                ),
                Behavior(
                    NearbyFlatRandomRoamExecutor(
                        CoreMemoryTypes.Companion.OCCUPIED_BED,
                        0.2f,
                        5,
                        100,
                        false,
                        -1,
                        true,
                        10
                    ), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.OCCUPIED_BED),
                        IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 11000 && entity.level!!.dayTime < 12000 }
                    ), 5, 1),
                Behavior(
                    WorkExecutor(), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.SITE_BLOCK),
                        any(
                            IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 0 && entity.level!!.dayTime < 8000 },
                            IBehaviorEvaluator { entity: EntityMob -> level!!.dayTime >= 10000 && entity.level!!.dayTime < 11000 }
                        )
                    ), 4, 1, 1),
                Behavior(
                    GossipExecutor(CoreMemoryTypes.Companion.GOSSIP_TARGET), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.GOSSIP_TARGET),
                        IBehaviorEvaluator { entity: EntityMob? -> !isBaby() }
                    ), 3, 1),
                Behavior(
                    VillagerBreedingExecutor(EntityVillagerV2::class.java, 16, 100, 0.5f),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.ENTITY_SPOUSE),
                    2,
                    1
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(
                ISensor { entity: EntityMob ->
                    if (level!!.tick % 120 == 0) {
                        if (memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.OCCUPIED_BED)) {
                            val range = 48
                            val lookY = 5
                            var block: BlockBed? = null
                            for (x in -range..range) {
                                for (z in -range..range) {
                                    for (y in -lookY..lookY) {
                                        val lookTransform =
                                            entity.transform.add(x.toDouble(), y.toDouble(), z.toDouble())
                                        val lookBlock = lookTransform.levelBlock
                                        if (lookBlock is BlockBed) {
                                            if (!lookBlock.isHeadPiece && Arrays.stream<Entity>(
                                                    level!!.entities
                                                ).noneMatch { entity1: Entity ->
                                                    entity1 is EntityVillagerV2 && entity1.memoryStorage!!
                                                        .notEmpty(CoreMemoryTypes.Companion.OCCUPIED_BED) && entity1.getBed() == lookBlock
                                                }
                                            ) {
                                                block = lookBlock.footPart
                                            }
                                        }
                                    }
                                }
                            }
                            if (block != null && !block.isOccupied) setBed(block)
                        } else if (!memoryStorage!!.get<BlockBed>(CoreMemoryTypes.Companion.OCCUPIED_BED).isBedValid) {
                            namedTag!!.remove("bed")
                            memoryStorage!!.clear(CoreMemoryTypes.Companion.OCCUPIED_BED)
                        }
                    }
                },
                ISensor { entity: EntityMob ->
                    if (level!!.tick % 60 == 0) {
                        val entities = Arrays.stream(
                            entity.level!!.getCollidingEntities(
                                entity.getBoundingBox()!!.grow(64.0, 3.0, 64.0)
                            )
                        ).filter { entity1: Entity -> entity1 is EntityVillagerV2 && entity1 !== this }
                            .map { entity1: Entity -> (entity1 as EntityVillagerV2) }
                        if (level!!.dayTime > 8000 && level!!.dayTime < 10000) {
                            if (memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.GOSSIP_TARGET)) {
                                var minDistance = Float.MAX_VALUE.toDouble()
                                var nearest: EntityVillagerV2? = null
                                for (entity1 in entities.toList()) {
                                    val distance = entity1.position.distance(this.position)
                                    if (distance < minDistance) {
                                        minDistance = distance
                                        nearest = entity1
                                    }
                                }
                                if (nearest != null) {
                                    memoryStorage!!.put<EntityVillagerV2>(
                                        CoreMemoryTypes.Companion.GOSSIP_TARGET,
                                        nearest
                                    )
                                } else memoryStorage!!.clear(CoreMemoryTypes.Companion.GOSSIP_TARGET)
                            }
                        } else {
                            if (shouldShareFood()) {
                                entities.filter { entity1: EntityVillagerV2 ->
                                    entity1.isHungry() && entity1.position.distance(
                                        this.position
                                    ) < 16
                                }.findAny().ifPresentOrElse(
                                    { entity1: EntityVillagerV2 ->
                                        memoryStorage!!.put<EntityVillagerV2>(
                                            CoreMemoryTypes.Companion.GOSSIP_TARGET,
                                            entity1
                                        )
                                        entity1.memoryStorage!!
                                            .put<EntityVillagerV2>(CoreMemoryTypes.Companion.GOSSIP_TARGET, this)
                                    },
                                    { memoryStorage!!.clear(CoreMemoryTypes.Companion.GOSSIP_TARGET) })
                            } else if (!isHungry()) memoryStorage!!.clear(CoreMemoryTypes.Companion.GOSSIP_TARGET)
                        }
                    }
                },
                ISensor { entity: EntityMob? ->
                    if (level!!.tick % 30 == 0) {
                        if (!isBaby()) {
                            val siteBlock = memoryStorage!!.get<Block>(CoreMemoryTypes.Companion.SITE_BLOCK)
                            if (siteBlock != null) if (siteBlock.levelBlock.id != siteBlock.id) {
                                memoryStorage!!.clear(CoreMemoryTypes.Companion.SITE_BLOCK)
                            }
                            if (memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.SITE_BLOCK)) {
                                for (block in level!!.getCollisionBlocks(
                                    getBoundingBox()!!
                                        .grow(16.0, 4.0, 16.0)
                                )) {
                                    if (Arrays.stream<Entity>(level!!.entities).noneMatch { entity1: Entity ->
                                            entity1 is EntityVillagerV2 && entity1.memoryStorage!!
                                                .notEmpty(CoreMemoryTypes.Companion.SITE_BLOCK) && entity1.getSite() == block
                                        }) if (setProfessionBlock(block)) return@of
                                }
                                if (getTradeExp() == 0) setProfession(0, true)
                            }
                        }
                    }
                },
                ISensor { entity: EntityMob ->
                    if (level!!.tick % 100 == 0) {
                        if (memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.WILLING)) {
                            val entities = entity.level!!.entities
                            var maxDistanceSquared = -1.0
                            var nearestInLove: EntityVillagerV2? = null
                            for (e in entities) {
                                val newDistance = e.position.distanceSquared(entity.position)
                                if (e is EntityVillagerV2 && e !== entity) {
                                    if (!e.isBaby() && e.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.WILLING) && e.memoryStorage!!
                                            .isEmpty(CoreMemoryTypes.Companion.ENTITY_SPOUSE) && (maxDistanceSquared == -1.0 || newDistance < maxDistanceSquared)
                                    ) {
                                        maxDistanceSquared = newDistance
                                        nearestInLove = e
                                    }
                                }
                            }
                            if (nearestInLove != null) {
                                nearestInLove.memoryStorage!!
                                    .put<Entity>(CoreMemoryTypes.Companion.ENTITY_SPOUSE, this)
                                memoryStorage!!.put<Entity>(CoreMemoryTypes.Companion.ENTITY_SPOUSE, nearestInLove)
                            }
                        }
                    }
                },
                BlockSensor(BlockDoor::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK_2, 1, 0, 10),
                NearestEntitySensor(EntityZombie::class.java, CoreMemoryTypes.Companion.NEAREST_ZOMBIE, 8.0, 0.0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getFloatingHeight(): Float {
        return super.getFloatingHeight() * 0.7f
    }

    fun isHungry(): Boolean {
        return getFoodPoints() < 12
    }

    fun shouldShareFood(): Boolean {
        for (item in getInventory().contents.values) {
            if ((item.id == Item.BREAD && item.getCount() >= 6)
                || ((item.id == Item.CARROT || item.id == Block.BEETROOT) && item.getCount() >= 24)
                || (item.id == Block.WHEAT && item.getCount() >= 18 && getProfession() == 1)
            ) return true
        }
        return false
    }

    fun getFoodPoints(): Int {
        var points = 0
        for (item in getInventory().contents.values) {
            points += when (item.id) {
                Item.BREAD -> 4
                Item.CARROT, Item.POTATO, Block.BEETROOT -> 1
                else -> 0
            } * item.getCount()
        }
        return points
    }

    fun setBed(bed: BlockBed) {
        if (bed.isBedValid) {
            memoryStorage!!.put<BlockBed>(CoreMemoryTypes.Companion.OCCUPIED_BED, bed)
            for (i in 0..4) {
                val randX = Utils.rand(0f, 0.5f)
                val randY = Utils.rand(0f, 0.3f)
                val randZ = Utils.rand(0f, 0.5f)
                level!!.addParticleEffect(
                    position.add(
                        randX.toDouble(),
                        (this.eyeHeight + randY).toDouble(),
                        randZ.toDouble()
                    ), ParticleEffect.VILLAGER_HAPPY
                )
                level!!.addParticleEffect(
                    bed.position.add(
                        randX.toDouble(),
                        (0.5625f + randY).toDouble(),
                        randZ.toDouble()
                    ), ParticleEffect.VILLAGER_HAPPY
                )
            }
        }
    }

    fun getBed(): BlockBed? {
        return memoryStorage!!.get<BlockBed>(CoreMemoryTypes.Companion.OCCUPIED_BED)
    }

    fun getSite(): Block? {
        return memoryStorage!!.get<Block>(CoreMemoryTypes.Companion.SITE_BLOCK)
    }

    override fun getHeight(): Float {
        if (isSleeping()) return getWidthR()
        return getHeightR()
    }

    override fun getWidth(): Float {
        return getWidthR()
    }

    override fun getLength(): Float {
        if (isSleeping()) return getHeightR()
        return super.getLength()
    }

    private fun getWidthR(): Float {
        if (this.isBaby()) {
            return 0.3f
        }
        return 0.6f
    }

    private fun getHeightR(): Float {
        if (this.isBaby()) {
            return 0.95f
        }
        return 1.9f
    }

    fun isSleeping(): Boolean {
        return getDataFlag(EntityFlag.SLEEPING)
    }

    override fun getOriginalName(): String {
        return "VillagerV2"
    }

    public override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
        setTradingPlayer(0L)
        if (!namedTag!!.contains("tradeSeed")) {
            this.setTradeSeed(NukkitRandom().nextInt(Int.MAX_VALUE - 1))
        } else {
            this.tradeSeed = namedTag!!.getInt("tradeSeed")
        }
        if (!namedTag!!.contains("canTrade")) {
            this.setCanTrade(!(profession == 0 || profession == 14))
        } else {
            this.canTrade = namedTag!!.getBoolean("canTrade")
        }
        if (!namedTag!!.contains("displayName") && profession != 0) {
            this.setDisplayName(getProfessionName(profession)!!)
        } else {
            this.displayName = namedTag!!.getString("displayName")
        }
        if (!namedTag!!.contains("tradeTier")) {
            this.setTradeTier(1)
        } else {
            this.tradeTier = namedTag!!.getInt("tradeTier")
        }
        if (!namedTag!!.contains("maxTradeTier")) {
            this.setMaxTradeTier(5)
        } else {
            val maxTradeTier = namedTag!!.getInt("maxTradeTier")
            this.maxTradeTier = maxTradeTier
            this.setDataProperty(EntityDataTypes.Companion.MAX_TRADE_TIER, maxTradeTier)
        }
        if (!namedTag!!.contains("tradeExp")) {
            this.setTradeExp(0)
        } else {
            val tradeExp = namedTag!!.getInt("tradeExp")
            this.tradeExp = tradeExp
            this.setDataProperty(EntityDataTypes.Companion.TRADE_EXPERIENCE, tradeExp)
        }
        if (namedTag!!.containsInt("clothing")) {
            this.setDataProperty(EntityDataTypes.Companion.MARK_VARIANT, namedTag!!.getInt("clothing"))
        } else {
            val bv = position.asBlockVector3()
            this.setDataProperty(
                EntityDataTypes.Companion.MARK_VARIANT, Clothing.getClothing(
                    level!!.getBiomeId(bv.x, bv.y, bv.z)
                ).ordinal
            )
        }
        if (namedTag!!.containsCompound("bed")) {
            val compound = namedTag!!.getCompound("bed")
            val vector = Vector3(
                compound.getInt("x").toDouble(),
                compound.getInt("y").toDouble(),
                compound.getInt("z").toDouble()
            )
            if (level!!.getBlock(vector) is BlockBed) {
                setBed(bed)
            }
        }
        memoryStorage!!.put<Object2ObjectArrayMap<String, IntArrayList>>(
            CoreMemoryTypes.Companion.GOSSIP,
            Object2ObjectArrayMap<String, IntArrayList>()
        )
        if (namedTag!!.containsCompound("gossip")) {
            val gossipMap =
                memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
            val gossipTag = namedTag!!.getCompound("gossip")
            for (key in gossipTag.tags.keys) {
                val gossipValues = gossipTag.getList(key, IntTag::class.java)
                val valueMap = IntArrayList()
                for (i in 0..<gossipValues.size()) {
                    valueMap.add(i, gossipValues[i].getData())
                }
                gossipMap[key] = valueMap
            }
        }
        if (namedTag!!.containsString("purifyPlayer")) {
            val xuid = namedTag!!.removeAndGet<Tag>("purifyPlayer").parseValue<String>()
            this.addGossip(xuid, Gossip.MAJOR_POSITIVE, 20)
            this.addGossip(xuid, Gossip.MINOR_POSITIVE, 25)
        }
        if (namedTag!!.containsCompound("siteBlock")) {
            val tag = namedTag!!.getCompound("siteBlock")
            val vector3 = Vector3(tag.getInt("x").toDouble(), tag.getInt("y").toDouble(), tag.getInt("z").toDouble())
            val block = level!!.getBlock(vector3)
            setProfessionBlock(block)
        }
        if (namedTag!!.containsInt("profession")) {
            setProfession(namedTag!!.getInt("profession"), false)
        }
        this.inventory = EntityEquipmentInventory(this)
        if (namedTag!!.contains("Inventory") && namedTag!!["Inventory"] is ListTag<*>) {
            val inventory = this.getInventory()
            val inventoryList = namedTag!!.getList(
                "Inventory",
                CompoundTag::class.java
            )
            for (item in inventoryList.all) {
                val slot = item.getByte("Slot").toInt()
                inventory.setItem(slot, NBTIO.getItemHelper(item)) //inventory 0-39
            }
        }
        if (canTrade!!) {
            tradeInventory = TradeInventory(this)
        }
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (super.attack(source)) {
            if (source is EntityDamageByEntityEvent) {
                if (source.damager is Player) {
                    addGossip(player.getLoginChainData().getXUID(), Gossip.MINOR_NEGATIVE, 25)
                    val pk = EntityEventPacket()
                    pk.eid = getId()
                    pk.event = EntityEventPacket.VILLAGER_ANGRY
                    Server.broadcastPacket(viewers.values, pk)
                }
            }
            return true
        } else return false
    }

    override fun kill() {
        if (getLastDamageCause() is EntityDamageByEntityEvent) {
            if (event.getEntity() is Player) {
                println("1")
                Arrays.stream<Entity>(
                    level!!.getCollidingEntities(
                        getBoundingBox()!!.grow(16.0, 16.0, 16.0)
                    )
                ).filter { entity: Entity? -> entity is EntityVillagerV2 }.forEach { entity: Entity ->
                    (entity as EntityVillagerV2).addGossip(
                        player.getLoginChainData().getXUID(), Gossip.MAJOR_NEGATIVE, 25
                    )
                }
            }
        }
        super.kill()
    }

    fun addGossip(xuid: String, gossip: Gossip, value: Int) {
        val gossipMap =
            memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
        if (!gossipMap.containsKey(xuid)) gossipMap[xuid] = IntArrayList(
            Collections.nCopies(
                Gossip.VALUES.size, 0
            )
        )
        val values = gossipMap[xuid]
        val ordinal = gossip.ordinal
        values!![ordinal] = min(gossip.max.toDouble(), (values.getInt(ordinal) + value).toDouble()).toInt()
        level!!.players.values.stream().filter { player: Player -> player.loginChainData.xuid == xuid }.findFirst()
            .ifPresent { player: Player ->
                this.updateTrades(
                    player
                )
            }
    }

    fun spreadGossip() {
        Arrays.stream<Entity>(level!!.getCollidingEntities(getBoundingBox()!!.grow(2.0, 0.0, 2.0)))
            .filter { entity2: Entity? -> entity2 is EntityVillagerV2 }
            .map<EntityVillagerV2> { entity2: Entity -> (entity2 as EntityVillagerV2) }
            .forEach { target: EntityVillagerV2 ->
                val gossipMap =
                    memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
                val targetGossipMap = target.memoryStorage!!
                    .get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
                for ((xuid, value) in gossipMap.object2ObjectEntrySet()) {
                    if (!targetGossipMap.containsKey(xuid)) targetGossipMap[xuid] = IntArrayList(
                        Collections.nCopies(
                            Gossip.VALUES.size, 0
                        )
                    )
                    val targetValues = targetGossipMap[xuid]
                    for (gossip in Gossip.VALUES) {
                        val ordinal = gossip.ordinal
                        targetValues!![ordinal] = max(
                            targetValues.getInt(ordinal).toDouble(),
                            (value.getInt(ordinal) - gossip.penalty).toDouble()
                        ).toInt()
                    }
                }
            }
    }

    fun getGossip(xuid: String, gossip: Gossip): Int {
        val gossipMap =
            memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
        if (!gossipMap.containsKey(xuid)) gossipMap[xuid] = IntArrayList(
            Collections.nCopies(
                Gossip.VALUES.size,
                0
            )
        )
        val values = gossipMap[xuid]
        val ordinal = gossip.ordinal
        return values!!.getInt(ordinal) * gossip.multiplier
    }

    fun getReputation(player: Player): Int {
        var reputation = 0
        val values = memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(
            CoreMemoryTypes.Companion.GOSSIP
        )[player.loginChainData.xuid]
        if (values != null) {
            for (gossip in Gossip.VALUES) {
                reputation += (values.getInt(gossip.ordinal) * gossip.multiplier)
            }
        }
        return reputation
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag!!.putInt("profession", this.getProfession())
        namedTag!!.putBoolean("isTrade", this.canTrade())
        namedTag!!.putString("displayName", getDisplayName()!!)
        namedTag!!.putInt("tradeTier", this.getTradeTier())
        namedTag!!.putInt("maxTradeTier", this.getMaxTradeTier())
        namedTag!!.putInt("tradeExp", this.getTradeExp())
        namedTag!!.putInt("tradeSeed", this.getTradeSeed())
        namedTag!!.putInt("clothing", this.getDataProperty<Int>(EntityDataTypes.Companion.MARK_VARIANT!!))
        val gossipTag = CompoundTag()
        for ((key, value) in memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
            .object2ObjectEntrySet()) {
            val gossipValues = ListTag<IntTag>()
            for (v2 in value) {
                gossipValues.add(IntTag(v2))
            }
            gossipTag.putList(key, gossipValues)
        }
        namedTag!!.putCompound("gossip", gossipTag)
        if (memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.OCCUPIED_BED)) {
            val bed = memoryStorage!!.get<BlockBed>(CoreMemoryTypes.Companion.OCCUPIED_BED)
            namedTag!!.putCompound(
                "bed",
                CompoundTag().putInt("x", bed!!.position.floorX).putInt("y", bed.position.floorY)
                    .putInt("z", bed.position.floorZ)
            )
        }
        if (memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.SITE_BLOCK)) {
            val site = memoryStorage!!.get<Block>(CoreMemoryTypes.Companion.SITE_BLOCK)
            namedTag!!.putCompound(
                "siteBlock",
                CompoundTag().putInt("x", site!!.position.floorX).putInt("y", site.position.floorY)
                    .putInt("z", site.position.floorZ)
            )
        }
        var inventoryTag: ListTag<CompoundTag?>? = null
        if (this.getInventory() != null) {
            inventoryTag = ListTag()
            namedTag!!.putList("Inventory", inventoryTag)
            for ((key, value) in getInventory().contents) {
                inventoryTag.add(NBTIO.putItemHelper(value, key))
            }
        }
    }

    /**
     * 获取村民职业id对应的displayName硬编码
     */
    private fun getProfessionName(profession: Int): String? {
        return when (profession) {
            1 -> "entity.villager.farmer"
            2 -> "entity.villager.fisherman"
            3 -> "entity.villager.shepherd"
            4 -> "entity.villager.fletcher"
            5 -> "entity.villager.librarian"
            6 -> "entity.villager.cartographer"
            7 -> "entity.villager.cleric"
            8 -> "entity.villager.armor"
            9 -> "entity.villager.weapon"
            10 -> "entity.villager.tool"
            11 -> "entity.villager.butcher"
            12 -> "entity.villager.leather"
            13 -> "entity.villager.mason"
            else -> null
        }
    }

    /**
     * @return 村民的职业id
     */
    fun getProfession(): Int {
        return profession
    }


    fun setProfession(profession: Int, apply: Boolean) {
        this.profession = profession
        this.setDataProperty(EntityDataTypes.Companion.VARIANT, profession)
        if (apply) applyProfession()
    }

    fun setProfessionBlock(block: Block): Boolean {
        for (profession in Profession.Companion.getProfessions().values) {
            if (getTradeExp() != 0 && profession.index != getProfession()) continue
            if (block.id == profession.blockID) {
                memoryStorage!!.put<Block>(CoreMemoryTypes.Companion.SITE_BLOCK, block)
                setProfession(profession.index, true)
                return true
            }
        }
        return false
    }

    /**
     * 这个方法插件一般不用
     */
    fun setTradingPlayer(eid: Long?) {
        this.setDataProperty(EntityDataTypes.Companion.TRADE_TARGET_EID, eid!!)
    }

    /**
     * @return 该村民是否可以交易
     */
    fun canTrade(): Boolean {
        return canTrade!!
    }

    /**
     * 设置村民是否可以交易
     *
     * @param canTrade true 可以交易
     */
    fun setCanTrade(canTrade: Boolean) {
        this.canTrade = canTrade
        namedTag!!.putBoolean("canTrade", canTrade)
    }

    /**
     * @return 交易UI的显示名称
     */
    fun getDisplayName(): String? {
        return displayName
    }

    /**
     * @param displayName 设置交易UI的显示名称
     */
    fun setDisplayName(displayName: String) {
        this.displayName = displayName
        namedTag!!.putString("displayName", displayName)
    }

    /**
     * @return 该村民的交易等级
     */
    fun getTradeTier(): Int {
        return tradeTier
    }

    /**
     * @param tradeTier
     *
     *村民的交易等级(1-[EntityVillagerV2.maxTradeTier])
     */
    fun setTradeTier(tradeTier: Int) {
        var tradeTier = tradeTier
        this.tradeTier = --tradeTier
        namedTag!!.putInt("tradeTier", this.tradeTier)
    }

    fun updateTrades(player: Player) {
        if (player.topWindow.isEmpty || player.topWindow.get() !== getTradeInventory()) return
        val pk1 = UpdateTradePacket()
        pk1.containerId = player.getWindowId(getTradeInventory()!!).toByte()
        pk1.tradeTier = getTradeTier()
        pk1.traderUniqueEntityId = getId()
        pk1.playerUniqueEntityId = player.id
        pk1.displayName = getDisplayName()
        val tierExpRequirements = ListTag<CompoundTag>()
        var i = 0
        val len = tierExpRequirement.size
        while (i < len) {
            tierExpRequirements.add(i, CompoundTag().putInt(i.toString(), tierExpRequirement[i]))
            ++i
        }
        val recipes = getRecipes().copy() as ListTag<CompoundTag>
        val reputation = getReputation(player)
        for (tag in recipes.all) {
            if (tag.containsCompound("buyA")) {
                val buyA = tag.getCompound("buyA")
                var multiplier = 0f
                if (tag.containsFloat("priceMultiplierA")) multiplier = tag.getFloat("priceMultiplierA")
                buyA.putByte(
                    "Count",
                    max((buyA.getByte("Count") - (reputation * multiplier).toInt()).toDouble(), 1.0).toInt()
                )
            }
            if (tag.containsCompound("buyB")) {
                val buyB = tag.getCompound("buyB")
                var multiplier = 0f
                if (tag.containsFloat("priceMultiplierB")) multiplier = tag.getFloat("priceMultiplierB")
                buyB.putByte(
                    "Count",
                    max((buyB.getByte("Count") - (reputation * multiplier).toInt()).toDouble(), 1.0).toInt()
                )
            }
        }
        pk1.offers = CompoundTag()
            .putList("Recipes", recipes)
            .putList("TierExpRequirements", tierExpRequirements)
        pk1.newTradingUi = true
        pk1.usingEconomyTrade = true
        player.dataPacket(pk1)
    }

    /**
     * @return 村民所允许的最大交易等级
     */
    fun getMaxTradeTier(): Int {
        return maxTradeTier
    }

    /**
     * @param maxTradeTier 设置村民所允许的最大交易等级
     */
    fun setMaxTradeTier(maxTradeTier: Int) {
        this.maxTradeTier = maxTradeTier
        this.setDataProperty(EntityDataTypes.Companion.MAX_TRADE_TIER, 5)
        namedTag!!.putInt("maxTradeTier", this.tradeTier)
    }

    override fun close() {
        getTradeNetIds().forEach(Consumer { key: Int? -> TradeRecipeBuildUtils.RECIPE_MAP.remove(key) })
        super.close()
    }

    /**
     * @return 村民当前的经验值
     */
    fun getTradeExp(): Int {
        return tradeExp
    }

    /**
     * @param tradeExp 设置村民当前的经验值
     */
    fun setTradeExp(tradeExp: Int) {
        this.tradeExp = tradeExp
        this.setDataProperty(EntityDataTypes.Companion.TRADE_EXPERIENCE, 10)
        namedTag!!.putInt("tradeExp", this.tradeTier)
    }


    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (this.canTrade()) {
            player.addWindow(getTradeInventory()!!)
            return true
        } else return false
    }

    fun getTradeInventory(): TradeInventory? {
        return tradeInventory
    }

    override fun getInventory(): EntityEquipmentInventory {
        return inventory!!
    }

    fun getTradeSeed(): Int {
        return tradeSeed
    }

    protected fun setTradeSeed(tradeSeed: Int) {
        this.tradeSeed = tradeSeed
        namedTag!!.putInt("tradeSeed", tradeSeed)
    }

    fun addExperience(xp: Int) {
        this.tradeExp += xp
        this.setDataProperty(EntityDataTypes.Companion.TRADE_EXPERIENCE, this.tradeExp)
        val next = getTradeTier() + 1
        if (next < tierExpRequirement.size) {
            if (tradeExp >= tierExpRequirement[next]) {
                setTradeTier(next + 1)
            }
        }
    }

    override fun onUpdate(tick: Int): Boolean {
        if (ticksLived % 24000 == 23999) {
            for ((_, values) in memoryStorage!!.get<Object2ObjectArrayMap<String, IntArrayList>>(CoreMemoryTypes.Companion.GOSSIP)
                .object2ObjectEntrySet()) {
                for (gossip in Gossip.VALUES) {
                    values[gossip.ordinal] =
                        max(0.0, (values.getInt(gossip.ordinal) - gossip.decay).toDouble()).toInt()
                }
            }
        }

        if (tick % 20 == 0) {
            for (i in level!!.getNearbyEntities(getBoundingBox()!!.grow(1.0, 0.5, 1.0))) {
                if (i is EntityItem) {
                    val item = i.item
                    if (when (item!!.id) {
                            Item.BREAD, Item.CARROT, Item.POTATO, BlockID.WHEAT, Item.WHEAT_SEEDS, Item.BEETROOT_SEEDS, BlockID.BEETROOT, Item.TORCHFLOWER_SEEDS, Item.PITCHER_POD, Item.BONE_MEAL -> true
                            else -> false
                        }
                    ) {
                        val slice = InventorySlice(getInventory(), 1, getInventory().size)
                        if (slice.canAddItem(item)) {
                            val pk = TakeItemEntityPacket()
                            pk.entityId = this.getId()
                            pk.target = i.getId()
                            Server.broadcastPacket(viewers.values, pk)
                            slice.addItem(item)
                            i.close()
                        }
                    }
                }
            }
        }
        return super.onUpdate(tick)
    }

    fun applyProfession() {
        if (this.profession == 0) {
            this.setCanTrade(false)
        } else {
            getTradeNetIds().forEach(Consumer { key: Int? -> TradeRecipeBuildUtils.RECIPE_MAP.remove(key) })
            val profession: Profession = Profession.Companion.getProfession(this.profession)
            setDisplayName(profession.name)
            for (trade in profession.buildTrades(getTradeSeed()).all) {
                getTradeNetIds().add(trade.getInt("netId"))
            }
            this.setCanTrade(true)
            if (tradeInventory == null) {
                tradeInventory = TradeInventory(this)
            }
        }
    }

    override fun getExperienceDrops(): Int {
        return 0
    }

    enum class Clothing {
        PLAINS,
        DESERT,
        JUNGLE,
        SAVANNA,
        SNOW,
        SWAMP,
        TAIGA;

        companion object {
            fun getClothing(biomeId: Int): Clothing {
                val definition = Registries.BIOME[biomeId]
                val tags = definition.tags
                if (tags.contains("desert") || tags.contains("mesa")) return DESERT
                if (tags.contains("jungle")) return JUNGLE
                if (tags.contains("savanna")) return SAVANNA
                if (tags.contains("frozen")) return SNOW
                if (tags.contains("swamp")) return SWAMP
                if (tags.contains("taiga") || tags.contains("extreme_hills")) return TAIGA
                return PLAINS
            }
        }
    }

    enum class Gossip(val gain: Int, val decay: Int, val penalty: Int, val max: Int, val multiplier: Int) {
        MAJOR_POSITIVE(20, 0, 100, 20, 5),
        MINOR_POSITIVE(25, 1, 5, 200, 1),
        MINOR_NEGATIVE(25, 20, 20, 200, -1),
        MAJOR_NEGATIVE(25, 10, 10, 100, -5),
        TRADING(2, 2, 20, 25, 1);

        companion object {
            val VALUES: Array<Gossip> = entries.toTypedArray()
        }
    }
}
