package cn.nukkit.form.element.simple

import com.google.gson.JsonObject
import lombok.*
import lombok.experimental.Accessors

/**
 * The image of a [ElementButton]
 */
@Getter
@Accessors(chain = true, fluent = true)
class ButtonImage(type: Type, data: String?) {
    protected val `object`: JsonObject = JsonObject()

    protected var type: Type? = null
    protected var data: String? = null

    init {
        this.type(type)
        this.data(data)
    }

    fun type(type: Type): ButtonImage {
        this.type = type
        `object`.addProperty("type", type.name().toLowerCase())
        return this
    }

    fun data(path: String?): ButtonImage {
        this.data = path
        `object`.addProperty("data", path)
        return this
    }

    /**
     * There are two types of images:
     * - PATH (image located inside a resource pack)
     * - URL (image accessed via the internet)
     */
    enum class Type {
        PATH,
        URL;

        fun of(path: String?): ButtonImage {
            return ButtonImage(this, path)
        }
    }

    fun toJson(): JsonObject {
        return this.`object`
    }
}
