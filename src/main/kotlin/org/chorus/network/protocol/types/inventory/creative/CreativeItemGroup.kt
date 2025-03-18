package org.chorus.network.protocol.types.inventory.creative

import org.chorus.item.Item

data class CreativeItemGroup (
    val category: CreativeItemCategory? = null,
    val name: String? = null,
    val icon: Item? = null,
)
