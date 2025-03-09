package org.chorus.level.generator.terra.delegate

import org.chorus.block.BlockAir
import com.dfsek.terra.api.block.BlockType
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.block.state.properties.Property
import lombok.extern.slf4j.Slf4j


@JvmRecord
data class PNXBlockStateDelegate(val innerBlockState: cn.nukkit.block.BlockState) : BlockState {
    override fun matches(blockState: BlockState): Boolean {
        return (blockState as PNXBlockStateDelegate).innerBlockState == this.innerBlockState
    }

    override fun <T : Comparable<T>?> has(property: Property<T>): Boolean {
        return false
    }

    override fun <T : Comparable<T>?> get(property: Property<T>): T {
        return null
    }

    override fun <T : Comparable<T>?> set(property: Property<T>, t: T): BlockState? {
        return null
    }

    override fun getBlockType(): BlockType {
        return PNXBlockType(innerBlockState)
    }

    override fun getAsString(properties: Boolean): String {
//        JeBlockState jeBlock = MappingRegistries.BLOCKS.getJEBlock(innerBlockState);
//        if (properties) {
//            return jeBlock;
//        } else {
//            int i = jeBlock.indexOf('[');
//            if (i != -1) {
//                return jeBlock.substring(0, jeBlock.indexOf('['));
//            } else {
//                return jeBlock;
//            }
//        }
        //todo support properties
        val name = innerBlockState.identifier
        //对于一些特殊方块的HACK
        return when (name) {
            "minecraft:snow_layer" -> "minecraft:snow"
            "minecraft:snow" -> "minecraft:snow_block"
            else -> name
        }
    }

    override fun isAir(): Boolean {
        return innerBlockState === BlockAir.STATE
    }

    override fun getHandle(): cn.nukkit.block.BlockState {
        return innerBlockState
    }
}
