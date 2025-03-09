package cn.nukkit.block

import cn.nukkit.event.block.BlockFadeEvent
import cn.nukkit.item.Item
import cn.nukkit.level.Level

interface IBlockOreRedstoneGlowing {
    val unlitBlock: Block

    val litBlock: Block?

    val level: Level

    fun toItem(): Item? {
        return unlitBlock.toItem()
    }

    fun onUpdate(block: Block, type: Int): Int {
        if (type == Level.BLOCK_UPDATE_SCHEDULED || type == Level.BLOCK_UPDATE_RANDOM) {
            val level = level
            val event = BlockFadeEvent(block, unlitBlock)
            level.server.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                level.setBlock(block.position, event.newState, true, true)
            }

            return Level.BLOCK_UPDATE_WEAK
        }
        return 0
    }
}
