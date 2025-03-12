package org.chorus.utils

import org.chorus.plugin.Plugin
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

abstract class SimpleConfig(file: File) {
    companion object : Loggable

    private val configFile = file

    @JvmOverloads
    constructor(
        plugin: Plugin,
        fileName: String = "config.yml"
    ) : this(File(plugin.dataFolder!!.path + File.separator + fileName))

    init {
        configFile.parentFile.mkdirs()
    }

    @JvmOverloads
    fun save(async: Boolean = false): Boolean {
        if (configFile.exists()) try {
            configFile.createNewFile()
        } catch (e: Exception) {
            return false
        }
        val cfg = Config(configFile, Config.Companion.YAML)
        for (field in javaClass.declaredFields) {
            if (skipSave(field)) continue
            val path = getPath(field)
            try {
                if (path != null) cfg[path] = field[this]
            } catch (e: Exception) {
                return false
            }
        }
        cfg.save(async)
        return true
    }

    fun load(): Boolean {
        if (!configFile.exists()) return false
        val cfg = Config(configFile, Config.Companion.YAML)
        for (field in javaClass.declaredFields) {
            if (field.name == "configFile") continue
            if (skipSave(field)) continue
            val path = getPath(field) ?: continue
            if (path.isEmpty()) continue
            field.isAccessible = true
            try {
                if (field.type == Int::class.javaPrimitiveType || field.type == Int::class.java) field[this] =
                    cfg.getInt(path, field.getInt(this))
                else if (field.type == Boolean::class.javaPrimitiveType || field.type == Boolean::class.java) field[this] =
                    cfg.getBoolean(path, field.getBoolean(this))
                else if (field.type == Long::class.javaPrimitiveType || field.type == Long::class.java) field[this] =
                    cfg.getLong(path, field.getLong(this))
                else if (field.type == Double::class.javaPrimitiveType || field.type == Double::class.java) field[this] =
                    cfg.getDouble(path, field.getDouble(this))
                else if (field.type == String::class.java) field[this] =
                    cfg.getString(path, field[this] as String)
                else if (field.type == ConfigSection::class.java) field[this] = cfg.getSection(path)
                else if (field.type == MutableList::class.java) {
                    val genericFieldType = field.genericType
                    if (genericFieldType is ParameterizedType) {
                        val fieldArgClass = genericFieldType.actualTypeArguments[0] as Class<*>
                        when (fieldArgClass) {
                            Int::class.java -> field[this] = cfg.getIntegerList(path)
                            Boolean::class.java -> field[this] = cfg.getBooleanList(path)
                            Long::class.java -> field[this] = cfg.getLongList(path)
                            Double::class.java -> field[this] = cfg.getDoubleList(path)
                            String::class.java -> field[this] = cfg.getStringList(path)
                            Char::class.java -> field[this] = cfg.getCharacterList(path)
                            Byte::class.java -> field[this] = cfg.getByteList(path)
                            Float::class.java -> field[this] = cfg.getFloatList(path)
                            Short::class.java -> field[this] = cfg.getShortList(path)
                        }
                    } else field[this] = cfg.getList(path) // Hell knows what's kind of List was found :)
                } else throw IllegalStateException("SimpleConfig did not supports class: " + field.type.name + " for config field " + configFile.name)
            } catch (e: Exception) {
                SimpleConfig.log.error("An error occurred while loading the config {}", configFile, e)
                return false
            }
        }
        return true
    }

    private fun getPath(field: Field): String? {
        var path: String? = null
        if (field.isAnnotationPresent(Path::class.java)) {
            val pathDefine = field.getAnnotation(
                Path::class.java
            )
            path = pathDefine.value
        }
        if (path.isNullOrEmpty()) path = field.name.replace("_".toRegex(), ".")
        if (Modifier.isFinal(field.modifiers)) return null
        if (Modifier.isPrivate(field.modifiers)) field.isAccessible = true
        return path
    }

    private fun skipSave(field: Field): Boolean {
        if (!field.isAnnotationPresent(Skip::class.java)) return false
        return field.getAnnotation(Skip::class.java).skipSave
    }

    private fun skipLoad(field: Field): Boolean {
        if (!field.isAnnotationPresent(Skip::class.java)) return false
        return field.getAnnotation(Skip::class.java).skipLoad
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class Path(val value: String = "")

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class Skip(val skipSave: Boolean = true, val skipLoad: Boolean = true)
}
