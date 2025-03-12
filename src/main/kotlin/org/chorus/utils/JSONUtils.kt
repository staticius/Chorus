package org.chorus.utils


import com.dfsek.terra.lib.commons.lang3.BooleanUtils
import com.dfsek.terra.lib.commons.lang3.math.NumberUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.chorus.utils.exception.FormativeRuntimeException
import java.io.*
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.function.Consumer

/**
 * Gson Tool Class
 *
 *
 * Advantages:
 * <br></br>
 * When the data volume is less than 10000, there is an absolute advantage in speed
 * <br></br>
 * The API and annotation support are relatively comprehensive, supporting loose parsing
 * <br></br>
 * Supports a wide range of data sources (strings, objects, files, streams, readers)
 *
 * @author duanxinyuan | CoolLoong
 */
@UtilityClass
object JSONUtils {
    private val GSON: Gson
    private val PRETTY_GSON: Gson

    init {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
        gsonBuilder.disableHtmlEscaping() // 禁止将部分特殊字符转义为unicode编码
        registerTypeAdapter(gsonBuilder)
        GSON = gsonBuilder.create()

        gsonBuilder.setPrettyPrinting()
        PRETTY_GSON = gsonBuilder.create()
    }

    private fun registerTypeAdapter(gsonBuilder: GsonBuilder) {
        gsonBuilder.registerTypeAdapter(
            Short::class.javaPrimitiveType, NumberTypeAdapter(
                Short::class.javaPrimitiveType
            )
        )
        gsonBuilder.registerTypeAdapter(Short::class.java, NumberTypeAdapter(Short::class.java))
        gsonBuilder.registerTypeAdapter(
            Int::class.javaPrimitiveType, NumberTypeAdapter(
                Int::class.javaPrimitiveType
            )
        )
        gsonBuilder.registerTypeAdapter(Int::class.java, NumberTypeAdapter(Int::class.java))
        gsonBuilder.registerTypeAdapter(
            Long::class.javaPrimitiveType, NumberTypeAdapter(
                Long::class.javaPrimitiveType
            )
        )
        gsonBuilder.registerTypeAdapter(Long::class.java, NumberTypeAdapter(Long::class.java))
        gsonBuilder.registerTypeAdapter(
            Float::class.javaPrimitiveType, NumberTypeAdapter(
                Float::class.javaPrimitiveType
            )
        )
        gsonBuilder.registerTypeAdapter(Float::class.java, NumberTypeAdapter(Float::class.java))
        gsonBuilder.registerTypeAdapter(
            Double::class.javaPrimitiveType, NumberTypeAdapter(
                Double::class.javaPrimitiveType
            )
        )
        gsonBuilder.registerTypeAdapter(Double::class.java, NumberTypeAdapter(Double::class.java))
        gsonBuilder.registerTypeAdapter(
            BigDecimal::class.java, NumberTypeAdapter(
                BigDecimal::class.java
            )
        )
    }

