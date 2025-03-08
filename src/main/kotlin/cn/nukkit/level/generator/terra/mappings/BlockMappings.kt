package cn.nukkit.level.generator.terra.mappings

import cn.nukkit.block.BlockState
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import lombok.Builder
import lombok.Data
import lombok.extern.slf4j.Slf4j


@Builder
@Data
@Slf4j
class BlockMappings {
    var mapping1: Object2ObjectOpenHashMap<JeBlockState, BlockState>? = null
    var mapping2: Object2ObjectOpenHashMap<BlockState, JeBlockState>? = null

    fun getPNXBlock(blockState: JeBlockState): BlockState? {
        return mapping1!![blockState]
    }

    fun getJEBlock(blockState: BlockState): JeBlockState? {
        return mapping2!![blockState]
    }
}
