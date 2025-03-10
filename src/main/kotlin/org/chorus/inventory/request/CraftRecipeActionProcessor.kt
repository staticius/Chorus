package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.mob.villagers.EntityVillagerV2
import org.chorus.entity.mob.villagers.EntityVillagerV2.addExperience
import org.chorus.entity.mob.villagers.EntityVillagerV2.addGossip
import org.chorus.entity.mob.villagers.EntityVillagerV2.getReputation
import org.chorus.event.inventory.CraftItemEvent
import org.chorus.event.inventory.EnchantItemEvent
import org.chorus.inventory.EnchantInventory
import org.chorus.inventory.InputInventory
import org.chorus.inventory.SmithingInventory
import org.chorus.item.*
import org.chorus.item.enchantment.Enchantment
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.PlayerEnchantOptionsPacket
import org.chorus.network.protocol.types.TrimData
import org.chorus.network.protocol.types.TrimMaterial
import org.chorus.network.protocol.types.TrimPattern
import org.chorus.network.protocol.types.itemstack.request.action.ConsumeAction
import org.chorus.network.protocol.types.itemstack.request.action.CraftRecipeAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.recipe.SmithingTransformRecipe
import org.chorus.recipe.SmithingTrimRecipe
import org.chorus.registry.Registries
import org.chorus.utils.TradeRecipeBuildUtils

import java.util.*
import kotlin.math.max

/**
 * Allay Project 2023/12/1
 *
 * @author daoge_cmd | Cool_Loong
 */

class CraftRecipeActionProcessor : ItemStackRequestActionProcessor<CraftRecipeAction> {
    fun checkTrade(recipeInput: CompoundTag, input: Item, subtract: Int): Boolean {
        val id = input.id
        val damage = input.damage
        val count = input.getCount()
        val required = max((recipeInput.getByte("Count") - subtract).toDouble(), 1.0).toInt()
        if (required > count || recipeInput.getShort("Damage")
                .toInt() != damage || (recipeInput.getString("Name") != id)
        ) {
            CraftRecipeActionProcessor.log.error(
                "The trade recipe does not match, expect {} actual {}, count {}",
                recipeInput,
                input,
                required
            )
            return true
        }
        if (recipeInput.contains("tag")) {
            val tag = recipeInput.getCompound("tag")
            val compoundTag = input.namedTag
            if (tag != compoundTag) {
                CraftRecipeActionProcessor.log.error(
                    "The trade recipe tag does not match tag, expect {} actual {}",
                    tag,
                    compoundTag
                )
                return true
            }
        }
        return false
    }

