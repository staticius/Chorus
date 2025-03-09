package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.entity.projectile.abstract_arrow.EntityThrownTrident
import cn.nukkit.event.entity.EntityShootBowEvent
import cn.nukkit.event.entity.ProjectileLaunchEvent
import cn.nukkit.item.enchantment.*
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * @author PetteriM1
 */
class ItemTrident @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.TRIDENT, meta, count, "Trident") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_TRIDENT

    override val attackDamage: Int
        get() = 9

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return true
    }

    override fun onRelease(player: Player, ticksUsed: Int): Boolean {
        if (this.hasEnchantment(Enchantment.Companion.ID_TRIDENT_RIPTIDE)) {
            return true
        }

        this.useOn(player)

        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(player.position.x))
                    .add(FloatTag(player.position.y + player.getEyeHeight()))
                    .add(FloatTag(player.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(-sin(player.rotation.yaw / 180 * Math.PI) * cos(player.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(-sin(player.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(cos(player.rotation.yaw / 180 * Math.PI) * cos(player.rotation.pitch / 180 * Math.PI)))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (player.rotation.yaw > 180) 360 else 0) - player.rotation.yaw.toFloat()))
                    .add(FloatTag(-player.rotation.pitch.toFloat()))
            )

        val trident = EntityThrownTrident(player.chunk, nbt, player)
        trident.setItem(this)

        val p = ticksUsed.toDouble() / 20
        val f = min((p * p + p * 2) / 3, 1.0) * 2.5

        if (player.isCreative) {
            trident.setPickupMode(EntityProjectile.PICKUP_CREATIVE)
        }

        trident.setFavoredSlot(player.getInventory().heldItemIndex)

        val entityShootBowEvent = EntityShootBowEvent(player, this, trident, f)

        if (f < 0.1 || ticksUsed < 5) {
            entityShootBowEvent.setCancelled()
        }

        Server.getInstance().pluginManager.callEvent(entityShootBowEvent)
        if (entityShootBowEvent.isCancelled) {
            entityShootBowEvent.getProjectile().close()
        } else {
            entityShootBowEvent.getProjectile()
                .setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.force))
            val ev = ProjectileLaunchEvent(entityShootBowEvent.getProjectile(), player)
            Server.getInstance().pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                entityShootBowEvent.getProjectile().close()
            } else {
                entityShootBowEvent.getProjectile().spawnToAll()
                player.level!!.addSound(player.position, Sound.ITEM_TRIDENT_THROW)
                if (!player.isCreative) {
                    count--
                    player.getInventory().setItemInHand(this)
                }
            }
        }

        return true
    }
}
