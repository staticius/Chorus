package cn.nukkit.level.generator.terra.mappings

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import lombok.Getter
import lombok.Setter
import kotlin.collections.MutableMap
import kotlin.collections.set

@Getter
@Setter
class JeBlockState(str: String) {
    private val identifier: String
    private val attributes: MutableMap<String, String> = Object2ObjectArrayMap(1)
    private val equalsIgnoreAttributes = false
    private val equalsIgnoreWaterlogged = false

    init {
        val strings: Array<String> = str.replace("[", ",").replace("]", ",").replace(" ", "").split(",")
        identifier = strings[0]
        if (strings.size > 1) {
            for (i in 1..<strings.size) {
                val tmp = strings[i]
                val index = tmp.indexOf("=")
                attributes[tmp.substring(0, index)] = tmp.substring(index + 1)
            }
        }
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is JeBlockState) {
            if (equalsIgnoreAttributes || obj.equalsIgnoreAttributes) {
                if (obj.identifier == identifier) return true
            }
            if (equalsIgnoreWaterlogged || obj.equalsIgnoreWaterlogged) {
                val m1 = Object2ObjectArrayMap(attributes)
                val m2 = Object2ObjectArrayMap(obj.attributes)
                m1.remove("waterlogged")
                m2.remove("waterlogged")
                if (obj.identifier == identifier && m1 == m2) return true
            }
            return obj.identifier == identifier && attributes == obj.attributes
        }
        return false
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }

    override fun toString(): String {
        val ret = StringBuilder(identifier).append(";")
        attributes.forEach { k: String?, v: String? -> ret.append(k).append("=").append(v).append(";") }
        return ret.toString()
    }
}
