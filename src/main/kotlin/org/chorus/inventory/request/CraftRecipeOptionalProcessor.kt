package org.chorus.inventory.request

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.BlockID
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.inventory.AnvilInventory
import cn.nukkit.inventory.CartographyTableInventory
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.types.itemstack.request.action.CraftRecipeOptionalAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import io.netty.util.internal.StringUtil
import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.objects.ObjectIntMutablePair
import lombok.extern.slf4j.Slf4j
import java.util.*
import java.util.stream.Stream
import kotlin.math.max
import kotlin.math.min

/**
 * @author CoolLoong
 */
@Slf4j
class CraftRecipeOptionalProcessor : ItemStackRequestActionProcessor<CraftRecipeOptionalAction> {
    override fun handle(
        action: CraftRecipeOptionalAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            CraftRecipeOptionalProcessor.log.error("the player's inventory is empty!")
            return context.error()
        }
        val itemStackRequest = context.itemStackRequest
        val inventory = topWindow.get()

        var filterString: String? = null
        if (itemStackRequest.filterStrings.size != 0 && !itemStackRequest.filterStrings[0].isBlank()) {
            val filteredStringIndex = action.filteredStringIndex
            val filterStrings = itemStackRequest.filterStrings
            filterString = filterStrings[filteredStringIndex]
            if (filterString.isBlank() || filterString.length > 64) {
                CraftRecipeOptionalProcessor.log.debug(player.getName() + ": FilterTextPacket with too long text")
                return context.error()
            }
        }

