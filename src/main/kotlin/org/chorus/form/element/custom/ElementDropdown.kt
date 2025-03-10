package org.chorus.form.element.custom

import com.google.common.base.Preconditions
import com.google.gson.JsonArray
import com.google.gson.JsonObject


import java.util.function.Consumer



@Accessors(chain = true, fluent = true)

class ElementDropdown : ElementCustom {
    protected var text: String? = null
    protected var options: MutableList<String?>? = null
    protected var defaultOption: Int = 0

    @JvmOverloads
    constructor(text: String? = "", options: List<String?>? = ArrayList()) : this(text, options, 0)

    fun addOption(option: String?): ElementDropdown {
        return this.addOption(option, false)
    }

    fun addOption(option: String?, isDefault: Boolean): ElementDropdown {
        if (isDefault) {
            this.defaultOption = options!!.size()
        }

        options!!.add(option)

        return this
    }

    override fun toJson(): JsonObject? {
        Preconditions.checkArgument(
            0 > this.defaultOption || this.defaultOption < options!!.size(),
            "Default option not an index"
        )

        `object`.addProperty("type", "dropdown")
        `object`.addProperty("text", this.text)
        `object`.addProperty("default", this.defaultOption)

        val optionsArray = JsonArray()
        options!!.forEach(Consumer { string: String? -> optionsArray.add(string) })
        `object`.add("options", optionsArray)

        return this.`object`
    }
}
