package cn.nukkit.inventory

import cn.nukkit.level.Level
import cn.nukkit.math.Vector3

interface InventoryHolder {
    @JvmField
    val inventory: Inventory?

    val level: Level

    val x: Double

    val y: Double

    val z: Double

    val vector3: Vector3
}
