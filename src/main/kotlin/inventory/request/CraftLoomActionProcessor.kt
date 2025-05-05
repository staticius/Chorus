package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.inventory.LoomInventory
import org.chorus_oss.chorus.item.ItemBanner
import org.chorus_oss.chorus.item.ItemBannerPattern
import org.chorus_oss.chorus.item.ItemDye
import org.chorus_oss.chorus.network.protocol.types.BannerPattern
import org.chorus_oss.chorus.network.protocol.types.BannerPatternType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.CraftLoomAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.utils.DyeColor
import org.chorus_oss.chorus.utils.Loggable

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
        val loomInventory = topWindow.get() as LoomInventory
        val banner = loomInventory.banner
        val dye = loomInventory.dye
        if ((banner.isNothing) || (dye.isNothing)) {
            return context.error()
        }

        val pattern = loomInventory.pattern
        var patternType: BannerPatternType? = null
        if (pattern is ItemBannerPattern && action.patternId.isNotBlank()) {
            patternType = pattern.patternType
            if (action.patternId != patternType?.code) return context.error()
        }
        var dyeColor: DyeColor = DyeColor.BLACK
        if (dye is ItemDye) {
            dyeColor = dye.dyeColor
        }
        val result = ItemBanner()
        if (patternType != null) {
            result.addPattern(BannerPattern(patternType, dyeColor))
        } else {
            result.setBaseColor(dyeColor)
        }
        player.creativeOutputInventory.setItem(result)
        return null
    }

    companion object : Loggable
}
