package org.chorus.event.player

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.item.Item
import org.chorus.math.BlockFace

abstract class PlayerBucketEvent(
    who: Player?,
    blockClicked: Block,
    blockFace: BlockFace,
    liquid: Block,
    bucket: Item,
    itemInHand: Item
) :
    PlayerEvent(), Cancellable {
    val blockClicked: Block

    val blockFace: BlockFace

    private val liquid: Block

    /**
     * Returns the bucket used in this event
     * @return bucket
     */
    val bucket: Item

    /**
     * Returns the item in hand after the event
     * @return item
     */
    @JvmField
    var item: Item


    init {
        this.player = who
        this.blockClicked = blockClicked
        this.blockFace = blockFace
        this.liquid = liquid
        this.item = itemInHand
        this.bucket = bucket
    }
}
