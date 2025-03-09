package org.chorus.network.protocol.types.itemstack.response

import org.chorus.network.protocol.ItemStackResponsePacket
import org.chorus.network.protocol.types.itemstack.request.ItemStackRequest
import lombok.Data

/**
 * Represents an individual response to a [ItemStackRequest]
 * sent as part of [ItemStackResponsePacket].
 */

class ItemStackResponse(
    /**
     * Replaces the success boolean as of v419
     */
    var result: ItemStackResponseStatus,
    /**
     * requestId is the unique ID of the request that this response is in reaction to. If rejected, the client
     * will undo the actions from the request with this ID.
     */
    var requestId: Int,
    /**
     * containers holds information on the containers that had their contents changed as a result of the
     * request.
     */
    var containers: List<ItemStackResponseContainer>
)
