package org.chorus.form.element.custom

import com.google.gson.JsonObject







@Accessors(chain = true, fluent = true)

class ElementLabel : ElementCustom {
    private val text: String? = null

    constructor() : this("")

    override fun toJson(): JsonObject? {
        `object`.addProperty("type", "label")
        `object`.addProperty("text", this.text)

        return this.`object`
    }
}