    override fun handle(action: CraftRecipeAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val inventory = player.topWindow.orElseGet { player.craftingGrid }
        if (action.recipeNetworkId >= PlayerEnchantOptionsPacket.ENCH_RECIPEID) {  //handle ench recipe
            val enchantOptionData = PlayerEnchantOptionsPacket.RECIPE_MAP[action.recipeNetworkId]
            if (enchantOptionData == null) {
                CraftRecipeActionProcessor.log.error("Can't find enchant recipe from netId " + action.recipeNetworkId)
                return context.error()
            }
            val first = inventory.getItem(0)
            if (first.isNull) {
                CraftRecipeActionProcessor.log.error("Can't find enchant input!")
                return context.error()
            }
            var item = first.clone().autoAssignStackNetworkId()
            if (item.id == Item.BOOK) item = Item.get(Item.ENCHANTED_BOOK)
            val enchantments = enchantOptionData.enchantments
            item.addEnchantment(*enchantments.toArray(Enchantment.EMPTY_ARRAY))
            val event = EnchantItemEvent(
                inventory as EnchantInventory,
                first.clone().autoAssignStackNetworkId(),
                item,
                enchantOptionData.minLevel,
                player
            )
            Server.getInstance().pluginManager.callEvent(event)
            if (!event.isCancelled) {
                if ((player.gamemode and 0x01) == 0) {
                    player.setExperience(player.experience, player.experienceLevel - (enchantOptionData.entry + 1))
                }
                player.creativeOutputInventory.setItem(item)
                PlayerEnchantOptionsPacket.RECIPE_MAP.remove(action.recipeNetworkId)
                player.regenerateEnchantmentSeed()
                context.put(ENCH_RECIPE_KEY, true)
            }
            return null
        } else if (action.recipeNetworkId >= TradeRecipeBuildUtils.TRADE_RECIPE_ID) { //handle village trade recipe
            val tradeRecipe = TradeRecipeBuildUtils.RECIPE_MAP[action.recipeNetworkId]
            if (tradeRecipe == null) {
                CraftRecipeActionProcessor.log.error("Can't find trade recipe from netId {}", action.recipeNetworkId)
                return context.error()
            }
            val first = inventory.getUnclonedItem(0)
            val second = inventory.getUnclonedItem(1)
            val output = NBTIO.getItemHelper(tradeRecipe.getCompound("sell"))
            var reputation = 0
            if (inventory.holder is EntityVillagerV2) {
                reputation = villager.getReputation(player)
            }
            output.setCount(output.getCount() * action.numberOfRequestedCrafts)
            if (first!!.isNull && second!!.isNull) {
                CraftRecipeActionProcessor.log.error("Can't find trade input!")
                return context.error()
            }
            val ca = tradeRecipe.contains("buyA")
            val cb = tradeRecipe.contains("buyB")

            val reductionA =
                (reputation * (if (tradeRecipe.containsFloat("priceMultiplierA")) tradeRecipe.getFloat("priceMultiplierA") else 0f)).toInt()
            val reductionB =
                (reputation * (if (tradeRecipe.containsFloat("priceMultiplierB")) tradeRecipe.getFloat("priceMultiplierB") else 0f)).toInt()

            if (ca && cb) {
                if ((first.isNull || second!!.isNull)) {
                    CraftRecipeActionProcessor.log.error("Can't find trade input!")
                    return context.error()
                } else {
                    if (checkTrade(tradeRecipe.getCompound("buyA"), first, reductionA)) return context.error()
                    if (checkTrade(tradeRecipe.getCompound("buyB"), second, reductionB)) return context.error()
                    player.creativeOutputInventory.setItem(output)
                }
            } else if (ca) {
                if (first.isNull) {
                    CraftRecipeActionProcessor.log.error("Can't find trade input!")
                    return context.error()
                } else {
                    if (checkTrade(tradeRecipe.getCompound("buyA"), first, reductionA)) return context.error()
                    if (tradeRecipe.getInt("uses") + action.numberOfRequestedCrafts > tradeRecipe.getInt("maxUses")) return context.error()
                    inventory.sendContents(player)
                    player.creativeOutputInventory.setItem(output)
                }
            }
            if (ca) {
                val traderExp = if (tradeRecipe.contains("traderExp")) tradeRecipe.getInt("traderExp") else 0
                val rewardExp = if (tradeRecipe.contains("rewardExp")) tradeRecipe.getInt("rewardExp") else 0
                player.addExperience(rewardExp * action.numberOfRequestedCrafts)
                tradeRecipe.putInt("uses", tradeRecipe.getInt("uses") + action.numberOfRequestedCrafts)
                if (inventory.holder is EntityVillagerV2) {
                    villager.addExperience(traderExp * action.numberOfRequestedCrafts)
                    villager.addGossip(player.loginChainData.xuid, EntityVillagerV2.Gossip.TRADING, 2)
                }
            }
            return null
        }
        val craft: InputInventory = if (player.topWindow.isPresent && inventory is InputInventory) {
            inventory
        } else {
            player.craftingGrid
        }
        val numberOfRequestedCrafts = action.numberOfRequestedCrafts
        val recipe = Registries.RECIPE.getRecipeByNetworkId(action.recipeNetworkId)
        val input = craft.input
        val data = input!!.data
        val items = ArrayList<Item>()
        for (d in data) {
            Collections.addAll(items, *d)
        }
        val craftItemEvent = CraftItemEvent(player, items.toArray(Item.EMPTY_ARRAY), recipe, numberOfRequestedCrafts)
        player.getServer().getPluginManager().callEvent(craftItemEvent)
        if (craftItemEvent.isCancelled) {
            return context.error()
        }
        val matched = recipe.match(input)
        if (!matched) {
            if (recipe is SmithingTrimRecipe) {
                return handleSmithingTrim(player, context)
            } else if (recipe is SmithingTransformRecipe) {
                return handleSmithingUpgrade(recipe, player, context)
            }
            CraftRecipeActionProcessor.log.warn(
                "Mismatched recipe! Network id: {},Recipe name: {},Recipe type: {}",
                action.recipeNetworkId,
                recipe.recipeId,
                recipe.type
            )
            return context.error()
        } else {
            // Validate if the player has provided enough ingredients
            val itemStackArray = player.craftingGrid.input.flatItems
            for (slot in itemStackArray.indices) {
                val ingredient = itemStackArray[slot]
                // Skip empty slot because we have checked item type above
                if (ingredient.isNull) continue
                if (ingredient.getCount() < numberOfRequestedCrafts) {
                    CraftRecipeActionProcessor.log.warn(
                        "Not enough ingredients in slot {}! Expected: {}, Actual: {}",
                        slot,
                        numberOfRequestedCrafts,
                        ingredient.getCount()
                    )
                    return context.error()
                }
            }
            // Validate the consume action count which client sent
            // 还有一部分检查被放在了ConsumeActionProcessor里面（例如消耗物品数量检查）
            val consumeActions = findAllConsumeActions(context.itemStackRequest.actions, context.currentActionIndex + 1)
            val consumeActionCountNeeded = input.canConsumerItemCount()
            if (consumeActions.size != consumeActionCountNeeded) {
                CraftRecipeActionProcessor.log.warn("Mismatched consume action count! Expected: " + consumeActionCountNeeded + ", Actual: " + consumeActions.size + " on inventory " + craft.javaClass.simpleName)
                return context.error()
            }
            if (recipe.results.size == 1) {
                // 若配方输出物品为1，客户端将不会发送CreateAction，此时我们直接在CraftRecipeAction输出物品到CREATED_OUTPUT
                // 若配方输出物品为多个，客户端将会发送CreateAction，此时我们将在CreateActionProcessor里面输出物品到CREATED_OUTPUT
                val output: Item = recipe.results.getFirst().clone()
                output.setCount(output.getCount() * numberOfRequestedCrafts)
                val createdOutput = player.creativeOutputInventory
                createdOutput.setItem(0, output.clone().autoAssignStackNetworkId(), false)
                context.put(RECIPE_DATA_KEY, recipe)
            }
        }
        return null
    }

