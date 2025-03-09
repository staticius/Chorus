package org.chorus.utils

import java.util.function.Consumer

/**
 * @author fromgate
 * @since 26.04.2016
 */
class ConfigSection
/**
 * Empty ConfigSection constructor
 */
    () : LinkedHashMap<String?, Any?>() {
    /**
     * Constructor of ConfigSection that contains initial key/value data
     *
     * @param key
     * @param value
     */
    constructor(key: String, value: Any?) : this() {
        this.set(key, value)
    }

    /**
     * Constructor of ConfigSection, based on values stored in map.
     *
     * @param map
     */
    constructor(map: LinkedHashMap<String?, Any?>?) : this() {
        if (map.isNullOrEmpty()) return
        for ((key, value) in map) {
            when (value) {
                is LinkedHashMap<*, *> -> {
                    val safeMap = value.entries
                        .filter { it.key is String? }
                        .associate { it.key as String? to it.value }
                        .let { LinkedHashMap(it) }
                    super.put(key, ConfigSection(safeMap))
                }
                is Map<*, *> -> {
                    val safeMap = value.entries
                        .filter { it.key is String? }
                        .associate { it.key as String? to it.value }
                    super.put(key, ConfigSection(safeMap))
                }
                is List<*> -> super.put(key, parseList(value))
                else -> super.put(key, value)
            }
        }
    }

    constructor(map: Map<String?, Any?>?) : this() {
        if (map.isNullOrEmpty()) return
        for ((key, value) in map) {
            when (value) {
                is LinkedHashMap<*, *> -> {
                    val safeMap = value.entries
                        .filter { it.key is String? }
                        .associate { it.key as String? to it.value }
                        .let { LinkedHashMap(it) }
                    super.put(key, ConfigSection(safeMap))
                }
                is Map<*, *> -> {
                    val safeMap = value.entries
                        .filter { it.key is String? }
                        .associate { it.key as String? to it.value }
                    super.put(key, ConfigSection(safeMap))
                }
                is List<*> -> super.put(key, parseList(value))
                else -> super.put(key, value)
            }
        }
    }

    private fun parseList(list: List<*>): List<*> {
        val newList: MutableList<Any> = ArrayList()

        for (o in list) {
            if (o == null) continue
            if (o is LinkedHashMap<*, *>) {
                newList.add(ConfigSection(o as LinkedHashMap<String?, Any?>))
            } else {
                newList.add(o)
            }
        }

        return newList
    }

    val allMap: Map<String?, Any?>
        /**
         * Get root section as LinkedHashMap
         *
         * @return
         */
        get() = LinkedHashMap(this)

    val all: ConfigSection
        /**
         * Get new instance of config section
         *
         * @return
         */
        get() = ConfigSection(this)

    /**
     * Get object by key. If section does not contain value, return null
     */
    override fun get(key: String?): Any? {
        return this.get<Any?>(key, null)
    }

    /**
     * Get object by key. If section does not contain value, return default value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    fun <T> get(key: String?, defaultValue: T?): T? {
        if (key.isNullOrEmpty()) return defaultValue
        if (super.containsKey(key)) {
            val value = super.get(key)
            if (defaultValue != null && !defaultValue.javaClass.isInstance(value)) {
                if (value is Map<*, *> && defaultValue is ConfigSection) {
                    val safeMap = value.entries
                        .filter { it.key is String? }
                        .associate { it.key as String? to it.value }
                    return ConfigSection(safeMap) as T
                }
            }
            return value as T?
        }
        val keys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
        if (!super.containsKey(keys[0])) return defaultValue
        val value = super.get(keys[0])
        if (value is ConfigSection) {
            return value.get<T?>(keys[1], defaultValue)
        } else if (value is Map<*, *>) {
            val safeMap = value.entries
                .filter { it.key is String? }
                .associate { it.key as String? to it.value }
            val section = ConfigSection(safeMap)
            return section.get<T?>(keys[1], defaultValue)
        }
        return defaultValue
    }

    /**
     * Store value into config section
     *
     * @param key
     * @param value
     */
    fun set(key: String, value: Any?) {
        val subKeys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
        if (subKeys.size > 1) {
            var childSection: ConfigSection? = ConfigSection()
            if (this.containsKey(subKeys[0]) && super.get(subKeys[0]) is ConfigSection) childSection = super.get(
                subKeys[0]
            ) as ConfigSection?
            childSection!!.set(subKeys[1], value)
            super.put(subKeys[0], childSection)
        } else super.put(subKeys[0], value)
    }

    /**
     * Check type of section element defined by key. Return true this element is ConfigSection
     *
     * @param key
     * @return
     */
    fun isSection(key: String?): Boolean {
        val value = this[key]
        return value is ConfigSection
    }

    /**
     * Get config section element defined by key
     *
     * @param key
     * @return
     */
    fun getSection(key: String?): ConfigSection {
        return get(key, ConfigSection())!!
    }

    val sections: ConfigSection
        /**
         * Get all ConfigSections in root path.
         * Example config:
         * a1:
         * b1:
         * c1:
         * c2:
         * a2:
         * b2:
         * c3:
         * c4:
         * a3: true
         * a4: "hello"
         * a5: 100
         *
         *
         * getSections() will return new ConfigSection, that contains sections a1 and a2 only.
         *
         * @return
         */
        get() = getSections(null)

    /**
     * Get sections (and only sections) from provided path
     *
     * @param key - config section path, if null or empty root path will used.
     * @return
     */
    fun getSections(key: String?): ConfigSection {
        val sections = ConfigSection()
        val parent = if (key == null || key.isEmpty()) this.all else getSection(key)
        if (parent == null) return sections
        parent.forEach { (key1: String?, value: Any?) ->
            if (value is ConfigSection) sections[key1] = value
        }
        return sections
    }

    /**
     * Get int value of config section element
     *
     * @param key - key (inside) current section (default value equals to 0)
     * @return
     */
    fun getInt(key: String?): Int {
        return this.getInt(key, 0)
    }

    /**
     * Get int value of config section element
     *
     * @param key          - key (inside) current section
     * @param defaultValue - default value that will returned if section element is not exists
     * @return
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return get(key, (defaultValue as Number))!!.toInt()
    }

    /**
     * Check type of section element defined by key. Return true this element is Integer
     *
     * @param key
     * @return
     */
    fun isInt(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Int
    }

    /**
     * Get long value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getLong(key: String?): Long {
        return this.getLong(key, 0)
    }

    /**
     * Get long value of config section element
     *
     * @param key          - key (inside) current section
     * @param defaultValue - default value that will returned if section element is not exists
     * @return
     */
    fun getLong(key: String?, defaultValue: Long): Long {
        return get(key, (defaultValue as Number))!!.toLong()
    }

    /**
     * Check type of section element defined by key. Return true this element is Long
     *
     * @param key
     * @return
     */
    fun isLong(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Long
    }

    /**
     * Get double value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getDouble(key: String?): Double {
        return this.getDouble(key, 0.0)
    }

    /**
     * Get double value of config section element
     *
     * @param key          - key (inside) current section
     * @param defaultValue - default value that will returned if section element is not exists
     * @return
     */
    fun getDouble(key: String?, defaultValue: Double): Double {
        return get(key, (defaultValue as Number))!!.toDouble()
    }

    /**
     * Check type of section element defined by key. Return true this element is Double
     *
     * @param key
     * @return
     */
    fun isDouble(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Double
    }

    /**
     * Get String value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getString(key: String?): String {
        return this.getString(key, "")
    }

    /**
     * Get String value of config section element
     *
     * @param key          - key (inside) current section
     * @param defaultValue - default value that will returned if section element is not exists
     * @return
     */
    fun getString(key: String?, defaultValue: String): String {
        val result: Any = get(key, defaultValue)!!
        return result.toString()
    }

    /**
     * Check type of section element defined by key. Return true this element is String
     *
     * @param key
     * @return
     */
    fun isString(key: String?): Boolean {
        val `val` = get(key)
        return `val` is String
    }

    /**
     * Get boolean value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getBoolean(key: String?): Boolean {
        return this.getBoolean(key, false)
    }

    /**
     * Get boolean value of config section element
     *
     * @param key          - key (inside) current section
     * @param defaultValue - default value that will returned if section element is not exists
     * @return
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return get(key, defaultValue)!!
    }

    /**
     * Check type of section element defined by key. Return true this element is Integer
     *
     * @param key
     * @return
     */
    fun isBoolean(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Boolean
    }

    /**
     * Get List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getList(key: String?): List<*> {
        return this.getList(key, null)
    }

    /**
     * Get List value of config section element
     *
     * @param key         - key (inside) current section
     * @param defaultList - default value that will returned if section element is not exists
     * @return
     */
    fun getList(key: String?, defaultList: List<*>?): List<*> {
        return get(key, defaultList)!!
    }

    /**
     * Check type of section element defined by key. Return true this element is List
     *
     * @param key
     * @return
     */
    fun isList(key: String?): Boolean {
        val `val` = get(key)
        return `val` is List<*>
    }

    /**
     * Get String List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getStringList(key: String?): List<String> {
        val value = this.getList(key)
            ?: return ArrayList(0)
        val result: MutableList<String> = ArrayList()
        for (o in value) {
            if (o is String || o is Number || o is Boolean || o is Char) {
                result.add(o.toString())
            }
        }
        return result
    }

    /**
     * Get Integer List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getIntegerList(key: String?): List<Int> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Int> = ArrayList()

        for (`object` in list) {
            if (`object` is Int) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toInt())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code)
            } else if (`object` is Number) {
                result.add(`object`.toInt())
            }
        }
        return result
    }

    /**
     * Get Boolean List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getBooleanList(key: String?): List<Boolean> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Boolean> = ArrayList()
        for (`object` in list) {
            if (`object` is Boolean) {
                result.add(`object`)
            } else if (`object` is String) {
                if (java.lang.Boolean.TRUE.toString() == `object`) {
                    result.add(true)
                } else if (java.lang.Boolean.FALSE.toString() == `object`) {
                    result.add(false)
                }
            }
        }
        return result
    }

    /**
     * Get Double List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getDoubleList(key: String?): List<Double> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Double> = ArrayList()
        for (`object` in list) {
            if (`object` is Double) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toDouble())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toDouble())
            } else if (`object` is Number) {
                result.add(`object`.toDouble())
            }
        }
        return result
    }

    /**
     * Get Float List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getFloatList(key: String?): List<Float> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Float> = ArrayList()
        for (`object` in list) {
            if (`object` is Float) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toFloat())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toFloat())
            } else if (`object` is Number) {
                result.add(`object`.toFloat())
            }
        }
        return result
    }

    /**
     * Get Long List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getLongList(key: String?): List<Long> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Long> = ArrayList()
        for (`object` in list) {
            if (`object` is Long) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toLong())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toLong())
            } else if (`object` is Number) {
                result.add(`object`.toLong())
            }
        }
        return result
    }

    /**
     * Get Byte List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getByteList(key: String?): List<Byte> {
        val list = getList(key) ?: return ArrayList(0)

        val result: MutableList<Byte> = ArrayList()

        for (`object` in list) {
            if (`object` is Byte) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toByte())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toByte())
            } else if (`object` is Number) {
                result.add(`object`.toByte())
            }
        }

        return result
    }

    /**
     * Get Character List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getCharacterList(key: String?): List<Char> {
        val list = getList(key) ?: return ArrayList(0)

        val result: MutableList<Char> = ArrayList()

        for (`object` in list) {
            if (`object` is Char) {
                result.add(`object`)
            } else if (`object` is String) {
                val str = `object`

                if (str.length == 1) {
                    result.add(str[0])
                }
            } else if (`object` is Number) {
                result.add(`object`.toInt().toChar())
            }
        }

        return result
    }

    /**
     * Get Short List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getShortList(key: String?): List<Short> {
        val list = getList(key) ?: return ArrayList(0)

        val result: MutableList<Short> = ArrayList()

        for (`object` in list) {
            if (`object` is Short) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toShort())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toShort())
            } else if (`object` is Number) {
                result.add(`object`.toShort())
            }
        }

        return result
    }

    /**
     * Get Map List value of config section element
     *
     * @param key - key (inside) current section
     * @return
     */
    fun getMapList(key: String?): List<Map<*, *>> {
        val list = getList(key) as List<Map<*, *>>
        val result: MutableList<Map<*, *>> = ArrayList()

        if (list == null) {
            return result
        }

        for (`object` in list) {
            if (`object` is Map<*, *>) {
                result.add(`object`)
            }
        }

        return result
    }

    /**
     * Check existence of config section element
     *
     * @param key
     * @param ignoreCase
     * @return
     */
    /**
     * Check existence of config section element
     *
     * @param key
     * @return
     */
    @JvmOverloads
    fun exists(key: String, ignoreCase: Boolean = false): Boolean {
        var key = key
        if (ignoreCase) key = key.lowercase()
        for (existKey in this.getKeys(true)) {
            var existKey = existKey
            if (ignoreCase) existKey = existKey.lowercase()
            if (existKey == key) return true
        }
        return false
    }

    /**
     * Remove config section element
     *
     * @param key
     */
    override fun remove(key: String?) {
        if (key == null || key.isEmpty()) return
        if (super.containsKey(key)) super.remove(key)
        else if (this.containsKey(".")) {
            val keys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
            if (super.get(keys[0]) is ConfigSection) {
                val section = super.get(keys[0]) as ConfigSection?
                section!!.remove(keys[1])
            }
        }
    }

    /**
     * Get all keys
     *
     * @param child - true = include child keys
     * @return
     */
    fun getKeys(child: Boolean): Set<String> {
        val keys: MutableSet<String> = LinkedHashSet()
        this.forEach { (key: String?, value: Any?) ->
            keys.add(key!!)
            if (value is ConfigSection) {
                if (child) value.getKeys(true).forEach(Consumer { childKey: String -> keys.add("$key.$childKey") })
            }
        }
        return keys
    }

    fun getKeys(): Set<String> {
        return this.getKeys()
    }
}
