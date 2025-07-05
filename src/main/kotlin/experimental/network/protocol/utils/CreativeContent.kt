package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.network.protocol.types.inventory.creative.CreativeItemCategory
import org.chorus_oss.chorus.network.protocol.types.inventory.creative.CreativeItemData
import org.chorus_oss.chorus.network.protocol.types.inventory.creative.CreativeItemGroup
import org.chorus_oss.protocol.types.creative.CreativeCategory
import org.chorus_oss.protocol.types.creative.CreativeGroup
import org.chorus_oss.protocol.types.creative.CreativeItem
import org.chorus_oss.protocol.types.item.ItemInstance

operator fun CreativeGroup.Companion.invoke(from: CreativeItemGroup): CreativeGroup {
    return CreativeGroup(
        category = CreativeCategory(from.category),
        name = from.name,
        icon = ItemInstance(from.icon)
    )
}

operator fun CreativeItem.Companion.invoke(from: CreativeItemData): CreativeItem {
    return CreativeItem(
        creativeItemNetworkID = from.netID.toUInt(),
        item = ItemInstance(from.item),
        groupIndex = from.groupId.toUInt(),
    )
}

operator fun CreativeCategory.Companion.invoke(from: CreativeItemCategory): CreativeCategory {
    return CreativeCategory.entries[from.ordinal]
}