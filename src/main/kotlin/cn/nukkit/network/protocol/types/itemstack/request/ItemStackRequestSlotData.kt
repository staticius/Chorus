package cn.nukkit.network.protocol.types.itemstack.request

import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import lombok.Value

/**
 * Holds information on a specific slot client-side.
 */
@Value
class ItemStackRequestSlotData {
    /**
     * container that the slots that follow are in.
     *
     */
    @Deprecated("since v712 - FullContainerName#getContainer should be preferred")
    var container: ContainerSlotType? = null

    /**
     * slot is the index of the slot within the container
     */
    var slot: Int = 0

    /**
     * stackNetworkId is the unique stack ID that the client assumes to be present in this slot. The server
     * must check if these IDs match. If they do not match, servers should reject the stack request that the
     * action holding this info was in.
     */
    var stackNetworkId: Int = 0

    var containerName: FullContainerName? = null
}
