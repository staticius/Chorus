package org.chorus.network.protocol.types.inventory

import org.chorus.network.protocol.types.itemstack.ContainerSlotType

data class FullContainerName(
    var container: ContainerSlotType? = null,
    var dynamicId: Int? = null
)
