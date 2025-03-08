package cn.nukkit.form.element.custom

import com.google.gson.JsonObject
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
class ElementInput : ElementCustom {
    private val text: String? = null
    private val placeholder: String? = null
    private val defaultText: String? = null

    @JvmOverloads
    constructor(text: String? = "", placeholder: String? = "") : this(text, placeholder, "")

    override fun toJson(): JsonObject? {
        `object`.addProperty("type", "input")
        `object`.addProperty("text", this.text)
        `object`.addProperty("placeholder", this.placeholder)
        `object`.addProperty("default", this.defaultText)

        return this.`object`
    }
}
