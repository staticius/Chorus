package org.chorus.entity.data.property

import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.*
import org.chorus.network.protocol.SyncEntityPropertyPacket
import java.io.IOException
import java.util.function.Consumer
import java.util.function.IntFunction

/**
 * @author Peng_Lx
 */
abstract class EntityProperty(private val identifier: String) {
    fun getIdentifier(): String {
        return identifier
    }

    abstract fun populateTag(tag: CompoundTag)

    companion object {
        private const val PLAYER_KEY: String = "minecraft:player"
        private const val PROPERTIES_KEY: String = "properties"

        private val entityPropertyMap: MutableMap<String, MutableList<EntityProperty>> = HashMap()
        private val nbtCache: MutableList<CompoundTag> = ArrayList()
        private var playerPropertyCache: CompoundTag = CompoundTag()

        @JvmStatic
        fun init() {
            try {
                EntityProperty::class.java.getClassLoader().getResourceAsStream("entity_properties.nbt").use { stream ->
                    val root: CompoundTag = NBTIO.readCompressed(stream)
                    root.getTags().values.forEach(Consumer<Tag> { uncast: Tag ->
                        if (uncast is CompoundTag) {
                            val properties: ListTag<CompoundTag> = uncast.getList("properties", CompoundTag::class.java)
                            for (property: CompoundTag in properties.getAll()) {
                                val name: String = property.getString("name")
                                val type: Int = property.getInt("type")
                                val data: EntityProperty = when (type) {
                                    0 -> IntEntityProperty(
                                        name,
                                        property.getInt("min"),
                                        property.getInt("max"),
                                        property.getInt("min")
                                    )

                                    1 -> FloatEntityProperty(
                                        name,
                                        property.getInt("min").toFloat(),
                                        property.getInt("max").toFloat(),
                                        property.getInt("min").toFloat()
                                    )

                                    2 -> BooleanEntityProperty(name, false)
                                    3 -> {
                                        val enums: MutableList<String> = ArrayList()
                                        for (entry: StringTag in property.getList("enum", StringTag::class.java)
                                            .getAll()) enums.add(entry.data)
                                        EnumEntityProperty(
                                            name,
                                            enums.toArray<String>(IntFunction<Array<String>> { _Dummy_.__Array__() }),
                                            enums.first()
                                        )
                                    }

                                    else -> throw IllegalArgumentException("Unknown EntityProperty type " + type)
                                }
                                register(uncast.getString("type"), data)
                            }
                        }
                    })
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        fun register(entityIdentifier: String, property: EntityProperty): Boolean {
            val entityProperties: MutableList<EntityProperty> =
                entityPropertyMap.getOrDefault(entityIdentifier, ArrayList())
            for (entityProperty: EntityProperty in entityProperties) {
                if (entityProperty.identifier == property.identifier) return false
            }
            entityProperties.add(property)
            entityPropertyMap.put(entityIdentifier, entityProperties)
            return true
        }

        @JvmStatic
        fun buildPacketData() {
            for (entry: Map.Entry<String, List<EntityProperty>> in entityPropertyMap.entries) {
                val listProperty: ListTag<CompoundTag> = buildPropertyList(entry.value)
                val tag: CompoundTag = CompoundTag().putList(PROPERTIES_KEY, listProperty).putString("type", entry.key)
                nbtCache.add(tag)
            }
        }

        @JvmStatic
        fun buildPlayerProperty() {
            val properties: List<EntityProperty>? = entityPropertyMap.get(PLAYER_KEY)
            if (properties == null) {
                playerPropertyCache = CompoundTag()
                return
            }
            val listProperty: ListTag<CompoundTag> = buildPropertyList(properties)
            playerPropertyCache = CompoundTag().putList(PROPERTIES_KEY, listProperty).putString("type", PLAYER_KEY)
        }

        @JvmStatic
        fun getPacketCache(): List<SyncEntityPropertyPacket> {
            return nbtCache.stream().map { data: CompoundTag? -> SyncEntityPropertyPacket(data) }.toList()
        }

        @JvmStatic
        fun getPlayerPropertyCache(): CompoundTag {
            return playerPropertyCache
        }

        fun getEntityProperty(identifier: String): List<EntityProperty> {
            return entityPropertyMap.getOrDefault(identifier, ArrayList())
        }

        private fun buildPropertyList(properties: List<EntityProperty>): ListTag<CompoundTag> {
            val listProperty: ListTag<CompoundTag> = ListTag()
            for (entityProperty: EntityProperty in properties) {
                val propertyTag: CompoundTag = CompoundTag()
                propertyTag.putString("name", entityProperty.getIdentifier())
                entityProperty.populateTag(propertyTag)
                listProperty.add(propertyTag)
            }
            return listProperty
        }
    }
}
