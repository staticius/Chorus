package org.chorus.item

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.item.ItemWearEvent
import org.chorus.level.Sound
import org.chorus.math.*
import org.chorus.nbt.tag.ByteTag
import org.chorus.utils.*


abstract class ItemArmor : Item, ItemDurable {
    constructor(id: String) : super(id)

    constructor(id: String, meta: Int) : super(id, meta)

    constructor(id: String, meta: Int, count: Int) : super(id, meta, count)

    constructor(id: String, meta: Int, count: Int, name: String?) : super(id, meta, count, name)

    override val maxStackSize: Int
        get() = 1

    override val isArmor: Boolean
        get() = true

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        var equip = false
        var oldSlotItem: Item = Item.Companion.AIR
        if (this.isHelmet) {
            oldSlotItem = player.getInventory().helmet
            if (player.getInventory().setHelmet(this)) {
                equip = true
            }
        } else if (this.isChestplate) {
            oldSlotItem = player.getInventory().chestplate
            if (player.getInventory().setChestplate(this)) {
                equip = true
            }
        } else if (this.isLeggings) {
            oldSlotItem = player.getInventory().leggings
            if (player.getInventory().setLeggings(this)) {
                equip = true
            }
        } else if (this.isBoots) {
            oldSlotItem = player.getInventory().boots
            if (player.getInventory().setBoots(this)) {
                equip = true
            }
        }
        if (equip) {
            player.getInventory().setItem(player.getInventory().heldItemIndex, oldSlotItem)
            val tier = this.tier
            when (tier) {
                TIER_CHAIN -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_CHAIN)
                TIER_DIAMOND -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_DIAMOND)
                TIER_GOLD -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_GOLD)
                TIER_IRON -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_IRON)
                TIER_LEATHER -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_LEATHER)
                TIER_NETHERITE -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_NETHERITE)
                else -> player.level!!.addSound(player.position, Sound.ARMOR_EQUIP_GENERIC)
            }
        }

        return this.getCount() == 0
    }

    override var damage: Int
        get() = super.damage
        set(damage) {
            val event = ItemWearEvent(this, damage)
            val pluginManager = Server.instance.pluginManager
            pluginManager?.callEvent(event) //Method gets called on server start before plugin manager is initiated

            if (!event.isCancelled) {
                super.setDamage(event.newDurability)
                getOrCreateNamedTag().putInt("Damage", event.newDurability)
            }
        }

    override val enchantAbility: Int
        get() = when (this.tier) {
            TIER_CHAIN -> 12
            TIER_LEATHER, TIER_NETHERITE -> 15
            TIER_DIAMOND -> 10
            TIER_GOLD -> 25
            TIER_IRON -> 9
            else -> 0
        }

    override val isUnbreakable: Boolean
        get() {
            val tag = this.getNamedTagEntry("Unbreakable")
            return tag is ByteTag && tag.data > 0
        }

    companion object {
        const val TIER_LEATHER: Int = 1
        const val TIER_IRON: Int = 2
        const val TIER_CHAIN: Int = 3
        const val TIER_GOLD: Int = 4
        const val TIER_DIAMOND: Int = 5
        const val TIER_NETHERITE: Int = 6
        val TIER_OTHER: Int = Utils.dynamic(1000)
    }
}
