package org.chorus.level.generator.terra

import cn.nukkit.block.BlockState
import cn.nukkit.item.Item
import cn.nukkit.level.generator.terra.delegate.PNXBiomeDelegate
import cn.nukkit.level.generator.terra.delegate.PNXBlockStateDelegate
import cn.nukkit.level.generator.terra.delegate.PNXItemDelegate

object PNXAdapter {
    fun adapt(pnxItem: Item): PNXItemDelegate {
        return PNXItemDelegate(pnxItem)
    }

    fun adapt(biome: Int): PNXBiomeDelegate {
        return PNXBiomeDelegate(biome)
    }

    fun adapt(blockState: BlockState?): PNXBlockStateDelegate {
        return PNXBlockStateDelegate(blockState!!)
    }
}