    fun handleSmithingUpgrade(
        recipe: SmithingTransformRecipe,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            CraftRecipeActionProcessor.log.error("the player's haven't open any inventory!")
            return context.error()
        }
        if (topWindow.get() !is SmithingInventory) {
            CraftRecipeActionProcessor.log.error("the player's haven't open smithing inventory! Instead " + topWindow.get().javaClass.simpleName)
            return context.error()
        }
        val equipment: Item = smithingInventory.getEquipment()
        val ingredient: Item = smithingInventory.getIngredient()
        val template: Item = smithingInventory.getTemplate()

        val expectEquipment = recipe.base
        val expectIngredient = recipe.addition
        val expectTemplate = recipe.template
        var match = expectEquipment.match(equipment)
        match = match and expectIngredient.match(ingredient)
        match = match and expectTemplate.match(template)
        if (match) {
            val result = recipe.result.clone()
            val tag = equipment.namedTag
            if (tag != null) {
                result.setCompoundTag(tag.copy())
            }
            player.creativeOutputInventory.setItem(result)
            return null
        }
        return context.error()
    }

    fun handleSmithingTrim(player: Player, context: ItemStackRequestContext): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            CraftRecipeActionProcessor.log.error("the player's haven't open any inventory!")
            return context.error()
        }
        if (topWindow.get() !is SmithingInventory) {
            CraftRecipeActionProcessor.log.error("the player's haven't open smithing inventory!")
            return context.error()
        }
        val equipment: Item = smithingInventory.getEquipment()
        val ingredient: Item = smithingInventory.getIngredient()
        val template: Item = smithingInventory.getTemplate()

        if (!ingredient.isNull && !template.isNull) {
            val find1 = TrimData.trimPatterns.stream()
                .filter { trimPattern: TrimPattern -> template.id == trimPattern.itemName }.findFirst()
            val find2 = TrimData.trimMaterials.stream()
                .filter { trimMaterial: TrimMaterial -> ingredient.id == trimMaterial.itemName }.findFirst()
            if (equipment.isNull || find1.isEmpty || find2.isEmpty) {
                return context.error()
            }
            val trimPattern = find1.get()
            val trimMaterial = find2.get()
            val result = equipment.clone()
            val trim = CompoundTag().putString("Material", trimMaterial.materialId)
                .putString("Pattern", trimPattern.patternId)
            var compound = ingredient.namedTag
            compound = if (compound == null) {
                result.getOrCreateNamedTag()
            } else compound.copy() // Ensure no cached CompoundTags are used double

            compound.putCompound("Trim", trim)
            result.setNamedTag(compound)
            player.creativeOutputInventory.setItem(result)
            return null
        }
        return context.error()
    }

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RECIPE

    companion object {
        const val RECIPE_DATA_KEY: String = "recipe"
        const val ENCH_RECIPE_KEY: String = "ench_recipe"

        fun findAllConsumeActions(actions: Array<ItemStackRequestAction?>, startIndex: Int): List<ConsumeAction> {
            val found = ArrayList<ConsumeAction>()
            for (i in startIndex..<actions.size) {
                val action = actions[i]
                if (action is ConsumeAction) {
                    found.add(action)
                }
            }
            return found
        }
    }
}
