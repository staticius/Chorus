package cn.nukkit.network.protocol.types.inventory.creative

import cn.nukkit.item.Item
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString

@ToString
@Getter
@AllArgsConstructor
class CreativeItemGroup {
    private val category: CreativeItemCategory? = null
    private val name: String? = null
    private val icon: Item? = null
}
