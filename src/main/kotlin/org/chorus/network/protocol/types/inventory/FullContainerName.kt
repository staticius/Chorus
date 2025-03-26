package org.chorus.network.protocol.types.inventory

import org.chorus.network.protocol.types.itemstack.ContainerSlotType

data class FullContainerName(
    val container: ContainerSlotType,
    val dynamicId: Int? = null
)
