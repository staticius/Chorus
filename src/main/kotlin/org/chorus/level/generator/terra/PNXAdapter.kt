package org.chorus.level.generator.terra

import org.chorus.block.BlockState
import org.chorus.item.Item
import org.chorus.level.generator.terra.delegate.PNXBiomeDelegate
import org.chorus.level.generator.terra.delegate.PNXBlockStateDelegate
import org.chorus.level.generator.terra.delegate.PNXItemDelegate

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
