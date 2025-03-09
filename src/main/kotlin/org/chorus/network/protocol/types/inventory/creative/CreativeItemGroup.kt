package org.chorus.network.protocol.types.inventory.creative

import org.chorus.item.Item
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString




class CreativeItemGroup {
    private val category: CreativeItemCategory? = null
    private val name: String? = null
    private val icon: Item? = null
}