    /**
     * JSON deserialization
     */
    fun <V> from(reader: Reader, type: Class<V>?): V {
        val jsonReader = JsonReader(Objects.requireNonNull(reader))
        return GSON.fromJson(jsonReader, type)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(reader: Reader, typeToken: TypeToken<V>?): V {
        val jsonReader = JsonReader(Objects.requireNonNull(reader))
        return GSON.fromJson(jsonReader, typeToken)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(inputStream: InputStream, type: Class<V>?): V {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        return GSON.fromJson(reader, type)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(inputStream: InputStream, typeToken: TypeToken<V>): V {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        return GSON.fromJson(reader, typeToken.type)
    }

    /**
     * JSON deserialization（List）
     */
    fun <V> fromList(inputStream: InputStream, type: Class<V>): List<V> {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        val typeToken = TypeToken.getParameterized(
            ArrayList::class.java, type
        ) as TypeToken<List<V>>
        return GSON.fromJson(reader, typeToken.type)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(file: File, type: Class<V>?): V {
        try {
            val reader = JsonReader(FileReader(file))
            return GSON.fromJson(reader, type)
        } catch (e: FileNotFoundException) {
            throw GsonException("gson from error, file path: {}, type: {}", file.path, type, e)
        }
    }

    /**
     * JSON deserialization
     */
    fun <V> from(file: File, typeToken: TypeToken<V>): V {
        try {
            val reader = JsonReader(FileReader(file))
            return GSON.fromJson(reader, typeToken.type)
        } catch (e: FileNotFoundException) {
            throw GsonException("gson from error, file path: {}, type: {}", file.path, typeToken.type, e)
        }
    }

    /**
     * JSON deserialization（List）
     */
    fun <V> fromList(file: File, type: Class<V>): List<V> {
        try {
            val reader = JsonReader(FileReader(file))
            val typeToken = TypeToken.getParameterized(
                ArrayList::class.java, type
            ) as TypeToken<List<V>>
            return GSON.fromJson(reader, typeToken.type)
        } catch (e: FileNotFoundException) {
            throw GsonException("gson from error, file path: {}, type: {}", file.path, type, e)
        }
    }

    /**
     * JSON deserialization
     */
    fun <V> from(json: String?, type: Class<V>?): V {
        return GSON.fromJson(json, type)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(json: String?, type: Type?): V {
        return GSON.fromJson(json, type)
    }

    /**
     * JSON deserialization
     */
    fun <V> from(json: String?, typeToken: TypeToken<V>): V {
        return GSON.fromJson(json, typeToken.type)
    }

    /**
     * read inputstream as [JsonObject]
     */
    fun fromAsJsonTree(inputStream: InputStream?, type: Class<*>?): JsonObject {
        return GSON.toJsonTree(from(inputStream!!, type)).asJsonObject
    }

    /**
     * JSON deserialization（List）
     */
    fun <V> fromList(json: String?, type: Class<V>): List<V>? {
        val typeToken = TypeToken.getParameterized(
            ArrayList::class.java, type
        ) as TypeToken<List<V>>
        return GSON.fromJson(json, typeToken.type)
    }

    /**
     * JSON deserialization（Map）
     */
    fun fromMap(json: String?): Map<String, Any> {
        return GSON.fromJson(json, object : TypeToken<HashMap<String?, Any?>?>() {
        }.type)
    }

    /**
     * Lenient JSON deserialization
     */
    fun <V> fromLenient(inputStream: InputStream, type: Class<V>?): V {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        reader.isLenient = true
        return GSON.fromJson(reader, type)
    }

    fun <V> fromLenient(inputStream: InputStream, type: TypeToken<V>?): V {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        reader.isLenient = true
        return GSON.fromJson(reader, type)
    }

    /**
     * Lenient JSON deserialization（List）
     */
    fun <V> fromListLenient(inputStream: InputStream, type: Class<V>): List<V> {
        val reader = JsonReader(InputStreamReader(Objects.requireNonNull(inputStream)))
        reader.isLenient = true
        val typeToken = TypeToken.getParameterized(
            ArrayList::class.java, type
        ) as TypeToken<List<V>>
        return GSON.fromJson(reader, typeToken.type)
    }

    /**
     * Lenient JSON deserialization
     */
    fun <V> fromLenient(file: File, type: Class<V>?): V {
        try {
            val reader = JsonReader(FileReader(file))
            reader.isLenient = true
            return GSON.fromJson(reader, type)
        } catch (e: FileNotFoundException) {
            throw GsonException("gson lenient from error, file path: {}, type: {}", file.path, type, e)
        }
    }

    /**
     * Lenient JSON deserialization（List）
     */
    fun <V> fromListLenient(file: File, type: Class<V>): List<V> {
        try {
            val reader = JsonReader(FileReader(file))
            reader.isLenient = true
            val typeToken = TypeToken.getParameterized(
                ArrayList::class.java, type
            ) as TypeToken<List<V>>
            return GSON.fromJson(reader, typeToken.type)
        } catch (e: FileNotFoundException) {
            throw GsonException("gson lenient from error, file path: {}, type: {}", file.path, type, e)
        }
    }

    /**
     * Lenient JSON deserialization
     */
    fun <V> fromLenient(json: String?, type: Class<V>?): V? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val reader = JsonReader(StringReader(json))
        reader.isLenient = true
        return GSON.fromJson(reader, type)
    }

    /**
     * Lenient JSON deserialization
     */
    fun <V> fromLenient(json: String?, type: Type?): V? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val reader = JsonReader(StringReader(json))
        reader.isLenient = true
        return GSON.fromJson(reader, type)
    }

    /**
     * Lenient JSON deserialization
     */
    fun <V> fromLenient(json: String?, typeToken: TypeToken<V>): V? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val reader = JsonReader(StringReader(json))
        reader.isLenient = true
        return GSON.fromJson(reader, typeToken.type)
    }

    /**
     * Lenient JSON deserialization (List)
     */
    fun <V> fromListLenient(json: String?, type: Class<V>): List<V>? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val reader = JsonReader(StringReader(json))
        reader.isLenient = true
        val typeToken = TypeToken.getParameterized(
            ArrayList::class.java, type
        ) as TypeToken<List<V>>
        return GSON.fromJson(reader, typeToken.type)
    }

    /**
     * Serialized to JSON
     */
    fun <V> to(list: List<V>?): String {
        return GSON.toJson(list)
    }

    /**
     * Serialized to JSON
     */
    fun <V> to(v: V): String {
        return GSON.toJson(v)
    }

    /**
     * Serialized to JSON of Pretty format
     */
    fun <V> toPretty(list: List<V>?): String {
        return PRETTY_GSON.toJson(list)
    }

    /**
     * Serialized to JSON of Pretty format
     */
    fun <V> toPretty(v: V): String {
        return PRETTY_GSON.toJson(v)
    }

    /**
     * Serialize as a file
     */
    fun <V> toFile(path: String, list: List<V>?) {
        try {
            JsonWriter(FileWriter(path, true)).use { jsonWriter ->
                GSON.toJson(list, object : TypeToken<List<V>?>() {
                }.type, jsonWriter)
                jsonWriter.flush()
            }
        } catch (e: Exception) {
            throw GsonException("gson to file error, path: {}, list: {}", path, list, e)
        }
    }

    /**
     * Serialize as a file
     *
     * @param path the file path
     * @param v    the type v
     */
    fun <V> toFile(path: String, v: V) {
        toFile(path, v, null)
    }

    /**
     * 序列化为JSON文件
     */
    fun <V> toFile(path: String, v: V, jsonWriterConfigurator: Consumer<JsonWriter?>?) {
        try {
            JsonWriter(FileWriter(path, true)).use { jsonWriter ->
                jsonWriterConfigurator?.accept(jsonWriter)
                GSON.toJson(v, v.javaClass, jsonWriter)
                jsonWriter.flush()
            }
        } catch (e: Exception) {
            throw GsonException("gson to file error, path: {}, obj: {}", path, v, e)
        }
    }

    /**
     * Get a string field from a JSON string
     *
     * @return String，default is null
     */
    fun getAsString(json: String, key: String?): String? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        var propertyValue: String
        val jsonByKey = getAsJsonObject(json, key)
        propertyValue = try {
            jsonByKey.asString
        } catch (e: Exception) {
            jsonByKey.toString()
        }
        return propertyValue
    }

