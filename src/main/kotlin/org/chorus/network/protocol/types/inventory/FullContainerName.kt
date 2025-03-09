package org.chorus.network.protocol.types.inventory

import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import lombok.Value

@Value
class FullContainerName {
    var container: ContainerSlotType? = null

    var dynamicId: Int? = null
}
