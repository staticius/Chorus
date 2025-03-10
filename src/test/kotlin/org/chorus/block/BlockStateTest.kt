package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BlockPropertyType.BlockPropertyValue
import org.chorus.registry.BlockRegistry
import org.chorus.registry.Registries
import com.google.gson.JsonParser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class BlockStateTest @Test @SneakyThrows internal constructor() {
    init {
        Registries.BLOCK.init()
        var blocks = 0
        FileInputStream("src/main/resources/block_states.json").use { stream ->
            val reader = BufferedReader(InputStreamReader(stream))
            val blockStateData = JsonParser.parseReader(reader).asJsonArray
            for (i in 0..<blockStateData.size()) {
                val entry = blockStateData[i].asJsonObject
                val hash = entry["blockStateHash"].asInt
                val name = entry["name"].asString
                if (BlockRegistry.skipBlockSet.contains(name)) continue
                val state = Registries.BLOCKSTATE[hash]
                if (state == null) {
                    throw RuntimeException("$name ($hash) was not a part of block_states.json.")
                } else {
                    if (state.identifier != name) {
                        throw RuntimeException("BlockState " + hash + " was not " + name + ". Instead it is " + state.identifier)
                    }
                }
                blocks++
            }
            Assertions.assertEquals(blocks, Registries.BLOCKSTATE.allState.size)
        }
    }

    @Test
    @SneakyThrows
    fun BlockStateImpl_computeSpecialValue() {
        val i1 = BlockState.computeSpecialValue(
            arrayOf<BlockPropertyValue<*, *, *>>(
                CommonBlockProperties.DIRECTION.createValue(1),  //2bit
                CommonBlockProperties.OPEN_BIT.createValue(false),  //1bit
                CommonBlockProperties.UPSIDE_DOWN_BIT.createValue(false) //1bit
            )
        )
        Assertions.assertEquals((1 shl 2 or (0 shl 1) or 0), i1.toInt())
    }
}
