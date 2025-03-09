package org.chorus.level.format

import cn.nukkit.level.DimensionData
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import lombok.experimental.Accessors
import java.util.concurrent.ThreadLocalRandom

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(fluent = true)
class LevelConfig {
    var format: String = "leveldb"
    var enable: Boolean = true
    var generators: Map<Int, GeneratorConfig>? = null

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(fluent = true)
    class GeneratorConfig {
        var name: String? = null
        var seed: Long = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
        var enableAntiXray: Boolean = false
        var antiXrayMode: AntiXrayMode = AntiXrayMode.LOW
        var preDeobfuscate: Boolean = true
        var dimensionData: DimensionData? = null
        var preset: Map<String, Any>? = null
    }

    enum class AntiXrayMode {
        LOW,
        MEDIUM,
        HIGH
    }
}
