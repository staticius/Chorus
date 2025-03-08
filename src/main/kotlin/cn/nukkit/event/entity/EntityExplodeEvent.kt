package cn.nukkit.event.entity

import cn.nukkit.block.Block
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Locator

/**
 * @author Angelic47 (Nukkit Project)
 */
class EntityExplodeEvent(entity: Entity?, locator: Locator, blocks: List<Block>, yield: Double) :
    EntityEvent(), Cancellable {
    val position: Locator
    var blockList: List<Block>

    @JvmField
    var ignitions: Set<Block>
    @JvmField
    var yield: Double

    init {
        this.entity = entity
        this.position = locator
        this.blockList = blocks
        this.yield = yield
        this.ignitions = HashSet(0)
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
