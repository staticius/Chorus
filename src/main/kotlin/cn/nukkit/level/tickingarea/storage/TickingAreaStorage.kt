package cn.nukkit.level.tickingarea.storage

interface TickingAreaStorage {
    fun addTickingArea(area: TickingArea)

    fun addTickingArea(vararg areas: TickingArea?) {
        for (area in areas) {
            addTickingArea(area)
        }
    }

    fun readTickingArea(): Map<String, TickingArea>

    fun removeTickingArea(name: String)

    fun removeAllTickingArea()

    fun containTickingArea(name: String?): Boolean
}
