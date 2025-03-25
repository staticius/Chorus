package org.chorus.inventory

import org.chorus.level.Level
import org.chorus.math.Vector3

interface InventoryHolder {
    val inventory: Inventory

    val level: Level?

    val vector3: Vector3
}
