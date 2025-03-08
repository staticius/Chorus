package cn.nukkit.entity.item

import cn.nukkit.Server
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.utils.DyeColor
import java.util.*
import java.util.concurrent.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * @author CreeperFace
 */
open class EntityFireworksRocket(chunk: IChunk?, nbt: CompoundTag) : Entity(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.FIREWORKS_ROCKET
    }

    private val lifetime: Int
    private var fireworkAge: Int = 0
    private var firework: Item? = null
    private var hadCollision: Boolean = false


    init {
        val rand: Random = ThreadLocalRandom.current()
        this.lifetime = 30 + rand.nextInt(12)

        motion.south = rand.nextGaussian() * 0.001
        motion.west = rand.nextGaussian() * 0.001
        motion.up = 0.05

        if (nbt.contains("FireworkItem")) {
            firework = NBTIO.getItemHelper(nbt.getCompound("FireworkItem"))
        } else {
            firework = ItemFireworkRocket()
        }

        if (!firework!!.hasCompoundTag() || !firework!!.getNamedTag()!!.contains("Fireworks")) {
            var tag: CompoundTag? = firework!!.getNamedTag()
            if (tag == null) {
                tag = CompoundTag()
            }

            val ex: CompoundTag = CompoundTag()
                .putByteArray("FireworkColor", byteArrayOf(DyeColor.BLACK.getDyeData().toByte()))
                .putByteArray("FireworkFade", byteArrayOf())
                .putBoolean("FireworkFlicker", false)
                .putBoolean("FireworkTrail", false)
                .putByte("FireworkType", ItemFireworkRocket.FireworkExplosion.ExplosionType.CREEPER_SHAPED.ordinal)

            tag.putCompound(
                "Fireworks", CompoundTag()
                    .putList("Explosions", ListTag<CompoundTag>().add(ex))
                    .putByte("Flight", 1)
            )

            firework!!.setNamedTag(tag)
        }

        //        this.setDataProperty(Entity.HORSE_FLAGS, firework.getNamedTag());//TODO FIXME
        this.setDataProperty(EntityDataTypes.Companion.DISPLAY_OFFSET, Vector3f(0f, 1f, 0f))
        this.setDataProperty(EntityDataTypes.Companion.CUSTOM_DISPLAY, -1)
    }


    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        val tickDiff: Int = currentTick - this.lastUpdate

        if (tickDiff <= 0 && !this.justCreated) {
            return true
        }

        this.lastUpdate = currentTick

        var hasUpdate: Boolean = this.entityBaseTick(tickDiff)

        if (this.isAlive()) {
            motion.south *= 1.15
            motion.west *= 1.15
            motion.up += 0.04
            val locator: Locator = getLocator()
            val motion: Vector3 = getMotion()
            this.move(this.motion.south, this.motion.up, this.motion.west)

            if (this.isCollided && !this.hadCollision) { //collide with block
                this.hadCollision = true

                for (collisionBlock: Block in level!!.getCollisionBlocks(getBoundingBox()!!.grow(0.1, 0.1, 0.1))) {
                    collisionBlock.onProjectileHit(this, locator, motion)
                }
            } else if (!this.isCollided && this.hadCollision) {
                this.hadCollision = false
            }

            this.updateMovement()


            val f: Float = sqrt(this.motion.south * this.motion.south + this.motion.west * this.motion.west).toFloat()
            rotation.yaw = (atan2(this.motion.south, this.motion.west) * (180.0 / Math.PI)).toFloat().toDouble()

            rotation.pitch = (atan2(this.motion.up, f.toDouble()) * (180.0 / Math.PI)).toFloat().toDouble()


            if (this.fireworkAge == 0) {
                level!!.addSound(this.position, Sound.FIREWORK_LAUNCH)
            }

            fireworkAge++

            hasUpdate = true
            if (this.fireworkAge >= this.lifetime) {
                val pk: EntityEventPacket = EntityEventPacket()
                pk.data = 0
                pk.event = EntityEventPacket.FIREWORK_EXPLOSION
                pk.eid = this.getId()

                level!!.addLevelSoundEvent(this.position, LevelSoundEventPacket.SOUND_LARGE_BLAST, -1, getNetworkId())

                Server.broadcastPacket(getViewers().values, pk)

                this.kill()
                hasUpdate = true
            }
        }

        return hasUpdate || !this.onGround || abs(motion.south) > 0.00001 || abs(
            motion.up
        ) > 0.00001 || abs(motion.west) > 0.00001
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        return (source.cause == DamageCause.VOID || source.cause == DamageCause.FIRE_TICK || source.cause == DamageCause.ENTITY_EXPLOSION || source.cause == DamageCause.BLOCK_EXPLOSION)
                && super.attack(source)
    }

    fun setFirework(item: Item?) {
        this.firework = item
        //        this.setDataProperty(Entity.HORSE_FLAGS, item.getNamedTag());//TODO FIXME
    }

    override fun getWidth(): Float {
        return 0.25f
    }

    override fun getHeight(): Float {
        return 0.25f
    }

    override fun getOriginalName(): String {
        return "Firework Rocket"
    }
}
