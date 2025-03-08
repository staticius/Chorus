package cn.nukkit.level.generator.stages

import cn.nukkit.level.generator.ChunkGenerateContext
import cn.nukkit.level.generator.GenerateStage

class LightPopulationStage : GenerateStage() {
    override fun name(): String {
        return NAME
    }

    override fun apply(context: ChunkGenerateContext) {
        val chunk = context.chunk ?: return
        chunk.recalculateHeightMap()
        chunk.populateSkyLight()
        chunk.setLightPopulated()
    }

    companion object {
        const val NAME: String = "light_population"
    }
}
