package org.chorus.network.protocol.types.inventory.creative

import cn.nukkit.item.Item
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString

@Getter
@AllArgsConstructor
@ToString
class CreativeItemData {
    private val item: Item? = null
    private val groupId = 0
}
