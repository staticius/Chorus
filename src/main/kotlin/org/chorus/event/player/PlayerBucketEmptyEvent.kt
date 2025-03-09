package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class PlayerBucketEmptyEvent(
    who: Player?,
    blockClicked: Block,
    blockFace: BlockFace,
    liquid: Block,
    bucket: Item,
    itemInHand: Item
) :
    PlayerBucketEvent(who, blockClicked, blockFace, liquid, bucket, itemInHand) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
