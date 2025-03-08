package cn.nukkit.inventory.request

import cn.nukkit.Player
import cn.nukkit.network.protocol.types.itemstack.request.action.CraftRecipeAction
import cn.nukkit.network.protocol.types.itemstack.request.action.CreateAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.registry.Registries
import lombok.extern.slf4j.Slf4j
import java.util.*

/**
 * Allay Project 2023/12/2
 *
 * @author daoge_cmd
 */
@Slf4j
class CreateActionProcessor : ItemStackRequestActionProcessor<CreateAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CREATE

    override fun handle(action: CreateAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val itemStackRequestAction = Arrays.stream(context.itemStackRequest.actions)
            .filter { action1: ItemStackRequestAction? -> action1 is CraftRecipeAction }.findFirst()
        if (itemStackRequestAction.isEmpty) {
            CreateActionProcessor.log.warn("Recipe not found in ItemStackRequest Context! Context: $context")
            return context.error()
        }
        val recipe =
            Registries.RECIPE.getRecipeByNetworkId((itemStackRequestAction.get() as CraftRecipeAction).recipeNetworkId)
        val output = recipe.results[action.slot]
        val createdOutput = player.creativeOutputInventory
        createdOutput.setItem(0, output, false)
        return null
    }
}
