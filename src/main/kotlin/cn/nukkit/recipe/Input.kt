package cn.nukkit.recipe

import cn.nukkit.item.Item

/**
 * craft input,Upper left is origin point (0,0)
 */
class Input(
    var col: Int, var row: Int,
    /**
     * The item matrix
     *
     *
     * Each array element in the array represents a row of items in the craft table
     */
    var data: Array<Array<Item>>?
) {
    fun canConsumerItemCount(): Int {
        var count = 0
        for (d in data!!) {
            for (item in d) {
                if (!item.isNull) {
                    count++
                }
            }
        }
        return count
    }

    val flatItems: Array<Item?>
        get() {
            val items =
                arrayOfNulls<Item>(col * row)
            var index = 0
            for (i in data!!) {
                for (p in i) {
                    items[index++] = p
                }
            }
            return items
        }

    companion object {
        val EMPTY_INPUT_ARRAY: Array<Array<Item?>> = Array(0) { arrayOfNulls(0) }
    }
}
