package cn.nukkit.network.protocol.types.itemstack.response

import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import lombok.Value

/**
 * ContainerEntry holds information on what slots in a container have what item stack in them.
 */
@Value
class ItemStackResponseContainer {
    /**
     * container that the slots that follow are in.
     *
     */
    @Deprecated("since v712 - FullContainerName#getContainer should be preferred")
    var container: ContainerSlotType? = null

    /**
     * items holds information on what item stack should be present in specific slots in the container.
     */
    var items: List<ItemStackResponseSlot>? = null

    var containerName: FullContainerName? = null
}
