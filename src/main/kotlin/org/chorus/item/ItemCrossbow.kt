package org.chorus.item

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.item.EntityCrossbowFirework
import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.event.entity.EntityShootCrossbowEvent
import cn.nukkit.event.entity.ProjectileLaunchEvent
import cn.nukkit.inventory.Inventory
import cn.nukkit.item.enchantment.*
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.*
import cn.nukkit.utils.*
import kotlin.math.cos
import kotlin.math.sin

class ItemCrossbow @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.CROSSBOW, meta, count, "Crossbow") {
    private var loadTick = 0

    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_CROSSBOW

    override fun onUse(player: Player, ticksUsed: Int): Boolean {
        if (isLoaded) return true
        this.loadTick = player.level!!.tick
        var needTickUsed = 25
        val enchantment = this.getEnchantment(Enchantment.Companion.ID_CROSSBOW_QUICK_CHARGE)
        if (enchantment != null) {
            needTickUsed -= enchantment.level * 5 //0.25s
        }

        if (ticksUsed >= needTickUsed) {
            val itemArrow: Item
            val inventoryOptional = player.getInventory().contents.entries.stream()
                .filter { item: Map.Entry<Int?, Item?> -> item.value is ItemArrow || item.value is ItemFireworkRocket }
                .findFirst()
            val offhandOptional = player.getOffhandInventory()!!.contents.entries.stream()
                .filter { item: Map.Entry<Int?, Item?> -> item.value is ItemArrow || item.value is ItemFireworkRocket }
                .findFirst()
            var item: Item? = null
            var inventory: Inventory? = null
            if (offhandOptional.isPresent) {
                inventory = player.getOffhandInventory()
                item = offhandOptional.get().value
            } else if (inventoryOptional.isPresent) {
                inventory = player.getInventory()
                item = inventoryOptional.get().value
            }
            if (inventory == null) return false
            if (!this.canLoad(item!!)) {
                if (player.isCreative) {
                    this.loadArrow(player, item)
                }

                return true
            }

            if (!this.isLoaded) {
                itemArrow = item.clone()
                itemArrow.setCount(1)

                if (!player.isCreative) {
                    if (!this.isUnbreakable) {
                        val durability = this.getEnchantment(Enchantment.Companion.ID_DURABILITY)
                        if (durability == null || durability.level <= 0 || 100 / (durability.level + 1) > Utils.random.nextInt(
                                100
                            )
                        ) {
                            this.damage = this.damage + 2
                            if (this.damage >= 385) {
                                --this.count
                            }
                            player.getInventory().setItemInHand(this)
                        }
                    }

                    inventory.removeItem(itemArrow)
                }

                this.loadArrow(player, itemArrow)
            }
        }
        return true
    }

    protected fun canLoad(item: Item): Boolean {
        return when (item.getId()) {
            ItemID.Companion.ARROW, ItemID.Companion.FIREWORK_ROCKET -> true
            else -> false
        }
    }

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        if (this.isLoaded && player.level!!.tick - this.loadTick > 5) {
            val mX: Double
            val mY: Double
            val mZ: Double
            val nbt = (CompoundTag())
                .putList(
                    "Pos", ListTag<Tag>()
                        .add(FloatTag(player.position.x))
                        .add(FloatTag(player.position.y + player.getEyeHeight().toDouble()))
                        .add(FloatTag(player.position.z))
                )
                .putList(
                    "Motion", ListTag<Tag>()
                        .add(FloatTag((-sin(player.rotation.yaw / 180.0 * 3.141592653589793) * cos(player.rotation.pitch / 180.0 * 3.141592653589793)).also {
                            mX = it
                        }))
                        .add(FloatTag((-sin(player.rotation.pitch / 180.0 * 3.141592653589793)).also { mY = it }))
                        .add(FloatTag((cos(player.rotation.yaw / 180.0 * 3.141592653589793) * cos(player.rotation.pitch / 180.0 * 3.141592653589793)).also {
                            mZ = it
                        }))
                )
                .putList(
                    "Rotation", ListTag<Tag>()
                        .add(FloatTag((if (player.rotation.yaw > 180.0) 360 else 0).toFloat() - player.rotation.yaw.toFloat()))
                        .add(FloatTag((-player.rotation.pitch).toFloat()))
                )
            val item = get(this.namedTag.getCompound("chargedItem").getString("Name"))
            if (item.getId() == ItemID.Companion.FIREWORK_ROCKET) {
                val entity = EntityCrossbowFirework(player.chunk, nbt)
                entity.setMotion(Vector3(mX, mY, mZ))
                entity.spawnToAll()
                player.level!!.addSound(player.position, Sound.CROSSBOW_SHOOT)
                removeChargedItem(player)
            } else {
                val entity = EntityArrow(player.chunk, nbt, player, true)
                val chargedItem = this.namedTag.getCompound("chargedItem")
                entity.setItem(
                    get(
                        chargedItem.getString("Name"),
                        chargedItem.getShort("Damage").toInt(),
                        chargedItem.getByte("Count").toInt()
                    ) as ItemArrow
                )
                val entityShootBowEvent = EntityShootCrossbowEvent(player, this, entity)
                Server.getInstance().pluginManager.callEvent(entityShootBowEvent)
                if (entityShootBowEvent.isCancelled) {
                    entityShootBowEvent.getProjectile(0).close()
                    player.getInventory().sendContents(player)
                } else {
                    entityShootBowEvent.getProjectile(0)
                        .setMotion(entityShootBowEvent.getProjectile(0).getMotion().multiply(3.5))
                    if (entityShootBowEvent.getProjectile(0) != null) {
                        val proj = entityShootBowEvent.getProjectile(0)
                        val projectile = ProjectileLaunchEvent(proj, player)
                        Server.getInstance().pluginManager.callEvent(projectile)
                        if (projectile.isCancelled) {
                            proj.close()
                        } else {
                            proj.spawnToAll()
                            player.level!!.addSound(player.position, Sound.CROSSBOW_SHOOT)
                            removeChargedItem(player)
                        }
                    }
                }
            }
        }
        return true
    }

    fun removeChargedItem(player: Player) {
        this.setCompoundTag(this.namedTag.remove("chargedItem"))
        player.getInventory().setItemInHand(this)
    }

    override fun onRelease(player: Player, ticksUsed: Int): Boolean {
        return true
    }


    fun loadArrow(player: Player, arrow: Item?) {
        if (arrow != null) {
            val tag = if (this.namedTag == null) CompoundTag() else this.namedTag
            tag!!.putCompound(
                "chargedItem", CompoundTag()
                    .putByte("Count", arrow.getCount())
                    .putShort("Damage", arrow.damage)
                    .putString("Name", arrow.getId())
                    .putByte("WasPickedUp", 0)
            )
            this.setCompoundTag(tag)
            player.getInventory().setItemInHand(this)
            player.level!!.addSound(player.position, Sound.CROSSBOW_LOADING_END)
        }
    }

    val isLoaded: Boolean
        get() {
            val itemInfo = this.getNamedTagEntry("chargedItem")
            if (itemInfo == null) {
                return false
            } else {
                val tag = itemInfo as CompoundTag
                return tag.getByte("Count") > 0 && tag.getString("Name") != null
            }
        }

    override val enchantAbility: Int
        get() = 1
}
