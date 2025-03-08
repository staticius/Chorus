package cn.nukkit.level

enum class DimensionEnum(@JvmField val dimensionData: DimensionData) {
    OVERWORLD(DimensionData(Level.Companion.DIMENSION_OVERWORLD, -64, 319, 24)),
    NETHER(DimensionData(Level.Companion.DIMENSION_NETHER, 0, 127, 8)),
    END(DimensionData(Level.Companion.DIMENSION_THE_END, 0, 255, 16));

    companion object {
        fun getDataFromId(dimension: Int): DimensionData? {
            for (value in entries) {
                if (value.dimensionData.dimensionId == dimension) {
                    return value.dimensionData
                }
            }
            return null
        }
    }
}
