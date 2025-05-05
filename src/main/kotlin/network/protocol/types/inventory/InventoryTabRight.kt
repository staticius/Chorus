package org.chorus_oss.chorus.network.protocol.types.inventory

enum class InventoryTabRight {
    NONE,
    FULL_SCREEN,
    CRAFTING,
    ARMOR;

    companion object {
        val VALUES: Array<InventoryTabRight> = entries.toTypedArray()
    }
}
