package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.effect.PotionType
import cn.nukkit.event.player.PlayerItemConsumeEvent
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*

class ItemPotion @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.POTION, meta, count, "Potion") {
    init {
        updateName()
    }

    override var damage: Int
        get() = super.damage
        set(meta) {
            super.setDamage(meta)
            updateName()
        }

    private fun updateName() {
        val potion = this.potion
        name = if (PotionType.WATER.equals(potion)) {
            buildName(potion, "Bottle", true)
        } else {
            buildName(potion!!, "Potion", true)
        }
    }

    override val maxStackSize: Int
        get() = 1

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return true
    }

    override fun onUse(player: Player, ticksUsed: Int): Boolean {
        if (ticksUsed < 31) {
            return false
        }
        val consumeEvent = PlayerItemConsumeEvent(player, this)
        player.getServer().getPluginManager().callEvent(consumeEvent)
        if (consumeEvent.isCancelled) {
            return false
        }
        val potion = PotionType.get(this.getDamage())

        player.level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                player,
                player.position.clone(),
                VibrationType.DRINKING
            )
        )

        if (player.isAdventure || player.isSurvival) {
            --this.count
            player.getInventory().setItemInHand(this)
            player.getInventory().addItem(ItemGlassBottle())
        }

        if (potion != null) {
            potion.applyEffects(player, false, 1.0)
        }
        return true
    }

    val potion: PotionType?
        get() = PotionType.get(getDamage())

    companion object {
        fun buildName(potion: PotionType, type: String, includeLevel: Boolean): String {
            return when (potion.stringId) {
                "minecraft:water" -> "Water $type"
                "minecraft:mundane", "minecraft:long_mundane" -> "Mundane $type"
                "minecraft:thick" -> "Thick $type"
                "minecraft:awkward" -> "Awkward $type"
                "minecraft:turtle_master", "minecraft:long_turtle_master", "minecraft:strong_turtle_master" -> {
                    val name = "$type of the Turtle Master"
                    if (!includeLevel) {
                        name
                    }

                    if (potion.level <= 1) {
                        name
                    }

                    name + " " + potion.getRomanLevel()
                }

                else -> {
                    var finalName = potion.name
                    finalName = if (finalName!!.isEmpty()) {
                        type
                    } else {
                        "$type of $finalName"
                    }
                    if (includeLevel && potion.level > 1) {
                        finalName += " " + potion.getRomanLevel()
                    }
                    finalName
                }
            }
        }

        @JvmStatic
        fun fromPotion(potion: PotionType): ItemPotion {
            return ItemPotion(potion.id)
        }
    }
}
