package cn.nukkit.command.data

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.Data

@Data
class ChainedSubCommandData {
    private val name: String? = null
    private val values: List<Value> = ObjectArrayList()

    @Data
    class Value {
        private val first: String? = null
        private val second: String? = null
    }
}
