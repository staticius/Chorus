package org.chorus.network.protocol.types.inventory.creative

import org.chorus.item.Item

data class CreativeItemGroup(
    val category: CreativeItemCategory,
    val name: String,
    val icon: Item,
)
