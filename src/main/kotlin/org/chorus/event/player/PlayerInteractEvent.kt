package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item
import cn.nukkit.level.Locator
import cn.nukkit.math.BlockFace
import cn.nukkit.math.IVector3
import cn.nukkit.math.Vector3

/**
 * @author MagicDroidX (Nukkit Project)
 */
class PlayerInteractEvent @JvmOverloads constructor(
    player: Player,
    item: Item?,
    vector: IVector3,
    face: BlockFace?,
    action: Action = Action.RIGHT_CLICK_BLOCK
) :
    PlayerEvent(), Cancellable {
    var block: Block? = null
    val face: BlockFace?
    @JvmField
    val item: Item?
    var touchVector: Vector3? = null
    @JvmField
    val action: Action

    init {
        if (vector is Block) {
            this.block = vector
            this.touchVector = Vector3(0.0, 0.0, 0.0)
        } else {
            this.touchVector = vector.vector3
            this.block = Block.get(
                Block.AIR, Locator(
                    0.0, 0.0, 0.0,
                    player.level!!
                )
            )
        }

        this.player = player
        this.item = item
        this.face = face
        this.action = action
    }

    enum class Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
