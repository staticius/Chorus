package cn.nukkit.level.generator.terra.delegate

import com.dfsek.terra.api.world.biome.PlatformBiome

@JvmRecord
data class PNXBiomeDelegate(val innerBiome: Int) : PlatformBiome {
    override fun getHandle(): Int {
        return innerBiome
    }
}
