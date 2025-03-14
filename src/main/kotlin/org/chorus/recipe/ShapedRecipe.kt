package org.chorus.recipe

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.netty.util.collection.CharObjectHashMap
import org.chorus.item.Item
import org.chorus.network.protocol.types.RecipeUnlockingRequirement
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.recipe.descriptor.ItemDescriptor
import org.chorus.registry.RecipeRegistry
import java.util.*

class ShapedRecipe @JvmOverloads constructor(
    recipeId: String?,
    uuid: UUID?,
    priority: Int,
    primaryResult: Item,
    shape: Array<String>,
    ingredients: Map<Char, ItemDescriptor>,
    extraResults: Collection<Item?>,
    mirror: Boolean,
    recipeUnlockingRequirement: RecipeUnlockingRequirement? = null
) :
    CraftingRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                Lists.asList<Item>(
                    primaryResult,
                    extraResults.toArray<Item>(Item.EMPTY_ARRAY)
                ), ingredients.values, RecipeType.SHAPED
            ), priority, recipeUnlockingRequirement
    ) {
    val shape: Array<String>
    private val shapedIngredients = CharObjectHashMap<ItemDescriptor>()
    val height: Int
    val width: Int
    val isMirror: Boolean

    constructor(
        primaryResult: Item,
        shape: Array<String>,
        ingredients: Map<Char, Item>,
        extraResults: List<Item?>
    ) : this(null, 1, primaryResult, shape, ingredients, extraResults)

    /**
     * Constructs a ShapedRecipe instance.
     *
     * @param primaryResult Primary result of the recipe
     * @param shape         <br></br>        Array of 1, 2, or 3 strings representing the rows of the recipe.
     * This accepts an array of 1, 2 or 3 strings. Each string should be of the same length and must be at most 3
     * characters long. Each character represents a unique type of ingredient. Spaces are interpreted as air.
     * @param ingredients   <br></br>  Char =&gt; Item map of items to be set into the shape.
     * This accepts an array of Items, indexed by character. Every unique character (except space) in the shape
     * array MUST have a corresponding item in this list. Space character is automatically treated as air.
     * @param extraResults  <br></br> List of additional result items to leave in the crafting grid afterwards. Used for things like cake recipe
     * empty buckets.
     *
     *
     * Note: Recipes do not need to be square. Do NOT add padding for empty rows/columns.
     */
    constructor(
        recipeId: String?,
        priority: Int,
        primaryResult: Item,
        shape: Array<String>,
        ingredients: Map<Char, Item>,
        extraResults: List<Item?>
    ) : this(
        recipeId, priority, primaryResult, shape,
        Maps.transformEntries<Char, Item, ItemDescriptor>(
            ingredients,
            Maps.EntryTransformer<Char, Item, ItemDescriptor> { k: Char?, v: Item -> DefaultDescriptor(v) }),
        extraResults
    )

    constructor(
        recipeId: String?,
        priority: Int,
        primaryResult: Item,
        shape: Array<String>,
        ingredients: Map<Char, ItemDescriptor>,
        extraResults: Collection<Item?>
    ) : this(recipeId, null, priority, primaryResult, shape, ingredients, extraResults, false)

    init {
        this.uuid = uuid
        this.height = shape.size
        this.isMirror = mirror
        if (this.height > 3 || this.height == 0) {
            throw RuntimeException("Shaped recipes may only have 1, 2 or 3 rows, not " + this.height)
        }

        this.width = shape[0].length
        if (this.width > 3 || this.width == 0) {
            throw RuntimeException("Shaped recipes may only have 1, 2 or 3 columns, not " + this.width)
        }

        var i = 0
        val shapeLength = shape.size
        while (i < shapeLength) {
            val row = shape[i]
            if (row.length != this.width) {
                throw RuntimeException("Shaped recipe rows must all have the same length (expected " + this.width + ", got " + row.length + ")")
            }

            for (x in 0..<this.width) {
                val c = row[x]
                if (c != ' ' && !ingredients.containsKey(c)) {
                    throw RuntimeException("No item specified for symbol '$c'")
                }
            }
            shape[i] = row.intern()
            i++
        }
        results.add(primaryResult.clone()) //primaryResult
        results.addAll(extraResults) //extraResults
        this.shape = shape

        for ((key, item) in ingredients) {
            if (java.lang.String.join("", *this.shape).indexOf(key) < 0) {
                throw RuntimeException("Symbol does not appear in the shape: $key")
            }
            shapedIngredients.put(key, item)
            this.ingredients.add(item)
        }
    }

    val result: Item
        get() = results.first()

    fun setIngredient(key: String, item: Item): ShapedRecipe {
        return this.setIngredient(key[0], item)
    }

    fun setIngredient(key: Char, item: Item): ShapedRecipe {
        return this.setIngredient(key, DefaultDescriptor(item))
    }

    fun setIngredient(key: Char, item: ItemDescriptor): ShapedRecipe {
        if (java.lang.String.join("", *this.shape).indexOf(key) < 0) {
            throw RuntimeException("Symbol does not appear in the shape: $key")
        }
        shapedIngredients.put(key, item)

        val items: MutableList<ItemDescriptor> = ArrayList()
        var y = 0
        val y2 = height
        while (y < y2) {
            var x = 0
            val x2 = width
            while (x < x2) {
                items.add(getIngredient(x, y))
                ++x
            }
            ++y
        }
        ingredients.clear()
        ingredients.addAll(items)
        return this
    }

    /**
     * Gets ingredient with a row number and col number.
     *
     * @param x the col
     * @param y the row
     * @return the ingredient
     */
    fun getIngredient(x: Int, y: Int): ItemDescriptor {
        val shape = shape[y]
        val res = if (x < shape.length) shapedIngredients[shape[x]] else null
        return res ?: DefaultDescriptor(Item.AIR)
    }

    override fun match(input: Input): Boolean {
        var mirrorInput: Input? = null
        if (isMirror && input.getCol() == 3 && input.getRow() == 3) {
            mirrorInput = Input(3, 3, mirrorItemArray(input.getData()))
        }
        tryShrinkMatrix(input)

        var checkMirror = false
        next@ for (i in 0..<input.getRow()) {
            for (j in 0..<input.getCol()) {
                val ingredient = getIngredient(j, i)
                if (!ingredient.match(input.getData()[i][j])) {
                    if (mirrorInput != null) {
                        checkMirror = true
                        break@next
                    } else {
                        return false
                    }
                }
            }
        }
        if (checkMirror) {
            tryShrinkMatrix(mirrorInput!!)
            for (i in 0..<mirrorInput.getRow()) {
                for (j in 0..<mirrorInput.getCol()) {
                    val ingredient = getIngredient(j, i)
                    if (!ingredient.match(mirrorInput.getData()[i][j])) {
                        return false
                    }
                }
            }
        }
        return true
    }

    override val type: RecipeType
        get() = RecipeType.SHAPED

    companion object {
        private fun mirrorItemArray(data: Array<Array<Item?>?>): Array<Array<Item>> {
            val clone = Array(3) { arrayOfNulls<Item>(3) }
            System.arraycopy(data[0], 0, clone[0], 0, 3)
            System.arraycopy(data[1], 0, clone[1], 0, 3)
            System.arraycopy(data[2], 0, clone[2], 0, 3)
            var tmp: Item?
            for (i in 0..2) {
                tmp = clone[i][2]
                clone[i][2] = clone[i][0]
                clone[i][0] = tmp
            }
            return clone
        }

        /**
         * Try shrink the item matrix.This will remove air item that does not participate in the craft.
         *
         *
         * Example:
         * x represents air item, o represents valid items
         *
         *
         * <pre>
         * [              [
         * [x,x,x],
         * [x,o,o], =>    [o,o],
         * [x,o,o]        [o,o]
         * ]              ]
        </pre> *
         *
         * @param input the input
         */
        fun tryShrinkMatrix(input: Input) {
            val inputs = input.getData()
            var startRow = 0
            var endRow = inputs!!.size - 1
            for (row in inputs!!.indices) {
                if (notAllEmptyRow(inputs!![row])) {
                    startRow = row
                    break
                }
                // 发现全部都是空气，直接返回空数组
                if (row == inputs!!.size - 1) {
                    input.setCol(0)
                    input.setRow(0)
                    input.setData(Input.Companion.EMPTY_INPUT_ARRAY)
                    return
                }
            }
            for (row in inputs!!.indices.reversed()) {
                if (notAllEmptyRow(inputs!![row])) {
                    endRow = row
                    break
                }
            }
            var startColumn = 0
            var endColumn = inputs!![0]!!.size - 1
            for (column in inputs!![0]!!.indices) {
                if (notAllEmptyColumn(inputs, column)) {
                    startColumn = column
                    break
                }
            }
            for (column in inputs!![0]!!.indices.reversed()) {
                if (notAllEmptyColumn(inputs, column)) {
                    endColumn = column
                    break
                }
            }

            if (startRow == 0 && endRow == inputs!!.size - 1 && startColumn == 0 && endColumn == inputs!![0]!!.size - 1) {
                input.setData(inputs)
                return
            }
            val newRow = endRow - startRow + 1
            val newCol = endColumn - startColumn + 1
            val result = Array(newRow) { arrayOfNulls<Item>(newCol) }
            for (row in startRow..endRow) {
                if (endColumn + 1 - startColumn >= 0) System.arraycopy(
                    inputs!![row], startColumn,
                    result[row - startRow], 0, endColumn + 1 - startColumn
                )
            }
            input.setRow(newRow)
            input.setCol(newCol)
            input.setData(result)
        }

        private fun notAllEmptyRow(inputs: Array<Item>): Boolean {
            for (item in inputs) {
                if (!item.isNothing) {
                    return true
                }
            }
            return false
        }

        private fun notAllEmptyColumn(inputs: Array<Array<Item?>>, column: Int): Boolean {
            for (row in inputs) {
                if (!row[column]!!.isNothing) {
                    return true
                }
            }
            return false
        }
    }
}