    /**
     * Get int field from a JSON string
     *
     * @return int，default is 0
     */
    fun getAsInt(json: String, key: String?): Int {
        if (StringUtils.isEmpty(json)) {
            return 0
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asInt
        } catch (e: Exception) {
            throw GsonException("gson get int error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a long field from a JSON string
     *
     * @return long，default is 0
     */
    fun getAsLong(json: String, key: String?): Long {
        if (StringUtils.isEmpty(json)) {
            return 0L
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asLong
        } catch (e: Exception) {
            throw GsonException("gson get long error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a double field from a JSON string
     *
     * @return double，default is 0.0
     */
    fun getAsDouble(json: String, key: String?): Double {
        if (StringUtils.isEmpty(json)) {
            return 0.0
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asDouble
        } catch (e: Exception) {
            throw GsonException("gson get double error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a bigInteger field from a JSON string
     *
     * @return BigInteger，default 0.0
     */
    fun getAsBigInteger(json: String, key: String?): BigInteger {
        if (StringUtils.isEmpty(json)) {
            return BigInteger(0.00.toString())
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asBigInteger
        } catch (e: Exception) {
            throw GsonException("gson get big integer error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a bigDecimal field from a JSON string
     *
     * @return BigDecimal，default 0.0
     */
    fun getAsBigDecimal(json: String, key: String?): BigDecimal {
        if (StringUtils.isEmpty(json)) {
            return BigDecimal("0.0")
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asBigDecimal
        } catch (e: Exception) {
            throw GsonException("gson get big decimal error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a boolean field from a JSON string
     *
     * @return boolean, default is false
     */
    fun getAsBoolean(json: String, key: String?): Boolean {
        if (StringUtils.isEmpty(json)) {
            return false
        }
        val jsonByKey = getAsJsonObject(json, key) as JsonPrimitive
        try {
            if (jsonByKey.isBoolean) {
                return jsonByKey.asBoolean
            } else {
                if (jsonByKey.isString) {
                    val string = jsonByKey.asString
                    return if ("1" == string) {
                        true
                    } else {
                        BooleanUtils.toBoolean(string)
                    }
                } else { // number
                    return BooleanUtils.toBoolean(jsonByKey.asInt)
                }
            }
        } catch (e: Exception) {
            throw GsonException("gson get boolean error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get a byte field from a JSON string
     *
     * @return byte, default is 0
     */
    fun getAsByte(json: String, key: String?): Byte {
        if (StringUtils.isEmpty(json)) {
            return 0
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return jsonByKey.asByte
        } catch (e: Exception) {
            throw GsonException("gson get byte error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Get object field from a JSON string
     *
     * @return object, default null
     */
    fun <V> getAsObject(json: String, key: String?, type: Class<V>?): V? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            return from(jsonByKey.asString, type)
        } catch (e: Exception) {
            throw GsonException("gson get list error, json: {}, key: {}, type: {}", json, key, type, e)
        }
    }

    /**
     * Get a list field from a JSON string
     *
     * @return list, default null
     */
    fun <V> getAsList(json: String, key: String?, type: Class<V>): List<V>? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        val jsonByKey = getAsJsonObject(json, key)
        try {
            val jsonArray = jsonByKey.asJsonArray
            val typeToken = TypeToken.getParameterized(
                ArrayList::class.java, type
            ) as TypeToken<List<V>>
            return from(jsonArray.toString(), typeToken)
        } catch (e: Exception) {
            throw GsonException("gson get list error, json: {}, key: {}, type: {}", json, key, type, e)
        }
    }

    /**
     * Get a JsonElement field from a JSON string
     */
    fun getAsJsonObject(json: String, key: String?): JsonElement {
        try {
            val element = JsonParser.parseString(json)
            val jsonObj = element.asJsonObject
            return jsonObj[key]
        } catch (e: JsonSyntaxException) {
            throw GsonException("gson get object from json error, json: {}, key: {}", json, key, e)
        }
    }

    /**
     * Add element to the json
     */
    fun <V> add(json: String, key: String, value: V): String {
        val element = JsonParser.parseString(json)
        val jsonObject = element.asJsonObject
        add(jsonObject, key, value)
        return jsonObject.toString()
    }

    /**
     * Add element to the json
     */
    private fun <V> add(jsonObject: JsonObject, key: String, value: V) {
        if (value is String) {
            jsonObject.addProperty(key, value as String)
        } else if (value is Number) {
            jsonObject.addProperty(key, value as Number)
        } else {
            jsonObject.addProperty(key, to(value))
        }
    }

    /**
     * remove an element from the json string
     *
     * @return json
     */
    fun remove(json: String, key: String?): String {
        val element = JsonParser.parseString(json)
        val jsonObj = element.asJsonObject
        jsonObj.remove(key)
        return jsonObj.toString()
    }

    /**
     * update an element from the json string
     */
    fun <V> update(json: String, key: String, value: V): String {
        val element = JsonParser.parseString(json)
        val jsonObject = element.asJsonObject
        jsonObject.remove(key)
        add(jsonObject, key, value)
        return jsonObject.toString()
    }

    /**
     * Formatting Json (Beautifying)
     *
     * @return json
     */
    fun format(json: String): String {
        val jsonElement = JsonParser.parseString(json)
        return PRETTY_GSON.toJson(jsonElement)
    }

    /**
     * Determine whether the string is JSON
     *
     * @return json
     */
    fun isJson(json: String): Boolean {
        return try {
            JsonParser.parseString(json).isJsonObject
        } catch (e: Exception) {
            false
        }
    }

    /**
     * get data of map as a specific type value
     */
    fun <T> childAsType(data: Map<*, *>, key: String, asType: Class<T>): T {
        val value = data[key]
        check(asType.isInstance(value)) { "$key node is missing" }
        return value as T
    }

    private class NumberTypeAdapter<T>(private val c: Class<T>?) : TypeAdapter<Number?>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, number: Number?) {
            if (number != null) {
                jsonWriter.value(number)
            } else {
                jsonWriter.nullValue()
            }
        }

        override fun read(jsonReader: JsonReader): Number? {
            try {
                if (jsonReader.peek() == null) {
                    return null
                }
                val json = jsonReader.nextString()
                if (c == Short::class.javaPrimitiveType) {
                    return NumberUtils.toShort(json)
                } else if (c == Short::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return json.toShort()
                } else if (c == Int::class.javaPrimitiveType) {
                    return NumberUtils.toInt(json)
                } else if (c == Int::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return json.toInt()
                } else if (c == Long::class.javaPrimitiveType) {
                    return NumberUtils.toLong(json)
                } else if (c == Long::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return json.toLong()
                } else if (c == Float::class.javaPrimitiveType) {
                    return json.toFloat()
                } else if (c == Float::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return NumberUtils.toFloat(json)
                } else if (c == Double::class.javaPrimitiveType) {
                    return NumberUtils.toDouble(json)
                } else if (c == Double::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return json.toDouble()
                } else if (c == BigDecimal::class.java) {
                    if (StringUtils.isEmpty(json)) {
                        return null
                    }
                    return BigDecimal(json)
                } else {
                    return json.toInt()
                }
            } catch (e: Exception) {
                return null
            }
        }
    }


    class GsonException : FormativeRuntimeException {
        constructor() : super()

        constructor(message: String?) : super(message)

        constructor(cause: Throwable?) : super(cause)

        constructor(format: String?, vararg arguments: Any?) : super(format, *arguments)
    }
}