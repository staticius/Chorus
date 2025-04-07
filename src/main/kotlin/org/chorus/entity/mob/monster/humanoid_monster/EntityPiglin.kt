package org.chorus.entity.mob.monster.humanoid_monster

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
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.item.EntityItem
import org.chorus.entity.mob.EntityHoglin
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.EntityZoglin
import org.chorus.entity.mob.monster.EntityWither
import org.chorus.inventory.EntityInventoryHolder
import org.chorus.item.*
import org.chorus.level.*
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.AnimateEntityPacket.Animation
import org.chorus.network.protocol.LevelSoundEventPacket
import org.chorus.network.protocol.TakeItemEntityPacket
import org.chorus.utils.*
import java.util.*
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Consumer
import java.util.function.Function
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.forEach
import kotlin.collections.setOf

open class EntityPiglin(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.PIGLIN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    PiglinTransformExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob -> entity.level!!.dimension != Level.DIMENSION_NETHER },
                        IBehaviorEvaluator { entity: EntityMob? -> !isImmobile },
                        IBehaviorEvaluator { entity: EntityMob -> !entity.namedTag!!.getBoolean("IsImmuneToZombification") }
                    ), 13, 1),
                Behavior(
                    PiglinTradeExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> !itemInOffhand.isNothing },
                        IBehaviorEvaluator { entity: EntityMob? -> !isAngry() },
                        not(
                            all(
                                MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME),
                                IBehaviorEvaluator { entity: EntityMob ->
                                    entity.level!!.tick - memoryStorage.get<Int>(
                                        CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME
                                    ) <= 1
                                }
                            )
                        )
                    ), 12, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PIGLIN_JEALOUS, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob ->
                            viewers.values.stream().noneMatch { p: Player ->
                                p.position.distance(entity.position) < 8 && likesItem(p.inventory.itemInHand) && p.level.raycastBlocks(
                                    p.position,
                                    entity.position
                                ).isEmpty()
                            }
                        }), 12, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PIGLIN_ANGRY, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isAngry() }), 11, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PIGLIN_AMBIENT, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isAngry() }), 10, 1
                ),
                Behavior(
                    CrossBowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand() is ItemCrossbow }
                    ), 9, 1),
                Behavior(
                    CrossBowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.NEAREST_PLAYER,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            memoryStorage.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER) is Player && player.getInventory() != null && !Arrays.stream<Item>(
                                player.getInventory().getArmorContents()
                            )
                                .anyMatch { item: Item -> !item.isNothing && item is ItemArmor && item.getTier() == ItemArmor.TIER_GOLD }
                        },
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand() is ItemCrossbow }
                    ), 8, 1),
                Behavior(
                    CrossBowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob -> !entity.namedTag!!.getBoolean("CannotHunt") },
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand() is ItemCrossbow },
                        any(
                            IBehaviorEvaluator { entity: EntityMob? -> memoryStorage.get<Int>(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME) == 0 },
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME, 6000)
                        )
                    ), 7, 1
                ),
                Behavior(
                    PiglinMeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, 40, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET)
                    ), 6, 1
                ),
                Behavior(
                    PiglinMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.5f, 40, false, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            memoryStorage.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER) is Player && player.getInventory() != null && !Arrays.stream<Item>(
                                player.getInventory().getArmorContents()
                            )
                                .anyMatch { item: Item -> !item.isNothing && item is ItemArmor && item.getTier() == ItemArmor.TIER_GOLD }
                        }
                    ), 5, 1),
                Behavior(
                    PiglinMeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.5f,
                        40,
                        true,
                        30
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob -> !entity.namedTag!!.getBoolean("CannotHunt") },
                        any(
                            IBehaviorEvaluator { entity: EntityMob? -> memoryStorage.get<Int>(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME) == 0 },
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME, 6000)
                        )
                    ), 5, 1
                ),
                Behavior(
                    PiglinFleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                        IBehaviorEvaluator { entity: EntityMob? -> memoryStorage.get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK) is BlockSoulFire }
                    ), 2, 1),
                Behavior(
                    PiglinFleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            if (isBaby()) {
                                return@all true
                            } else {
                                val entity1 =
                                    memoryStorage.get<Entity>(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY)
                                return@all !(entity1 is EntityWither || entity1 is EntityWitherSkeleton)
                            }
                        }
                    ), 3, 1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                NearestPlayerAngryPiglinSensor(),
                NearestEntitySensor(
                    EntityZombiePigman::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    8.0,
                    0.0
                ),
                NearestEntitySensor(
                    EntityZoglin::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    8.0,
                    0.0
                ),
                NearestEntitySensor(
                    EntityWither::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    8.0,
                    0.0
                ),
                NearestEntitySensor(
                    EntityWitherSkeleton::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    8.0,
                    0.0
                ),
                BlockSensor(BlockDoor::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 2, 2, 20),
                BlockSensor(BlockSoulFire::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 8, 2, 20)
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }


    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (itemInOffhand.isNothing && !isAngry()) {
            if (item is ItemGoldIngot) {
                if (player.gamemode != Player.CREATIVE) player.inventory.decreaseCount(player.inventory.heldItemIndex)
                itemInOffhand = Item.get(ItemID.GOLD_INGOT)
            }
        }
        return super.onInteract(player, item)
    }

    override fun initEntity() {
        this.maxHealth = 16
        this.diffHandDamage = floatArrayOf(3f, 5f, 7f)
        super.initEntity()
        if (!isBaby()) setItemInHand(Item.get(if (Utils.rand()) Item.GOLDEN_SWORD else ItemID.CROSSBOW))
        if (Utils.rand(0, 10) == 0) helmet = Item.get(Item.GOLDEN_HELMET)
        if (Utils.rand(0, 10) == 0) chestplate =
            Item.get(Item.GOLDEN_CHESTPLATE)
        if (Utils.rand(0, 10) == 0) leggings =
            Item.get(Item.GOLDEN_LEGGINGS)
        if (Utils.rand(0, 10) == 0) boots = Item.get(Item.GOLDEN_BOOTS)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (currentTick % 20 == 0) {
            if (level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                pickupItems(this)
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Piglin"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return !this.isBaby()
    }

    fun isAngry(): Boolean {
        return getDataFlag(EntityFlag.ANGRY)
    }

    override fun getDrops(): Array<Item> {
        val drops: MutableList<Item> = ArrayList()
        if (ThreadLocalRandom.current().nextInt(200) < 17) { // 8.5%
            drops.add(getItemInHand())
            drops.addAll(equipment.armor)
        }
        drops.add(itemInOffhand)
        return drops.toTypedArray()
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.WITHER_SKELETON, EntityID.WITHER -> true
            EntityID.HOGLIN -> {
                if (entity is EntityHoglin) {
                    if (!entity.isBaby()) {
                        if (entity.getHealth() - getDiffHandDamage(Server.instance.difficulty) <= 0) {
                            val entities = Arrays.stream(
                                level!!.entities
                            ).filter { entity1: Entity ->
                                entity1 is EntityPiglin && entity1.position.distance(
                                    this.position
                                ) < 16
                            }.toList()
                            val builder = Animation.builder()
                            builder.animation("animation.piglin.celebrate_hunt_special")
                            builder.nextState("r")
                            builder.blendOutTime(1f)
                            Entity.Companion.playAnimationOnEntities(builder.build(), entities)
                            entities.forEach(Consumer { entity1: Entity ->
                                entity1.level!!.addSound(
                                    entity1.position,
                                    Sound.MOB_PIGLIN_CELEBRATE
                                )
                            })
                        }
                        true
                    }
                }
                false
            }

            else -> false
        }
    }

    fun pickupItems(entity: Entity) {
        if (!isAngry()) {
            if (entity is EntityInventoryHolder) {
                for (i in entity.level!!.getNearbyEntities(
                    entity.getBoundingBox().grow(1.0, 0.5, 1.0)
                )) {
                    var pickup = false
                    if (i is EntityItem) {
                        val item = i.item
                        if ((item.isArmor || item.isTool) && item.tier == ItemTool.TIER_GOLD) {
                            if (entity.equip(item)) {
                                pickup = true
                            }
                        } else if (item is ItemPorkchop) {
                            pickup = true
                        } else if (likesItem(item)) {
                            if (itemInOffhand.isNothing) {
                                itemInOffhand = item
                                pickup = true
                            }
                        }
                        if (pickup) {
                            val pk = TakeItemEntityPacket()
                            pk.entityId = entity.runtimeId
                            pk.target = i.getRuntimeID()
                            Server.broadcastPacket(entity.viewers.values, pk)
                            i.close()
                        }
                    }
                }
            }
        }
    }

    override fun getExperienceDrops(): Int {
        return Math.toIntExact(if (isBaby()) 1 else 5 + (equipment.armor.stream().filter { obj: Item -> obj.isArmor }
            .count() * ThreadLocalRandom.current().nextInt(1, 4)))
    }

    override fun equip(item: Item): Boolean {
        if ((item.tier > getItemInHand().tier && getItemInHand().tier != ItemArmor.TIER_GOLD) || item.tier == ItemArmor.TIER_GOLD) {
            level!!.dropItem(this.position, getItemInHand())
            this.setItemInHand(item)
            return true
        }
        return false
    }

    protected class PiglinFleeFromTargetExecutor(memory: MemoryType<out IVector3?>) :
        FleeFromTargetExecutor(memory, 0.5f, true, 8f) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            if (entity.position.distance(entity.memoryStorage.get(getMemory()).vector3) < 8) {
                entity.level!!.addSound(entity.position, Sound.MOB_PIGLIN_RETREAT)
            }
        }
    }

    protected class PiglinMeleeAttackExecutor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) :
        MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, entity.memoryStorage.get(memory).runtimeId)
            entity.setDataFlag(EntityFlag.ANGRY)
            entity.level!!.addLevelSoundEvent(
                entity.position,
                LevelSoundEventPacket.SOUND_ANGRY,
                -1,
                EntityID.PIGLIN,
                false,
                false
            )
            Arrays.stream<Entity>(entity.level!!.entities).filter { entity1: Entity ->
                entity1 is EntityPiglin && entity1.position.distance(entity.position) < 16 && entity1.memoryStorage
                    .isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)
            }.forEach { entity1: Entity ->
                (entity1 as EntityPiglin).memoryStorage
                    .set<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, entity.memoryStorage.get(memory))
            }
            if (entity.memoryStorage.get(memory) is EntityHoglin) {
                entity.memoryStorage.set<Int>(CoreMemoryTypes.Companion.LAST_HOGLIN_ATTACK_TIME, entity.level!!.tick)
            }
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

    companion object {
        fun likesItem(item: Item): Boolean {
            return when (item.id) {
                Block.BELL, Block.GOLD_BLOCK, ItemID.CLOCK, Item.ENCHANTED_GOLDEN_APPLE, Block.GILDED_BLACKSTONE, ItemID.GLISTERING_MELON_SLICE, ItemID.GOLD_INGOT, Item.GOLD_NUGGET, Block.GOLD_ORE, Item.GOLDEN_APPLE, Item.GOLDEN_AXE, Item.GOLDEN_BOOTS, ItemID.GOLDEN_CARROT, Item.GOLDEN_CHESTPLATE, Item.GOLDEN_HELMET, Item.GOLDEN_HOE, Item.GOLDEN_HORSE_ARMOR, Item.GOLDEN_LEGGINGS, Item.GOLDEN_PICKAXE, Item.GOLDEN_SHOVEL, Item.GOLDEN_SWORD, Block.LIGHT_WEIGHTED_PRESSURE_PLATE, Block.NETHER_GOLD_ORE, Block.GOLDEN_RAIL, Item.RAW_GOLD -> true
                else -> false
            }
        }
    }
}
