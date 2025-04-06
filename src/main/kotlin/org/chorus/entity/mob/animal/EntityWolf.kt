package org.chorus.entity.mob.animal

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.level.particle.ItemBreakParticle
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.EntityEventPacket
import org.chorus.utils.*
import java.util.List
import java.util.Set
import java.util.function.Function
import kotlin.math.max

/**
 * @author BeYkeRYkt (Nukkit Project)
 * @author Cool_Loong (PowerNukkitX Project)
 * todo 野生狼不会被刷新
 */
class EntityWolf(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable, EntityOwnable,
    EntityCanAttack, EntityCanSit, EntityAngerable, EntityHealable, EntityColor {
    override fun getIdentifier(): String {
        return EntityID.WOLF
    }

    override var diffHandDamage: FloatArray = floatArrayOf(3f, 4f, 6f)

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(
                            CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME,
                            6000,
                            Int.MAX_VALUE
                        ),  //只有拥有主人的狼才能交配
                        //Only wolves with a master can mate
                        IBehaviorEvaluator { entity: EntityMob? -> this.hasOwner() }
                    ),
                    1, 1
                ),  //刷新攻击目标
                Behavior(
                    { entity: EntityMob? ->
                        val storage = memoryStorage
                        val hasOwner = hasOwner()
                        var attackTarget: Entity? = null
                        val attackEvent = storage.get<EntityDamageEvent>(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT)
                        var attackByEntityEvent: EntityDamageByEntityEvent? = null
                        if (attackEvent is EntityDamageByEntityEvent) attackByEntityEvent = attackEvent
                        val validAttacker =
                            attackByEntityEvent != null && attackByEntityEvent.damager.isAlive && (attackByEntityEvent.damager !is Player || player.isSurvival())
                        if (hasOwner) {
                            //已驯服
                            if (storage.notEmpty(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER) && storage.get<Entity>(
                                    CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER
                                ).isAlive && (storage.get<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER) != this)
                            ) {
                                //攻击攻击主人的生物(排除自己)
                                attackTarget = storage.get<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER)
                                storage.clear(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER)
                            } else if (storage.notEmpty(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER) && storage.get<Entity>(
                                    CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER
                                ).isAlive && (storage.get<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER) != this)
                            ) {
                                //攻击主人攻击的生物
                                attackTarget = storage.get<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
                                storage.clear(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
                            } else if (attackByEntityEvent != null && validAttacker && (attackByEntityEvent.damager != owner)) {
                                //攻击攻击自己的生物（主人例外）
                                attackTarget = attackByEntityEvent.damager
                                storage.clear(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT)
                            } else if (storage.notEmpty(CoreMemoryTypes.Companion.NEAREST_SKELETON) && storage.get<Entity>(
                                    CoreMemoryTypes.Companion.NEAREST_SKELETON
                                ).isAlive
                            ) {
                                //攻击最近的骷髅
                                attackTarget = storage.get<Entity>(CoreMemoryTypes.Companion.NEAREST_SKELETON)
                                storage.clear(CoreMemoryTypes.Companion.NEAREST_SKELETON)
                            }
                        } else {
                            //未驯服
                            if (validAttacker) {
                                //攻击攻击自己的生物
                                attackTarget = attackByEntityEvent!!.damager
                                storage.clear(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT)
                            } else if (storage.notEmpty(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET) && storage.get<Entity>(
                                    CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET
                                ).isAlive
                            ) {
                                //攻击最近的合适生物
                                attackTarget =
                                    storage.get<Entity>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET)
                                storage.clear(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET)
                            }
                        }
                        storage.set<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, attackTarget)
                        false
                    },
                    { entity: EntityMob? -> this.memoryStorage.isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET) }, 1
                )
            ),
            Set.of<IBehavior>( //坐下锁定
                Behavior(
                    { entity: EntityMob? -> false },
                    { entity: EntityMob? -> this.isSitting }, 7
                ),  //攻击仇恨目标 todo 召集同伴
                Behavior(
                    WolfAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.7f, 33, true, 20),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    6, 1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityWolf>(EntityWolf::class.java, 16, 100, 0.35f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    5,
                    1
                ),
                Behavior(EntityMoveToOwnerExecutor(0.7f, true, 15), IBehaviorEvaluator { entity: EntityMob? ->
                    if (this.hasOwner()) {
                        val player = owner
                        if (!player!!.isOnGround) return@IBehaviorEvaluator false
                        val distanceSquared = position.distanceSquared(player!!.position)
                        return@IBehaviorEvaluator distanceSquared >= 100
                    } else return@IBehaviorEvaluator false
                }, 4, 1),
                Behavior(
                    LookAtFeedingPlayerExecutor(),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    3,
                    1
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ConditionalProbabilityEvaluator(
                        3, 7,
                        { entity: Entity? -> hasOwner(false) }, 10
                    ),
                    1,
                    1,
                    25
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 150, false, -1, true, 10),
                    ProbabilityEvaluator(5, 10), 1, 1, 50
                )
            ),
            Set.of<ISensor>(
                WolfNearestFeedingPlayerSensor(7.0, 0.0),
                NearestPlayerSensor(8.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 20.0, 20,
                    List.of<MemoryType<Entity?>?>(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        CoreMemoryTypes.Companion.NEAREST_SKELETON
                    ),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    },
                    Function<Entity, Boolean> { entity: Entity ->
                        when (entity.getIdentifier().toString()) {
                            EntityID.SKELETON, EntityID.WITHER_SKELETON, EntityID.STRAY -> true
                            else -> false
                        }
                    }),
                EntityAttackedByOwnerSensor(5, false)
            ),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        if (isBaby()) {
            return 0.3f
        }
        return 0.6f
    }

    override fun getHeight(): Float {
        if (isBaby()) {
            return 0.425f
        }
        return 0.8f
    }

    override fun getOriginalName(): String {
        return "Wolf"
    }

    public override fun initEntity() {
        this.maxHealth = 8
        super.initEntity()
        //update CollarColor to Color
        if (namedTag!!.contains("CollarColor")) {
            this.setColor(DyeColor.getByWoolData(namedTag!!.getByte("CollarColor").toInt()))
        }
    }

    override fun onUpdate(currentTick: Int): Boolean {
        //同步owner eid
        if (hasOwner()) {
            val owner = owner
            if (owner != null && getDataProperty<Long>(EntityDataTypes.Companion.OWNER_EID) != owner.id) {
                this.setDataProperty(EntityDataTypes.Companion.OWNER_EID, owner.id)
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (item.id === ItemID.NAME_TAG && !player.isAdventure) {
            return playerApplyNameTag(player, item)
        }

        val healable = this.getHealingAmount(item)
        //对于狼，只有骨头才能驯服，故此需要特判
        if (item.id === ItemID.BONE) {
            if (!this.hasOwner() && !this.isAngry()) {
                player.inventory.decreaseCount(player.inventory.heldItemIndex)
                if (Utils.rand(1, 3) == 3) {
                    val packet = EntityEventPacket()
                    packet.eid = this.getRuntimeID()
                    packet.event = EntityEventPacket.TAME_SUCCESS
                    player.dataPacket(packet)

                    this.maxHealth = 20
                    this.setHealth(20f)
                    this.ownerName = player.getName()
                    this.setColor(DyeColor.RED)
                    this.saveNBT()

                    level!!.dropExpOrb(this.position, Utils.rand(1, 7))

                    return true
                } else {
                    val packet = EntityEventPacket()
                    packet.eid = this.getRuntimeID()
                    packet.event = EntityEventPacket.TAME_FAIL
                    player.dataPacket(packet)
                }
            }
        } else if (item is ItemDye) {
            if (this.hasOwner() && player == this.owner) {
                player.inventory.decreaseCount(player.inventory.heldItemIndex)
                this.setColor(item.dyeColor)
                return true
            }
        } else if (this.isBreedingItem(item)) {
            player.inventory.decreaseCount(player.inventory.heldItemIndex)
            level!!.addSound(this.position, Sound.RANDOM_EAT)
            level!!.addParticle(
                ItemBreakParticle(
                    position.add(0.0, (height * 0.75f).toDouble(), 0.0),
                    Item.get(item.id, 0, 1)
                )
            )

            if (healable != 0) {
                this.setHealth(max(maxHealth.toDouble(), (this.getHealth() + healable).toDouble()).toFloat())
            }

            memoryStorage.set<Player>(CoreMemoryTypes.Companion.LAST_FEED_PLAYER, player)
            memoryStorage.set<Int>(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, level!!.tick)
            return true
        } else if (this.hasOwner() && player.getName() == ownerName && !this.isTouchingWater) {
            this.isSitting = !this.isSitting
            return false
        }

        return false
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id === ItemID.CHICKEN || item.id === ItemID.COOKED_CHICKEN || item.id === ItemID.BEEF || item.id === ItemID.COOKED_BEEF || item.id === ItemID.MUTTON || item.id === ItemID.COOKED_MUTTON || item.id === ItemID.PORKCHOP || item.id === ItemID.COOKED_PORKCHOP || item.id === ItemID.RABBIT || item.id === ItemID.COOKED_RABBIT || item.id === ItemID.ROTTEN_FLESH
    }

    /**
     * 获得可以治疗狼的物品的治疗量
     */
    override fun getHealingAmount(item: Item): Int {
        return when (item.id) {
            ItemID.PORKCHOP, ItemID.BEEF, ItemID.RABBIT -> 3
            ItemID.COOKED_PORKCHOP, ItemID.COOKED_BEEF -> 8
            ItemID.COD, ItemID.SALMON, ItemID.CHICKEN, ItemID.MUTTON -> 2
            ItemID.TROPICAL_FISH, ItemID.PUFFERFISH -> 1
            ItemID.COOKED_COD, ItemID.COOKED_RABBIT -> 5
            ItemID.COOKED_SALMON, ItemID.COOKED_CHICKEN, ItemID.COOKED_MUTTON -> 6
            ItemID.ROTTEN_FLESH -> 4
            ItemID.RABBIT_STEW -> 10
            else -> 0
        }
    }


    //兔子、狐狸、骷髅及其变种、羊驼、绵羊和小海龟。然而它们被羊驼啐唾沫时会逃跑。
    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.RABBIT, EntityID.FOX, EntityID.SKELETON, EntityID.WITHER_SKELETON, EntityID.STRAY, EntityID.LLAMA, EntityID.SHEEP, EntityID.TURTLE -> true
            else -> false
        }
    }

    override fun getDiffHandDamage(): FloatArray {
        return diffHandDamage
    }
}
