package org.chorus.form.element.custom

import com.google.common.base.Preconditions
import com.google.gson.JsonObject
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors



@Accessors(chain = true, fluent = true)

class ElementSlider : ElementCustom {
    private val text: String? = null
    private val min = 0f
    private val max = 0f
    private val step = 0
    private val defaultValue = 0f

    @JvmOverloads
    constructor(text: String? = "", min: Float = 1f, max: Float = Math.max(min, 100f), step: Int = 1) : this(
        text,
        min,
        max,
        step,
        1f
    )

    override fun toJson(): JsonObject? {
        Preconditions.checkArgument(this.min < this.max, "Maximum slider value must exceed the minimum value")
        Preconditions.checkArgument(
            this.defaultValue >= this.min && this.defaultValue <= this.max,
            "Default value out of range"
        )

        `object`.addProperty("type", "slider")
        `object`.addProperty("text", this.text)
        `object`.addProperty("min", this.min)
        `object`.addProperty("max", this.max)
        `object`.addProperty("step", this.step)
        `object`.addProperty("default", this.defaultValue)

        return this.`object`
    }
}
