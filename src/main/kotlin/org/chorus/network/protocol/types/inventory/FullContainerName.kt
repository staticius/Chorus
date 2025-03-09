package org.chorus.network.protocol.types.inventory

import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import lombok.Value

@Value
class FullContainerName {
    var container: ContainerSlotType? = null

    var dynamicId: Int? = null
}
