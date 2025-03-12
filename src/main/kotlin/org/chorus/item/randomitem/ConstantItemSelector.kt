package org.chorus.item.randomitem

import org.chorus.entity.effect.PotionType.Companion.get
import org.chorus.item.*

/**
 * @author Snake1999
 * @since 2016/1/15
 */
open class ConstantItemSelector(val item: Item, parent: Selector?) : Selector(parent) {
    constructor(id: String?, parent: Selector?) : this(id, 0, parent)

    constructor(id: String?, meta: Int, parent: Selector?) : this(id, meta, 1, parent)

    constructor(id: String?, meta: Int, count: Int, parent: Selector?) : this(get(id, meta, count), parent)

    override fun select(): Any {
        return item
    }
}
