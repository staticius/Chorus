package org.chorus.network.protocol.types

import cn.nukkit.utils.JSONUtils
import com.google.gson.JsonObject
import org.jetbrains.annotations.UnmodifiableView
import java.util.*

object TrimData {
    var trimPatterns: @UnmodifiableView MutableList<TrimPattern>? = null
    var trimMaterials: @UnmodifiableView MutableList<TrimMaterial>? = null

    init {
        try {
            TrimData::class.java.classLoader.getResourceAsStream("trim_data.json").use { stream ->
                val obj = JSONUtils.from(stream, JsonObject::class.java)
                val l1 = ArrayList<TrimPattern>()
                val l2 = ArrayList<TrimMaterial>()
                for (e in obj.getAsJsonArray("patterns").asList()) {
                    val asJsonObject = e.asJsonObject
                    l1.add(TrimPattern(asJsonObject["itemName"].asString, asJsonObject["patternId"].asString))
                }
                for (e in obj.getAsJsonArray("materials").asList()) {
                    val asJsonObject = e.asJsonObject
                    l2.add(
                        TrimMaterial(
                            asJsonObject["materialId"].asString,
                            asJsonObject["color"].asString,
                            asJsonObject["itemName"].asString
                        )
                    )
                }
                trimPatterns = Collections.unmodifiableList(l1)
                trimMaterials = Collections.unmodifiableList(l2)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
