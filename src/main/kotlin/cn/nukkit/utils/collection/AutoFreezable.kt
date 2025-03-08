package cn.nukkit.utils.collection


interface AutoFreezable {
    val freezeStatus: FreezeStatus

    val temperature: Int

    /**
     * this.temperature += temperature; <br></br>
     * 带有沸点检查，没有绝对零度检查！
     * @param temperature 温度变化量
     */
    fun warmer(temperature: Int)

    /**
     * this.temperature -= temperature; <br></br>
     * 带有绝对零度检查，没有沸点检查！
     * @param temperature 温度变化量
     */
    fun colder(temperature: Int)

    /**
     * 强制冻结数组
     */
    fun freeze()

    /**
     * 强制深度冻结数组
     */
    fun deepFreeze()

    @ShouldThaw
    fun thaw()

    enum class FreezeStatus {
        NONE, FREEZING, FREEZE, DEEP_FREEZING, DEEP_FREEZE, THAWING
    }
}
