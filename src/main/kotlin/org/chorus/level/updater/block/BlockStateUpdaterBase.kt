package org.chorus.level.updater.block


import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.chorus.level.updater.Updater
import org.chorus.level.updater.util.tagupdater.CompoundTagEditHelper
import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext
import org.chorus.utils.JSONUtils
import java.io.IOException
import kotlin.collections.set

class BlockStateUpdaterBase : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        context.addUpdater(0, 0, 0)
            .regex("name", "minecraft:.+")
            .regex("val", "[0-9]+")
            .addCompound("states")
            .tryEdit("states") { helper: CompoundTagEditHelper ->
                val tag = helper.compoundTag
                val parent = helper.parent
                val id = parent!!["name"] as String?
                var `val` = parent["val"] as Short
                val statesArray = LEGACY_BLOCK_DATA_MAP[id]
                if (statesArray != null) {
                    if (`val` >= statesArray.size) `val` = 0
                    tag.putAll(statesArray[`val`.toInt()])
                }
            }
            .remove("val")
    }

    companion object {
        @JvmField
        val INSTANCE: Updater = BlockStateUpdaterBase()

        val LEGACY_BLOCK_DATA_MAP: MutableMap<String, Array<Map<String, Any?>>> = HashMap()

        init {
            val node: JsonObject
            try {
                Updater::class.java.classLoader.getResourceAsStream("legacy_block_data_map.json").use { stream ->
                    checkNotNull(stream)
                    node = JSONUtils.fromAsJsonTree(stream, MutableMap::class.java)
                }
            } catch (e: IOException) {
                throw AssertionError("Error loading legacy block data map", e)
            }

            for (entry in node.entrySet()) {
                val name: String = entry.key
                val stateNodes: JsonArray = entry.value.getAsJsonArray()

                val states = stateNodes.map { convertStateToCompound(it.asJsonObject) }.toTypedArray()

                LEGACY_BLOCK_DATA_MAP[name] = states
            }
        }

        private fun convertStateToCompound(node: JsonObject): Map<String, Any?> {
            val tag: MutableMap<String, Any?> = LinkedHashMap()
            val iterator: Iterator<Map.Entry<String, JsonElement>> = node.entrySet().iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val name: String = entry.key
                val value: JsonElement = entry.value
                if (value.isJsonPrimitive) {
                    val primitive: JsonPrimitive = entry.value.getAsJsonPrimitive()
                    if (primitive.isBoolean) {
                        tag[name] = primitive.asBoolean
                    } else if (primitive.isNumber) {
                        tag[name] = primitive.asInt
                    } else if (primitive.isString) {
                        tag[name] = primitive.asString
                    } else {
                        throw UnsupportedOperationException("Invalid state type")
                    }
                } else throw UnsupportedOperationException("Invalid state type")
            }
            return tag
        }
    }
}
