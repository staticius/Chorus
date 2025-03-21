package org.chorus.entity.mob

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.ConditionalProbabilityEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.executor.EntityMoveToOwnerExecutor
import org.chorus.entity.ai.executor.LookAtTargetExecutor
import org.chorus.entity.ai.executor.MoveToTargetExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestItemSensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.item.EntityItem
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag


import java.util.Set
import java.util.function.Function

class EntityAllay(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityFlyable, EntityOwnable,
    EntityInventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.Companion.ALLAY
    }


    private var lastItemDropTick: Int = -1


    var dropCollectCooldown: Int = 60


    override fun initEntity() {
        super.initEntity()
        updateMemory()
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_ITEM, 0.22f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_ITEM),
                    5,
                    1
                ),
                Behavior(EntityMoveToOwnerExecutor(0.4f, true, 64, -1), IBehaviorEvaluator { entity: EntityMob? ->
                    if (this.hasOwner()) {
                        val player: Player? = getOwner()
                        val distanceSquared: Double = position.distanceSquared(player!!.position)
                        return@IBehaviorEvaluator distanceSquared >= 100
                    } else return@IBehaviorEvaluator false
                }, 4, 1),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ConditionalProbabilityEvaluator(
                        3, 7,
                        Function<Entity, Boolean> { entity: Entity? -> hasOwner(false) }, 10
                    ),
                    1,
                    1,
                    25
                ),
                Behavior(
                    SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(
                NearestItemSensor(32.0, 0.0, 20),
                NearestPlayerSensor(64.0, 0.0, 20)
            ),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun getHeight(): Float {
        return 0.6f
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getOriginalName(): String {
        return "Allay"
    }

    override fun getExperienceDrops(): Int {
        return 0
    }

    override fun onInteract(player: Player?, item: Item, clickedPos: Vector3): Boolean {
        if (player != null) {
            if (item.isNull()) {
                setOwnerName(null)
                setItemInHand(Item.AIR)
            } else {
                setOwnerName(player.getName())
                val itemInHand: Item = player.getInventory().getItemInHand().clone().clearNamedTag()
                itemInHand.setCount(1)
                setItemInHand(itemInHand)
            }
            updateMemory()
        }
        return super.onInteract(player!!, item, clickedPos)
    }

    private fun updateMemory() {
        val item: Item = getItemInHand()
        if (item.isNull()) {
            getMemoryStorage().clear(CoreMemoryTypes.Companion.LOOKING_ITEM)
        } else getMemoryStorage().put<Class<out Item>>(CoreMemoryTypes.Companion.LOOKING_ITEM, item.javaClass)
    }

    override fun getInventory(): Inventory {
        //0 = hand, 1 = offhand
        return InventorySlice(equipment, 2, 3) // TODO
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (currentTick % 10 == 0) {
            val nearestItem: EntityItem = getMemoryStorage().get<EntityItem>(CoreMemoryTypes.Companion.NEAREST_ITEM)
            if (nearestItem != null && !nearestItem.closed) {
                if (nearestItem.position.distance(this.position) < 1 && currentTick - lastItemDropTick > dropCollectCooldown) {
                    val item: Item? = nearestItem.getItem()
                    val currentItem: Item = getInventory().getItem(0).clone()
                    if (getInventory().canAddItem(item)) {
                        if (currentItem.isNull()) {
                            getInventory().setItem(0, item)
                        } else {
                            item!!.setCount(item.getCount() + currentItem.getCount())
                            getInventory().setItem(0, item)
                        }
                        level!!.addSound(this.position, Sound.RANDOM_POP)
                        nearestItem.close()
                    }
                }
            } else {
                if (hasOwner()) {
                    if (position.distance(getOwner()!!.position) < 2) {
                        dropItem(currentTick)
                    }
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    private fun dropItem(currentTick: Int): Boolean {
        if (!this.isAlive()) {
            return false
        }
        val item: Item = getInventory().getItem(0)
        if (item.isNull()) return true
        val motion: Vector3 = getDirectionVector().multiply(0.4)
        level!!.dropItem(position.add(0.0, 1.3, 0.0), item, motion, 40)
        getInventory().clearAll()
        this.lastItemDropTick = currentTick
        return true
    }

    override fun getDrops(): Array<Item> {
        return getInventory().contents.values.toTypedArray()
    }

    companion object {
        const val TAG_ALLAY_DUPLICATION_COOLDOWN: String = "AllayDuplicationCooldown"
        const val TAG_VIBRATION_LISTENER: String = "VibrationListener"
    }
}
