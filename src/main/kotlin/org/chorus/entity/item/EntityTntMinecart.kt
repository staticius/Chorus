package org.chorus.entity.item

import org.chorus.Player
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityExplosionPrimeEvent
import org.chorus.item.*
import org.chorus.level.Explosion
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.types.EntityLink
import org.chorus.utils.MinecartType
import java.util.concurrent.*
import kotlin.math.sqrt

/**
 * @author Adam Matthew [larryTheCoder] (Nukkit Project)
 */
class EntityTntMinecart(chunk: IChunk?, nbt: CompoundTag) : EntityMinecartAbstract(chunk, nbt), EntityExplosive {
    override fun getIdentifier(): String {
        return EntityID.Companion.TNT_MINECART
    }

    private var fuse: Int = 0

    init {
        super.setDisplayBlock(Block.get(BlockID.TNT), false)
    }

    override fun isRideable(): Boolean {
        return false
    }

    override fun initEntity() {
        super.initEntity()

        if (namedTag!!.contains("TNTFuse")) {
            fuse = namedTag!!.getByte("TNTFuse").toInt()
        } else {
            fuse = 80
        }
        this.setDataFlag(EntityFlag.CHARGED, false)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        // 记录最大高度，用于计算坠落伤害
        if (!this.onGround && position.y > highestPosition) {
            this.highestPosition = position.y
        }
        if (fuse < 80) {
            val tickDiff: Int = currentTick - lastUpdate

            lastUpdate = currentTick

            if (fuse % 5 == 0) {
                setDataProperty(EntityDataTypes.Companion.FUSE_TIME, fuse)
            }

            fuse -= tickDiff

            if (isAlive() && fuse <= 0) {
                if (level!!.gameRules.getBoolean(GameRule.TNT_EXPLODES)) {
                    this.explode(ThreadLocalRandom.current().nextInt(5).toDouble())
                }
                this.close()
                return false
            }
        }

        return super.onUpdate(currentTick)
    }

    override fun updateFallState(onGround: Boolean) {
        if (onGround) {
            fallDistance = (this.highestPosition - position.y).toFloat()

            if (fallDistance > 4) {
                if (level!!.gameRules.getBoolean(GameRule.TNT_EXPLODES)) {
                    this.explode(ThreadLocalRandom.current().nextInt(5).toDouble())
                }
                this.resetFallDistance()
                this.close()
            }
        }
    }

    public override fun activate(x: Int, y: Int, z: Int, flag: Boolean) {
        level!!.addSound(this.position, Sound.FIRE_IGNITE)
        this.fuse = 79
    }

    override fun explode() {
        explode(0.0)
    }

    fun explode(square: Double) {
        var root: Double = sqrt(square)

        if (root > 5.0) {
            root = 5.0
        }

        val event: EntityExplosionPrimeEvent =
            EntityExplosionPrimeEvent(this, (4.0 + ThreadLocalRandom.current().nextDouble() * 1.5 * root))
        Server.instance.pluginManager.callEvent(event)
        if (event.isCancelled) {
            return
        }
        val explosion: Explosion = Explosion(this.getLocator(), event.force, this)
        explosion.fireChance = event.fireChance
        if (event.isBlockBreaking()) {
            explosion.explodeA()
        }
        explosion.explodeB()
        this.close()
    }

    override fun dropItem() {
        if (lastDamageCause is EntityDamageByEntityEvent) {
            val damager: Entity = lastDamageCause.damager
            if (damager is Player && damager.isCreative()) {
                return
            }
        }
        level!!.dropItem(this.position, ItemTntMinecart())
    }

    override fun getOriginalName(): String {
        return getType().getName()
    }

    override fun getType(): MinecartType {
        return MinecartType.valueOf(3)
    }

    override fun saveNBT() {
        super.saveNBT()

        super.namedTag!!.putInt("TNTFuse", this.fuse)
    }


    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        val interact: Boolean = super.onInteract(player, item, clickedPos)
        if (item.getId() == ItemID.FLINT_AND_STEEL || item.getId() == Item.FIRE_CHARGE) {
            level!!.addSound(this.position, Sound.FIRE_IGNITE)
            this.fuse = 79
            return true
        }

        return interact
    }

    override fun mountEntity(entity: Entity, mode: EntityLink.Type): Boolean {
        return false
    }
}
