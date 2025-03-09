package org.chorus.item

import cn.nukkit.network.protocol.types.BannerPatternType

class ItemMojangBannerPattern : ItemBannerPattern(ItemID.Companion.MOJANG_BANNER_PATTERN) {
    override val patternType: BannerPatternType?
        get() = BannerPatternType.MOJANG

    override var damage: Int
        get() = super.damage
        set(damage) {
        }
}