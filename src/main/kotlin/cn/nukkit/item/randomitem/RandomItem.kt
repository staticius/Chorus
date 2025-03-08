package cn.nukkit.item.randomitem

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.set

/**
 * @author Snake1999
 * @since 2016/1/15
 */
object RandomItem {
    private val selectors: MutableMap<Selector, Float> = HashMap()

    val ROOT: Selector = Selector(null)

    @JvmOverloads
    fun putSelector(selector: Selector, chance: Float = 1f): Selector {
        if (selector.parent == null) selector.setParent(ROOT)
        selectors[selector] = chance
        return selector
    }

    fun selectFrom(selector: Selector?): Any? {
        Objects.requireNonNull(selector)
        val child: MutableMap<Selector, Float> = HashMap()
        selectors.forEach { (s: Selector?, f: Float?) ->
            if (s.parent === selector) child[s] =
                f
        }
        if (child.size == 0) return selector!!.select()
        return selectFrom(Selector.Companion.selectRandom(child))
    }
}
