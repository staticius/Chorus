package org.chorus.nbt.tag

import java.util.*


abstract class NumberTag<T : Number?> : Tag() {
    abstract var data: T

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), data)
    }
}
