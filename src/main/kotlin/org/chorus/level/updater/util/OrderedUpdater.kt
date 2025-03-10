package org.chorus.level.updater.util



/**
 * Stores updates from ints to Strings.
 */
class OrderedUpdater(oldProperty: String?, newProperty: String?, offset: Int, vararg order: String) {

    private val oldProperty: String?


    private val newProperty: String?

    private val order: Array<String>
    private val offset: Int

    /**
     * [.OrderedUpdater] with an offset of 0 (old values start at 0)
     */
    constructor(oldProperty: String?, newProperty: String?, vararg order: String?) : this(
        oldProperty,
        newProperty,
        0,
        *order
    )

    /**
     * Creates an OrderedUpdater whose values are provided by the ordered array.
     *
     * @param oldProperty the old state property
     * @param newProperty the new state property
     * @param offset      the difference between a value's old integer type and the value's index in the array.
     * If the first element has an old value of n, then the offset is n.
     * @param order       an array of ordered values
     */
    init {
        require(order.size >= 1) { "empty order array" }
        this.oldProperty = oldProperty
        this.newProperty = newProperty
        this.offset = offset
        this.order = order
    }

    fun translate(value: Int): String {
        var index = value - offset
        if (index < 0 || index >= order.size) {
            index = 0
        }
        return order[index]
    }

    companion object {
        val FACING_TO_BLOCK: OrderedUpdater = OrderedUpdater(
            "facing_direction", "minecraft:block_face",
            "down", "up", "north", "south", "west", "east"
        )

        val FACING_TO_CARDINAL: OrderedUpdater = OrderedUpdater(
            "facing_direction", "minecraft:cardinal_direction", 2,
            "north", "south", "west", "east"
        )

        val DIRECTION_TO_CARDINAL: OrderedUpdater = OrderedUpdater(
            "direction", "minecraft:cardinal_direction",
            "south", "west", "north", "east"
        )
    }
}
