package org.chorus.level.generator.stages

import org.chorus.level.generator.ChunkGenerateContext
import org.chorus.level.generator.GenerateStage

class LightPopulationStage : GenerateStage() {
    override fun name(): String {
        return NAME
    }

    override fun apply(context: ChunkGenerateContext) {
        val chunk = context.chunk
        chunk.recalculateHeightMap()
        chunk.populateSkyLight()
    }

    companion object {
        const val NAME: String = "light_population"
    }
}
