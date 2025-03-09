package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.inventory.LoomInventory
import org.chorus.item.*
import org.chorus.network.protocol.types.BannerPattern
import org.chorus.network.protocol.types.BannerPatternType
import org.chorus.network.protocol.types.itemstack.request.action.CraftLoomAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.utils.DyeColor
import lombok.extern.slf4j.Slf4j

/**
 * @author CoolLoong
 */
@Slf4j
class CraftLoomActionProcessor : ItemStackRequestActionProcessor<CraftLoomAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_LOOM

    override fun handle(action: CraftLoomAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            CraftLoomActionProcessor.log.error("the player's haven't open any inventory!")
            return context.error()
        }
        if (topWindow.get() !is LoomInventory) {
            CraftLoomActionProcessor.log.error("the player's haven't open loom inventory!")
            return context.error()
        }
        val banner: Item = loomInventory.getBanner()
        val dye: Item = loomInventory.getDye()
        if ((banner == null || banner.isNull) || (dye == null || dye.isNull)) {
            return context.error()
        }

        val pattern: Item = loomInventory.getPattern()
        var patternType: BannerPatternType? = null
        if (pattern is ItemBannerPattern && action.patternId != null && !action.patternId.isBlank()) {
            patternType = pattern.patternType
            if (action.patternId != patternType.code) return context.error()
        }
        var dyeColor: DyeColor? = DyeColor.BLACK
        if (dye is ItemDye) {
            dyeColor = dye.dyeColor
        }
        val result = ItemBanner()
        if (patternType != null) {
            result.addPattern(BannerPattern(patternType, dyeColor))
        } else {
            result.setBaseColor(dyeColor!!)
        }
        player.creativeOutputInventory.setItem(result)
        return null
    }
}
