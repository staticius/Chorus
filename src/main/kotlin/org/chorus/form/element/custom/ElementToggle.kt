package org.chorus.form.element.custom

import com.google.gson.JsonObject







@Accessors(chain = true, fluent = true)

class ElementToggle : ElementCustom {
    private val text: String? = null
    private val defaultValue = false

    @JvmOverloads
    constructor(text: String? = "") : this(text, false)

    override fun toJson(): JsonObject? {
        `object`.addProperty("type", "toggle")
        `object`.addProperty("text", this.text)
        `object`.addProperty("default", this.defaultValue)

        return this.`object`
    }
}
