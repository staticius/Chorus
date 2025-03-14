package org.chorus.level.format

import org.chorus.level.DimensionData


import java.util.concurrent.ThreadLocalRandom

data class LevelConfig(
    var format: String = "leveldb",
    var enable: Boolean = true,
    var generators: MutableMap<Int, GeneratorConfig> = mutableMapOf(),
) {
    data class GeneratorConfig(
        var name: String? = null,
        var seed: Long = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
        var enableAntiXray: Boolean = false,
        var antiXrayMode: AntiXrayMode = AntiXrayMode.LOW,
        var preDeobfuscate: Boolean = true,
        var dimensionData: DimensionData? = null,
        var preset: Map<String, Any>? = null,
    )

    enum class AntiXrayMode {
        LOW,
        MEDIUM,
        HIGH
    }
}
