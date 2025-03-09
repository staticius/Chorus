package org.chorus.command.data

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.Data


class ChainedSubCommandData {
    private val name: String? = null
    private val values: List<Value> = ObjectArrayList()


    class Value {
        private val first: String? = null
        private val second: String? = null
    }
}
