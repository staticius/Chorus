package org.chorus.level.generator

import org.chorus.Server
import org.chorus.level.DimensionData
import org.chorus.level.generator.stages.FinishedStage
import org.chorus.level.generator.stages.FlatGenerateStage
import org.chorus.level.generator.stages.LightPopulationStage
import org.chorus.registry.Registries


class Flat(dimensionData: DimensionData, options: Map<String?, Any>?) :
    Generator(dimensionData, options) {
    override fun stages(builder: GenerateStage.Builder) {
        builder.start(Registries.GENERATE_STAGE.get(FlatGenerateStage.NAME))
        if (Server.instance.settings.chunkSettings.lightUpdates) {
            builder.next(Registries.GENERATE_STAGE.get(LightPopulationStage.NAME))
        }
        builder.next(Registries.GENERATE_STAGE.get(FinishedStage.NAME))
    }

    override val name: String
        get() = "flat"
}
