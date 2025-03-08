package cn.nukkit.network.protocol.types.itemstack.request

import cn.nukkit.network.protocol.ItemStackRequestPacket
import cn.nukkit.network.protocol.PlayerAuthInputPacket
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import lombok.RequiredArgsConstructor
import lombok.Value

/**
 * Request represents a single request present in an [ItemStackRequestPacket] or
 * [PlayerAuthInputPacket] sent by the client to
 * change an item in an inventory.
 * Item stack requests are either approved or rejected by the server using the ItemStackResponse packet.
 */
@Value
@RequiredArgsConstructor
class ItemStackRequest {
    /**
     * requestId is a unique ID for the request. This ID is used by the server to send a response for this
     * specific request in the ItemStackResponse packet.
     */
    var requestId: Int = 0

    /**
     * actions is a list of actions performed by the client. The actual type of the actions depends on which
     * ID was present
     */
    var actions: Array<ItemStackRequestAction>

    /**
     * Used for the server to determine which strings should be filtered. Used in anvils to verify a renamed item.
     *
     * @since v422
     */
    var filterStrings: Array<String>

    /**
     * @since v552
     */
    var textProcessingEventOrigin: TextProcessingEventOrigin? = null

    constructor(
        requestId: Int,
        actions: Array<ItemStackRequestAction?>,
        filterStrings: Array<String?>
    ) : this(requestId, actions, filterStrings, TextProcessingEventOrigin.BLOCK_ENTITY_DATA_TEXT)
}
