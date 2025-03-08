package cn.nukkit.utils.functional


fun interface BlockPositionDataConsumer<D> {
    fun accept(x: Int, y: Int, z: Int, data: D)
}
