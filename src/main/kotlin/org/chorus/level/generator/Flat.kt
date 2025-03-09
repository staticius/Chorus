package org.chorus.level.generator

import org.chorus.Server
import org.chorus.level.DimensionData
import org.chorus.registry.Registries

/**
 * @author MagicDroidX (Nukkit Project)
 */
class Flat(dimensionData: DimensionData, options: Map<String?, Any>?) :
    Generator(dimensionData, options) {
    override fun stages(builder: GenerateStage.Builder) {
        builder.start(Registries.GENERATE_STAGE.get(FlatGenerateStage.Companion.NAME))
        if (Server.getInstance().settings.chunkSettings().lightUpdates()) {
            builder.next(Registries.GENERATE_STAGE.get(LightPopulationStage.Companion.NAME))
        }
        builder.next(Registries.GENERATE_STAGE.get(FinishedStage.Companion.NAME))
    }

    override val name: String
        get() = "flat"
}
