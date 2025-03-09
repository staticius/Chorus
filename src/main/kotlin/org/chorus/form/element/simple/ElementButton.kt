package org.chorus.form.element.simple

import org.chorus.form.element.Element
import com.google.gson.JsonObject
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors

/**
 * The button object used to add buttons within [cn.nukkit.form.window.SimpleForm]
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
class ElementButton : Element {
    /**
     * The button text shown
     */
    var text: String? = null

    /**
     * An optional image to send with the button
     */
    var image: ButtonImage? = null

    @JvmOverloads
    constructor(text: String? = "") : this(text, null)

    override fun toJson(): JsonObject? {
        `object`.addProperty("text", this.text)

        if (this.image != null) {
            `object`.add("image", image!!.toJson())
        }

        return this.`object`
    }

    companion object {
        var EMPTY_LIST: Array<ElementButton?> = arrayOfNulls(0)
    }
}
