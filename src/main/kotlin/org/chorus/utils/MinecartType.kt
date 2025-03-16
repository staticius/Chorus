package org.chorus.utils

/**
 * Helper class of Minecart variants
 *
 *
 * By Adam Matthew
 * Creation time: 2017/7/17 19:55.
 */
enum class MinecartType(
    number: Int,
    private val hasBlockInside: Boolean,
    val minecartName: String
) {
    /**
     * Represents an empty vehicle.
     */
    MINECART_EMPTY(0, false, "Minecart"),

    /**
     * Represents a chest holder.
     */
    MINECART_CHEST(1, true, "Minecart with Chest"),

    /**
     * Represents a furnace minecart.
     */
    MINECART_FURNACE(2, true, "Minecart with Furnace"),

    MINECART_TNT(3, true, "Minecart with TNT"),
    MINECART_MOB_SPAWNER(4, true, "Minecart with Mob Spawner"),
    MINECART_HOPPER(5, true, "Minecart with Hopper"),
    MINECART_COMMAND_BLOCK(6, true, "Minecart with Command Block"),
    MINECART_UNKNOWN(-1, false, "Unknown Minecart");

    /**
     * Get the variants of the current minecart
     *
     * @return Integer
     */
    val id: Int = number

    /**
     * Gets if the minecart contains block
     *
     * @return Boolean
     */
    fun hasBlockInside(): Boolean {
        return hasBlockInside
    }

    companion object {
        private val TYPES: Map<Int, MinecartType> = entries.associateBy { it.id }

        /**
         * Returns of an instance of Minecart-variants
         *
         * @param types The number of minecart
         * @return Integer
         */
        fun valueOf(types: Int): MinecartType {
            val what = TYPES[types]
            return what ?: MINECART_UNKNOWN
        }
    }
}
