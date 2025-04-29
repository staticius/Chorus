package org.chorus_oss.chorus.network.protocol.types.inventory.creative

import org.chorus_oss.chorus.item.Item

data class CreativeItemData(
    val item: Item,
    val groupId: Int
)
