package org.chorus_oss.chorus.network.protocol.types.inventory.creative

enum class CreativeItemCategory {
    ALL,
    CONSTRUCTION,
    NATURE,
    EQUIPMENT,
    ITEMS,
    ITEM_COMMAND_ONLY,
    UNDEFINED;

    companion object {
        @JvmField
        val VALUES: Array<CreativeItemCategory> = entries.toTypedArray()
    }
}
