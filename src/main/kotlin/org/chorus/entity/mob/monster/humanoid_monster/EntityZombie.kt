package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.Block
import org.chorus.block.BlockTurtleEgg
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.WalkController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.BlockSensor
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.item.EntityItem
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityTurtle
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.inventory.EntityInventoryHolder
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.TakeItemEntityPacket
import org.chorus.utils.Utils
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Consumer
import java.util.function.Function

open class EntityZombie(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable,
    EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.ZOMBIE
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(
                Behavior(
                    NearestBlockIncementExecutor(),
                    { entity: EntityMob? ->
                        !memoryStorage.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) && memoryStorage.get<Block>(
                            CoreMemoryTypes.Companion.NEAREST_BLOCK
                        ) is BlockTurtleEgg
                    }, 1, 1
                )
            ),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(
                        Sound.MOB_ZOMBIE_SAY,
                        if (isBaby()) 1.3f else 0.8f,
                        if (isBaby()) 1.7f else 1.2f,
                        1f,
                        1f
                    ), RandomSoundEvaluator(), 7, 1
                ),
                Behavior(
                    JumpExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.getCollisionBlocks()!!.stream().anyMatch { block: Block? -> block is BlockTurtleEgg }
                        }), 6, 1, 10
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK, 0.3f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                    5,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 0),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                BlockSensor(BlockTurtleEgg::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 11, 15, 10)
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (getHealth() - source.finalDamage <= 1) {
            if (source.cause == DamageCause.DROWNING) {
                transform()
                return true
            }
        }
        return super.attack(source)
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Zombie"
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun onUpdate(currentTick: Int): Boolean {
        //husk not burn
        if (this is EntityHusk) {
            return super.onUpdate(currentTick)
        }
        burn(this)
        if (currentTick % 20 == 0) {
            pickupItems(this)
        }
        return super.onUpdate(currentTick)
    }

    override fun getFloatingForceFactor(): Double {
        return 0.7
    }

    override fun getDrops(): Array<Item> {
        val drops = ThreadLocalRandom.current().nextFloat(100f)
        if (drops < 0.83) {
            return when (Utils.rand(0, 2)) {
                0 -> arrayOf<Item>(
                    Item.get(ItemID.IRON_INGOT, 0, 1),
                    Item.get(ItemID.ROTTEN_FLESH, 0, Utils.rand(0, 2))
                )

                1 -> arrayOf<Item>(Item.get(ItemID.CARROT, 0, 1), Item.get(ItemID.ROTTEN_FLESH, 0, Utils.rand(0, 2)))
                else -> arrayOf<Item>(Item.get(ItemID.POTATO, 0, 1), Item.get(ItemID.ROTTEN_FLESH, 0, Utils.rand(0, 2)))
            }
        }
        return arrayOf(Item.get(ItemID.ROTTEN_FLESH, 0, Utils.rand(0, 2)))
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.VILLAGER_V2, EntityID.SNOW_GOLEM, EntityID.IRON_GOLEM -> true
            EntityID.TURTLE -> entity is EntityTurtle && !entity.isBaby()
            else -> false
        }
    }

    protected open fun transform() {
        this.close()
        equipment.contents.values.forEach(Consumer { i -> level!!.dropItem(this.position, i) })
        val drowned = EntityDrowned(this.locator.chunk, this.namedTag)
        drowned.setPosition(this.position)
        drowned.setRotation(rotation.yaw, rotation.pitch)
        drowned.spawnToAll()
        drowned.namedTag!!.putBoolean("Transformed", true)
        drowned.level!!.addSound(drowned.position, Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED)
    }

    companion object {
        fun pickupItems(entity: Entity) {
            if (entity is EntityInventoryHolder) {
                for (i in entity.level!!.getNearbyEntities(
                    entity.getBoundingBox().grow(1.0, 0.5, 1.0)
                )) {
                    if (i is EntityItem) {
                        val item = i.item
                        if (item.isArmor || item.isTool) {
                            if (entity.equip(item)) {
                                val pk = TakeItemEntityPacket()
                                pk.entityId = entity.getRuntimeID()
                                pk.target = i.getRuntimeID()
                                Server.broadcastPacket(entity.viewers.values, pk)
                                i.close()
                            }
                        }
                    }
                }
            }
        }
    }
}
