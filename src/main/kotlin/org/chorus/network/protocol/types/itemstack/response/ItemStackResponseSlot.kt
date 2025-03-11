package org.chorus.network.protocol.types.itemstack.response



/**
 * ItemEntry holds information on what item stack should be present in a specific slot.
 */

class ItemStackResponseSlot(
    var slot: Int, var hotbarSlot: Int, var count: Int,
    /**
     * stackNetworkID is the network ID of the new stack at a specific slot.
     */
    var stackNetworkId: Int,
    /**
     * Holds the final custom name of a renamed item, if relevant.
     *
     * @since v422
     */
    var customName: String,
    /**
     * @since v766
     */
    var filteredCustomName: String,
    /**
     * @since v428
     */
    var durabilityCorrection: Int
) {
    constructor(
        requestedSlot: Int,
        slot: Int,
        amount: Int,
        stackNetworkId: Int,
        customName: String,
        durabilityCorrection: Int
    ) : this(requestedSlot, slot, amount, stackNetworkId, customName, "", durabilityCorrection)
}