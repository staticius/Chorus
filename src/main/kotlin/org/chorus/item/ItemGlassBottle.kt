package org.chorus.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.item.EntityAreaEffectCloud
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace
import java.util.*


class ItemGlassBottle @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.GLASS_BOTTLE, meta, count, "Glass Bottle") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player?,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        var filled: Item? = null
        if (player != null && Arrays.stream<Entity>(
                level.getCollidingEntities(
                    player.getBoundingBox()!!.grow(1.1, 1.1, 1.1)
                )
            ).anyMatch { entity: Entity -> entity is EntityAreaEffectCloud && entity.isDragonBreath() }
        ) {
            filled = ItemDragonBreath()
            Arrays.stream(
                level.getCollidingEntities(
                    player.getBoundingBox()!!.grow(1.1, 1.1, 1.1)
                )
            ).filter { entity: Entity -> entity is EntityAreaEffectCloud && entity.isDragonBreath() }.findAny()
                .ifPresent { entity: Entity ->
                    (entity as EntityAreaEffectCloud).setRadius(
                        entity.getRadius() - 1,
                        true
                    )
                }
        } else if (target.id == BlockID.FLOWING_WATER || target.id == BlockID.WATER) {
            filled = ItemPotion()
        } else if (target is BlockBeehive && target.isFull) {
            filled = get(ItemID.Companion.HONEY_BOTTLE)
            target.honeyCollected(player)
            level.addSound(player!!.position, Sound.BUCKET_FILL_WATER)
        }

        if (filled != null) {
            if (this.count == 1) {
                player!!.getInventory().setItemInHand(filled)
            } else if (this.count > 1) {
                count--
                player!!.getInventory().setItemInHand(this)
                if (player.getInventory().canAddItem(filled)) {
                    player.getInventory().addItem(filled)
                } else {
                    player.level!!.dropItem(
                        player.position.add(0.0, 1.3, 0.0),
                        filled,
                        player.getDirectionVector().multiply(0.4)
                    )
                }
            }

            level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    player,
                    target.position.add(0.5, 0.5, 0.5),
                    VibrationType.FLUID_PICKUP
                )
            )
        }

        return false
    }
}
