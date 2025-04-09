package org.chorus.event.player

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.HandlerList
import org.chorus.item.Item
import org.chorus.math.BlockFace

class PlayerBucketEmptyEvent(
    player: Player,
    blockClicked: Block,
    blockFace: BlockFace,
    liquid: Block,
    bucket: Item,
    itemInHand: Item
) :
    PlayerBucketEvent(player, blockClicked, blockFace, liquid, bucket, itemInHand) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
