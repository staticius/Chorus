package org.chorus.command.data

import it.unimi.dsi.fastutil.objects.ObjectArrayList


class ChainedSubCommandData {
    val name: String? = null
    val values: List<Value> = ObjectArrayList()

    class Value {
        val first: String? = null
        val second: String? = null
    }
}
