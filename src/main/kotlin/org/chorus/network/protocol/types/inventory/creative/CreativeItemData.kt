package org.chorus.network.protocol.types.inventory.creative

import org.chorus.item.Item

data class CreativeItemData (
    val item: Item,
    val groupId: Int
)
