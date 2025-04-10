package org.chorus.entity.mob

import org.chorus.block.Block
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntityVariant
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.LookAtTargetExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.ShulkerAttackExecutor
import org.chorus.entity.ai.executor.ShulkerIdleExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.player.PlayerTeleportEvent.TeleportCause
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Locator
import org.chorus.level.Sound
import org.chorus.level.Transform
import org.chorus.level.format.IChunk
import org.chorus.math.BlockFace
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.LevelSoundEventPacket
import org.chorus.utils.Utils
import java.util.*
import java.util.function.Consumer

class EntityShulker(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityVariant {
    override var color: Byte = 0

    override fun getIdentifier(): String {
        return EntityID.SHULKER
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    2,
                    1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_SHULKER_AMBIENT, 0.8f, 1.2f, 0.8f, 0.8f),
                    RandomSoundEvaluator(20, 20),
                    1,
                    1
                )
            ),
            setOf<IBehavior>(
                Behavior(ShulkerIdleExecutor(), RandomSoundEvaluator(20, 10), 2, 1),
                Behavior(
                    ShulkerAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 16.0),
                        not(PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 60))
                    ), 1, 1
                )
            ),
            setOf<ISensor>(NearestPlayerSensor(40.0, 0.0, 20)),
            setOf<IController>(LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    fun isPeeking(): Boolean {
        return getDataProperty<Int>(EntityDataTypes.Companion.SHULKER_PEEK_AMOUNT, 0) == 0
    }

    fun setPeeking(height: Int) {
        setDataProperty(EntityDataTypes.Companion.SHULKER_PEEK_AMOUNT, height)
    }

    override fun getAdditionalArmor(): Int {
        return if (isPeeking()) 20 else 30
    }

    override fun onCollide(currentTick: Int, collidingEntities: List<Entity>): Boolean {
        collidingEntities.stream().filter { entity: Entity? -> entity is EntityProjectile }.forEach { entity: Entity ->
            entity.setMotion(entity.getMotion().multiply(-1.0))
        }
        return super.onCollide(currentTick, collidingEntities)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        val block: Block = getLocator().levelBlock
        if (!block.isAir || block.down().isAir) teleport()
        return super.onUpdate(currentTick)
    }

    //Shulker doesn't take knockback
    override fun knockBack(attacker: Entity?, damage: Double, x: Double, z: Double, base: Double) {}

    //Shulker doesn't move
    override fun updateMovement() {}

    override fun getGravity(): Float {
        return 0f
    }

    //Shulker cannot burn
    override fun setOnFire(seconds: Int) {}

    override fun attack(source: EntityDamageEvent): Boolean {
        if (getHealth() - source.getDamage() < getMaxHealth() / 2f) {
            if (Utils.rand(0, 4) == 0) {
                teleport()
                return true
            }
        }
        return super.attack(source)
    }

    override fun initEntity() {
        this.setMaxHealth(30)
        super.initEntity()
        if (getMemoryStorage()[CoreMemoryTypes.Companion.VARIANT] == null) setVariant(16)
        setDataProperty(
            EntityDataTypes.Companion.SHULKER_ATTACH_POS,
            getLocator().levelBlock.getSide(BlockFace.UP).position.asBlockVector3()
        )
    }

    override fun getWidth(): Float {
        return 0.99f
    }

    override fun getHeight(): Float {
        return 0.99f
    }

    override fun getOriginalName(): String {
        return "Shulker"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.SHULKER_SHELL, 0, Utils.rand(0, 2)))
    }

    fun teleport() {
        Arrays.stream(level!!.getCollisionBlocks(getBoundingBox().grow(7.0, 7.0, 7.0)))
            .filter { block: Block -> block.isFullBlock && block.up().isAir }.findAny().ifPresent(
                Consumer { block: Block ->
                    val locator: Locator = block.up().locator
                    level!!.addLevelSoundEvent(
                        this.position,
                        LevelSoundEventPacket.SOUND_TELEPORT,
                        -1,
                        getIdentifier(),
                        false,
                        false
                    )
                    teleport(locator, TeleportCause.SHULKER)
                    level!!.addLevelSoundEvent(
                        locator.position,
                        LevelSoundEventPacket.SOUND_SPAWN,
                        -1,
                        getIdentifier(),
                        false,
                        false
                    )
                }
            )
    }

    override fun teleport(transform: Transform, cause: TeleportCause?): Boolean {
        val superValue: Boolean = super.teleport(transform, cause)
        if (superValue) super.updateMovement()
        return superValue
    }

    override fun getAllVariant(): IntArray {
        return intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
    }
}
