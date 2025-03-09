package org.chorus.item

import cn.nukkit.network.protocol.types.BannerPatternType

class ItemBordureIndentedBannerPattern : ItemBannerPattern(ItemID.Companion.BORDURE_INDENTED_BANNER_PATTERN) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }

    override val patternType: BannerPatternType?
        get() = BannerPatternType.BORDER
}