        if (inventory is AnvilInventory) {
            val pair = updateAnvilResult(player, inventory, filterString)
            if (pair != null) {
                player.creativeOutputInventory.setItem(pair.left())
                player.setExperience(player.experience, player.experienceLevel - pair.right())
            } else {
                return context.error()
            }
        } else if (inventory is CartographyTableInventory) {
            val item = updateCartographyTableResult(player, inventory, filterString)
            if (item != null) {
                player.creativeOutputInventory.setItem(item)
            } else {
                return context.error()
            }
        } else {
            //todo more
        }
        return null
    }

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL

    fun updateAnvilResult(player: Player, inventory: AnvilInventory, filterString: String?): Pair<Item, Int>? {
        val target = inventory.inputSlot
        val sacrifice = inventory.materialSlot
        if (target.isNull && sacrifice.isNull) {
            return null
        }

        val resultPair: Pair<Item, Int> = ObjectIntMutablePair.of(Item.AIR, 1)

        var extraCost = 0
        var costHelper = 0
        val repairMaterial = getRepairMaterial(target)
        var result = target.clone()

        val enchantments: MutableSet<Enchantment> = LinkedHashSet(Arrays.asList(*target.enchantments))
        if (!sacrifice.isNull) {
            val enchantedBook = sacrifice.id == Item.ENCHANTED_BOOK && sacrifice.enchantments.size > 0
            var repair: Int
            var repair2: Int
            var repair3: Int
            if (result.maxDurability != -1 && sacrifice.id == repairMaterial) { //Anvil - repair
                repair = min(result.damage.toDouble(), (result.maxDurability / 4).toDouble()).toInt()
                if (repair <= 0) {
                    return null
                }

                repair2 = 0
                while (repair > 0 && repair2 < sacrifice.getCount()) {
                    repair3 = result.damage - repair
                    result.damage = repair3
                    ++extraCost
                    repair = min(result.damage.toDouble(), (result.maxDurability / 4).toDouble()).toInt()
                    ++repair2
                }
            } else {
                if (!enchantedBook && (result.id != sacrifice.id || result.maxDurability == -1)) { //Anvil - ench
                    player.level!!.addSound(player.position, Sound.RANDOM_ANVIL_USE, 1f, 1f)
                    return null
                }

                if ((result.maxDurability != -1) && !enchantedBook) {
                    repair = target.maxDurability - target.damage
                    repair2 = sacrifice.maxDurability - sacrifice.damage
                    repair3 = repair2 + result.maxDurability * 12 / 100
                    val totalRepair = repair + repair3
                    var finalDamage = result.maxDurability - totalRepair + 1
                    if (finalDamage < 0) {
                        finalDamage = 0
                    }

                    if (finalDamage < result.damage) {
                        result.damage = finalDamage
                        extraCost += 2
                    }
                }

                val sacrificeEnchantments = sacrifice.enchantments
                var compatibleFlag = false
                var incompatibleFlag = false
                val enchantmentIterator = Arrays.stream(sacrificeEnchantments).iterator()

                iter@ while (true) {
                    var sacrificeEnchantment: Enchantment?
                    do {
                        if (!enchantmentIterator.hasNext()) {
                            if (incompatibleFlag && !compatibleFlag) {
                                return null
                            }
                            break@iter
                        }

                        sacrificeEnchantment = enchantmentIterator.next()
                    } while (sacrificeEnchantment == null)

                    val resultEnchantment = result.getEnchantment(sacrificeEnchantment.id)
                    val targetLevel = resultEnchantment?.level ?: 0
                    var resultLevel = sacrificeEnchantment.level
                    resultLevel = if (targetLevel == resultLevel) resultLevel + 1 else max(
                        resultLevel.toDouble(),
                        targetLevel.toDouble()
                    ).toInt()
                    var compatible = sacrificeEnchantment.canEnchant(target)
                    if (player.isCreative || target.id == Item.ENCHANTED_BOOK) {
                        compatible = true
                    }

                    val targetEnchIter = Stream.of(*target.enchantments).iterator()

                    while (targetEnchIter.hasNext()) {
                        val targetEnchantment = targetEnchIter.next()
                        if (!Enchantment.equal(targetEnchantment, sacrificeEnchantment) &&
                            (!sacrificeEnchantment.isCompatibleWith(targetEnchantment) ||
                                    !targetEnchantment.isCompatibleWith(sacrificeEnchantment))
                        ) {
                            compatible = false
                            ++extraCost
                        }
                    }

                    if (!compatible) {
                        incompatibleFlag = true
                    } else {
                        compatibleFlag = true
                        if (resultLevel > sacrificeEnchantment.maxLevel) {
                            resultLevel = sacrificeEnchantment.maxLevel
                        }
                        val usedEnch = if (sacrificeEnchantment.identifier == null) {
                            Enchantment.getEnchantment(sacrificeEnchantment.id)
                        } else {
                            Enchantment.getEnchantment(sacrificeEnchantment.identifier!!)
                        }
                        enchantments.add(usedEnch.setLevel(resultLevel))
                        var rarity: Int
                        val weight = sacrificeEnchantment.rarity.weight
                        rarity = if (weight >= 10) {
                            1
                        } else if (weight >= 5) {
                            2
                        } else if (weight >= 2) {
                            4
                        } else {
                            8
                        }

                        if (enchantedBook) {
                            rarity = max(1.0, (rarity / 2).toDouble()).toInt()
                        }

                        (extraCost += rarity * max(0.0, (resultLevel - targetLevel).toDouble())).toInt()
                        if (target.getCount() > 1) {
                            extraCost = 40
                        }
                    }
                }
            }
        }

        //Anvil - rename
        if (StringUtil.isNullOrEmpty(filterString)) {
            player.level!!.addSound(player.position, Sound.RANDOM_ANVIL_USE, 1f, 1f)
            if (target.hasCustomName()) {
                costHelper = 1
                extraCost += costHelper
                result.clearCustomName()
            }
        } else {
            costHelper = 1
            extraCost += costHelper
            result.setCustomName(filterString)
        }

        val levelCost = getRepairCost(result) + (if (sacrifice.isNull) 0 else getRepairCost(sacrifice))
        resultPair.right(levelCost + extraCost)
        if (extraCost <= 0) {
            result = Item.AIR
        }

        if (costHelper == extraCost && costHelper > 0 && resultPair.right() >= 40) {
            resultPair.right(39)
        }

        if (resultPair.right() >= 40 && !player.isCreative) {
            result = Item.AIR
        }

        if (!result.isNull) {
            var repairCost = getRepairCost(result)
            if (!sacrifice.isNull && repairCost < getRepairCost(sacrifice)) {
                repairCost = getRepairCost(sacrifice)
            }

            if (costHelper != extraCost || costHelper == 0) {
                repairCost = repairCost * 2 + 1
            }

            var namedTag = result.namedTag
            if (namedTag == null) {
                namedTag = CompoundTag()
            }
            namedTag.putInt("RepairCost", repairCost)
            namedTag.remove("ench")
            result.setNamedTag(namedTag)
            if (!enchantments.isEmpty()) {
                result.addEnchantment(*enchantments.toArray(Enchantment.EMPTY_ARRAY))
            }
        }
        resultPair.left(result)
        return resultPair
    }

    fun updateCartographyTableResult(
        player: Player,
        inventory: CartographyTableInventory,
        filterString: String?
    ): Item? {
        val server: Server = player.getServer()
        val input = inventory.input
        val additional = inventory.additional
        var result = Item.AIR

        if (input.isNull && additional.isNull) {
            return null
        }

        if (input.id == Item.PAPER && additional.isNull) {
            result = Item.get(Item.EMPTY_MAP)
        }

        if (input.id == Item.EMPTY_MAP || input.id == Item.FILLED_MAP && additional.isNull) {
            result = input.clone()
        }

        if ((input.id == Item.EMPTY_MAP || input.id == Item.FILLED_MAP || input.id == Item.PAPER) && additional.id == Item.COMPASS) {
            val item = if (input.id == Item.PAPER) Item.get(Item.EMPTY_MAP) else input.clone()
            item.damage = 2
            result = item
        }

        if (input.id == Item.FILLED_MAP && additional.id == BlockID.GLASS_PANE) {
            val item = input.clone()
            item.damage = 6
            result = item
        }

        if (input.id == Item.FILLED_MAP && additional.id == Item.EMPTY_MAP) {
            val item = input.clone()
            item.setCount(2)
            result = item
        }

        if (input.id == Item.FILLED_MAP && additional.id == Item.PAPER) {
            val item = input.clone() as ItemFilledMap
            val level = server.getLevel(item.mapWorld)
            val startX = item.mapStartX
            val startZ = item.mapStartZ
            val scale = item.mapScale + 1

            item.renderMap(level, startX, startZ, scale)
            item.sendImage(player, item.mapScale)

            result = item
        }

        if (!StringUtil.isNullOrEmpty(filterString)) {
            result.setCustomName(filterString)
        } else {
            result.clearCustomName()
        }

        return result
    }

    companion object {
        private fun getRepairCost(item: Item): Int {
            return if (item.hasCompoundTag() && item.namedTag!!.contains("RepairCost")) item.namedTag!!.getInt("RepairCost") else 0
        }

        private fun getRepairMaterial(target: Item): String {
            return when (target.id) {
                ItemID.WOODEN_SWORD, ItemID.WOODEN_PICKAXE, ItemID.WOODEN_SHOVEL, ItemID.WOODEN_AXE, ItemID.WOODEN_HOE -> ItemID.PLANKS
                ItemID.IRON_SWORD, ItemID.IRON_PICKAXE, ItemID.IRON_SHOVEL, ItemID.IRON_AXE, ItemID.IRON_HOE, ItemID.IRON_HELMET, ItemID.IRON_CHESTPLATE, ItemID.IRON_LEGGINGS, ItemID.IRON_BOOTS, ItemID.CHAINMAIL_HELMET, ItemID.CHAINMAIL_CHESTPLATE, ItemID.CHAINMAIL_LEGGINGS, ItemID.CHAINMAIL_BOOTS -> ItemID.IRON_INGOT
                ItemID.GOLDEN_SWORD, ItemID.GOLDEN_PICKAXE, ItemID.GOLDEN_SHOVEL, ItemID.GOLDEN_AXE, ItemID.GOLDEN_HOE, ItemID.GOLDEN_HELMET, ItemID.GOLDEN_CHESTPLATE, ItemID.GOLDEN_LEGGINGS, ItemID.GOLDEN_BOOTS -> ItemID.GOLD_INGOT
                ItemID.DIAMOND_SWORD, ItemID.DIAMOND_PICKAXE, ItemID.DIAMOND_SHOVEL, ItemID.DIAMOND_AXE, ItemID.DIAMOND_HOE, ItemID.DIAMOND_HELMET, ItemID.DIAMOND_CHESTPLATE, ItemID.DIAMOND_LEGGINGS, ItemID.DIAMOND_BOOTS -> ItemID.DIAMOND
                ItemID.LEATHER_HELMET, ItemID.LEATHER_CHESTPLATE, ItemID.LEATHER_LEGGINGS, ItemID.LEATHER_BOOTS -> ItemID.LEATHER
                ItemID.STONE_SWORD, ItemID.STONE_PICKAXE, ItemID.STONE_SHOVEL, ItemID.STONE_AXE, ItemID.STONE_HOE -> BlockID.COBBLESTONE
                ItemID.NETHERITE_SWORD, ItemID.NETHERITE_PICKAXE, ItemID.NETHERITE_SHOVEL, ItemID.NETHERITE_AXE, ItemID.NETHERITE_HOE, ItemID.NETHERITE_HELMET, ItemID.NETHERITE_CHESTPLATE, ItemID.NETHERITE_LEGGINGS, ItemID.NETHERITE_BOOTS -> ItemID.NETHERITE_INGOT
                ItemID.ELYTRA -> ItemID.PHANTOM_MEMBRANE
                else -> BlockID.AIR
            }
        }
    }
}
