package org.chorus_oss.chorus.inventory

enum class InventoryType(val networkType: Int) {
    NONE(-9),
    INVENTORY(-1),
    CONTAINER(0),
    WORKBENCH(1),
    FURNACE(2),
    ENCHANTMENT(3),
    BREWING_STAND(4),
    ANVIL(5),
    DISPENSER(6),
    DROPPER(7),
    HOPPER(8),
    CAULDRON(9),
    MINECART_CHEST(10),
    MINECART_HOPPER(11),
    HORSE(12),
    BEACON(13),
    STRUCTURE_EDITOR(14),
    TRADE(15),
    COMMAND_BLOCK(16),
    JUKEBOX(17),
    ARMOR(18),
    HAND(19),
    COMPOUND_CREATOR(20),
    ELEMENT_CONSTRUCTOR(21),
    MATERIAL_REDUCER(22),
    LAB_TABLE(23),
    LOOM(24),
    LECTERN(25),
    GRINDSTONE(26),
    BLAST_FURNACE(27),
    SMOKER(28),
    STONECUTTER(29),
    CARTOGRAPHY(30),
    HUD(31),
    JIGSAW_EDITOR(32),
    SMITHING_TABLE(33),
    CHEST_BOAT(34),
    DECORATED_POT(35),
    CRAFTER(36);

    companion object {
        @JvmStatic
        fun from(id: Int): InventoryType {
            return entries.find { it.networkType == id } ?: throw RuntimeException("Unknown InventoryType: $id")
        }
    }
}
