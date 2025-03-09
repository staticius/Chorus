package org.chorus.item

import cn.nukkit.network.protocol.types.BannerPatternType

class ItemFieldMasonedBannerPattern : ItemBannerPattern(ItemID.Companion.FIELD_MASONED_BANNER_PATTERN) {
    override val patternType: BannerPatternType?
        get() = BannerPatternType.BRICKS

    override var damage: Int
        get() = super.damage
        set(damage) {
        }
}