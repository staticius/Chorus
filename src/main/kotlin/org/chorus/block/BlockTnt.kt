package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.Entity
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.entity.projectile.EntitySmallFireball
import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Level
import cn.nukkit.level.Locator
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.RedstoneComponent
import cn.nukkit.utils.random.NukkitRandom
import kotlin.math.cos
import kotlin.math.sin

class BlockTnt @JvmOverloads constructor(state: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(state), RedstoneComponent, Natural {
    override val name: String
        get() = "TNT"

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override fun canBeActivated(): Boolean {
        return true
    }

    override val burnChance: Int
        get() = 15

    override val burnAbility: Int
        get() = 100

    @JvmOverloads
    fun prime(fuse: Int = 80, source: Entity? = null) {
        level.setBlock(this.position, get(BlockID.AIR), true)
        val mot = (NukkitRandom()).nextFloat() * Math.PI * 2
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag?>()
                    .add(FloatTag(position.x + 0.5))
                    .add(FloatTag(position.y))
                    .add(FloatTag(position.z + 0.5))
            )
            .putList(
                "Motion", ListTag<FloatTag?>()
                    .add(FloatTag(-sin(mot) * 0.02))
                    .add(FloatTag(0.2))
                    .add(FloatTag(-cos(mot) * 0.02))
            )
            .putList(
                "Rotation", ListTag<FloatTag?>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putShort("Fuse", fuse)
        val tnt: Entity = createEntity(
            Entity.TNT,
            level.getChunk(position.floorX shr 4, position.floorZ shr 4),
            nbt, source
        )
        if (tnt == null) {
            return
        }
        tnt.spawnToAll()
        level.vibrationManager.callVibrationEvent(
            VibrationEvent(
                source ?: this,
                position.add(0.5, 0.5, 0.5)!!, VibrationType.PRIME_FUSE
            )
        )
    }

    override fun onUpdate(type: Int): Int {
        if (!level.server.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if ((type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) && this.isGettingPower) {
            this.prime()
        }

        return 0
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        when (item.id) {
            Item.FLINT_AND_STEEL -> {
                item.useOn(this)
                this.prime(80, player)
                return true
            }

            Item.FIRE_CHARGE -> {
                if (player != null && !player.isCreative) {
                    item.count--
                }
                this.prime(80, player)
                return true
            }
        }
        if (item.hasEnchantment(Enchantment.ID_FIRE_ASPECT) && item.applyEnchantments()) {
            item.useOn(this)
            this.prime(80, player)
            return true
        }
        return false
    }

    override fun onProjectileHit(projectile: Entity, locator: Locator, motion: Vector3): Boolean {
        //TODO: Wither skull, ghast fireball
        if (projectile is EntitySmallFireball ||
            (projectile.isOnFire() && projectile is EntityArrow)
        ) {
            prime(80, projectile)
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TNT, CommonBlockProperties.EXPLODE_BIT)
            get() = Companion.field
    }
}
