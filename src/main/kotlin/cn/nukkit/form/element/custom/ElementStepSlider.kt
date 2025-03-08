package cn.nukkit.form.element.custom

import com.google.common.base.Preconditions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import lombok.*
import lombok.experimental.Accessors
import java.util.function.Consumer

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
class ElementStepSlider : ElementCustom {
    protected var text: String? = null
    protected var steps: MutableList<String?>? = null
    protected var defaultStep: Int = 0

    @JvmOverloads
    constructor(text: String? = "", steps: List<String?>? = ArrayList()) : this(text, steps, 0)

    fun addStep(step: String?): ElementStepSlider {
        return this.addStep(step, false)
    }

    fun addStep(step: String?, isDefault: Boolean): ElementStepSlider {
        if (isDefault) {
            this.defaultStep = steps!!.size()
        }

        steps!!.add(step)

        return this
    }

    override fun toJson(): JsonObject? {
        Preconditions.checkArgument(
            this.defaultStep > -1 && this.defaultStep < steps!!.size(),
            "Default option not within range"
        )

        `object`.addProperty("type", "step_slider")
        `object`.addProperty("text", this.text)
        `object`.addProperty("default", this.defaultStep)

        val stepsArray = JsonArray()
        steps!!.forEach(Consumer { string: String? -> stepsArray.add(string) })
        `object`.add("steps", stepsArray)

        return this.`object`
    }
}
