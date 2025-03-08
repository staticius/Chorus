package cn.nukkit.entity.mob

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.LookAtTargetExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.ShulkerAttackExecutor
import cn.nukkit.entity.ai.executor.ShulkerIdleExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.player.PlayerTeleportEvent.TeleportCause
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.utils.*
import java.util.*
import java.util.Set
import java.util.function.Consumer
import kotlin.collections.List

class EntityShulker(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityVariant {
    override var color: Int = 0

    override fun getIdentifier(): String {
        return EntityID.Companion.SHULKER
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
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
            Set.of<IBehavior>(
                Behavior(ShulkerIdleExecutor(), RandomSoundEvaluator(20, 10), 2, 1),
                Behavior(
                    ShulkerAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 16.0),
                        not(PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 60))
                    ), 1, 1
                )
            ),
            Set.of<ISensor>(NearestPlayerSensor(40.0, 0.0, 20)),
            Set.of<IController>(LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    fun isPeeking(): Boolean {
        return getDataProperty<Int>(EntityDataTypes.Companion.SHULKER_PEEK_AMOUNT!!, 0) == 0
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
        val block: Block = getLocator().getLevelBlock()
        if (!block.isAir() || block.down().isAir()) teleport()
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
        if (getMemoryStorage().get<Int?>(CoreMemoryTypes.Companion.VARIANT) == null) setVariant(16)
        setDataProperty(
            EntityDataTypes.Companion.SHULKER_ATTACH_POS,
            getLocator().getLevelBlock().getSide(BlockFace.UP).position.asBlockVector3()
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.SHULKER_SHELL, 0, Utils.rand(0, 2)))
    }

    fun teleport() {
        Arrays.stream(level!!.getCollisionBlocks(getBoundingBox()!!.grow(7.0, 7.0, 7.0)))
            .filter { block: Block -> block.isFullBlock() && block.up().isAir() }.findAny().ifPresent(
                Consumer { block: Block ->
                    val locator: Locator = block.up().getLocator()
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
