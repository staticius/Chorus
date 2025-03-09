package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.entity.item.EntityElytraFirework
import cn.nukkit.entity.item.EntityFireworksRocket
import cn.nukkit.level.Level
import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.DyeColor
import kotlin.math.cos
import kotlin.math.sin

class ItemFireworkRocket @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.FIREWORK_ROCKET, meta, count, "Firework Rocket") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        if (player.isAdventure) {
            return false
        }

        if (block.canPassThrough()) {
            this.spawnFirework(level, block.position)

            if (!player.isCreative) {
                player.getInventory().decreaseCount(player.getInventory().heldItemIndex)
            }

            return true
        }

        return false
    }

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        if (player.getInventory().chestplate is ItemElytra && player.isGliding()) {
            player.setMotion(
                Vector3(
                    -sin(Math.toRadians(player.rotation.yaw)) * cos(Math.toRadians(player.rotation.pitch)) * 2,
                    -sin(Math.toRadians(player.rotation.pitch)) * 2,
                    cos(Math.toRadians(player.rotation.yaw)) * cos(Math.toRadians(player.rotation.pitch)) * 2
                )
            )

            spawnElytraFirework(player.position, player)
            if (!player.isCreative) {
                count--
            }

            return true
        }

        return false
    }

    fun addExplosion(explosion: FireworkExplosion) {
        val colors = explosion.getColors()
        val fades = explosion.getFades()

        if (colors.isEmpty()) {
            return
        }
        val clrs = ByteArray(colors.size)
        for (i in clrs.indices) {
            clrs[i] = colors[i].dyeData.toByte()
        }

        val fds = ByteArray(fades.size)
        for (i in fds.indices) {
            fds[i] = fades[i].dyeData.toByte()
        }

        val explosions = this.namedTag.getCompound("Fireworks").getList(
            "Explosions",
            CompoundTag::class.java
        )
        val tag = CompoundTag()
            .putByteArray("FireworkColor", clrs)
            .putByteArray("FireworkFade", fds)
            .putBoolean("FireworkFlicker", explosion.flicker)
            .putBoolean("FireworkTrail", explosion.trail)
            .putByte("FireworkType", explosion.type.ordinal)

        explosions.add(tag)
    }

    fun clearExplosions() {
        this.namedTag.getCompound("Fireworks").putList("Explosions", ListTag<CompoundTag>())
    }

    private fun spawnFirework(level: Level, pos: Vector3) {
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(pos.x + 0.5))
                    .add(FloatTag(pos.y + 0.5))
                    .add(FloatTag(pos.z + 0.5))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putCompound("FireworkItem", NBTIO.putItemHelper(this))

        val entity = createEntity(
            Entity.FIREWORKS_ROCKET,
            level.getChunk(pos.floorX shr 4, pos.floorZ shr 4),
            nbt
        ) as EntityFireworksRocket?
        entity?.spawnToAll()
    }

    private fun spawnElytraFirework(pos: Vector3, player: Player) {
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(pos.x + 0.5))
                    .add(FloatTag(pos.y + 0.5))
                    .add(FloatTag(pos.z + 0.5))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putCompound("FireworkItem", NBTIO.putItemHelper(this))

        val entity = EntityElytraFirework(player.getLocator().getChunk(), nbt, player)
        entity.spawnToAll()
    }

    class FireworkExplosion {
        private val colors: MutableList<DyeColor> = ArrayList()
        private val fades: MutableList<DyeColor> = ArrayList()
        var flicker: Boolean = false
        var trail: Boolean = false
        var type: ExplosionType = ExplosionType.CREEPER_SHAPED
            private set

        fun getColors(): List<DyeColor> {
            return this.colors
        }

        fun getFades(): List<DyeColor> {
            return this.fades
        }

        fun hasFlicker(): Boolean {
            return this.flicker
        }

        fun hasTrail(): Boolean {
            return this.trail
        }

        fun setFlicker(flicker: Boolean): FireworkExplosion {
            this.flicker = flicker
            return this
        }

        fun setTrail(trail: Boolean): FireworkExplosion {
            this.trail = trail
            return this
        }

        fun type(type: ExplosionType): FireworkExplosion {
            this.type = type
            return this
        }

        fun addColor(color: DyeColor): FireworkExplosion {
            colors.add(color)
            return this
        }

        fun addFade(fade: DyeColor): FireworkExplosion {
            fades.add(fade)
            return this
        }

        enum class ExplosionType {
            SMALL_BALL,
            LARGE_BALL,
            STAR_SHAPED,
            CREEPER_SHAPED,
            BURST
        }
    }
}