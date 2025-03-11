package org.chorus.form.element.custom

import com.google.gson.JsonObject







@Accessors(chain = true, fluent = true)

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
