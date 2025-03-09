package org.chorus.form.response

import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.experimental.Accessors

/**
 * The response of a [cn.nukkit.form.element.custom.ElementDropdown] or [cn.nukkit.form.element.custom.ElementStepSlider]
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
class ElementResponse {
    /**
     * The ordinal of the selected option or step
     */
    val elementId: Int = 0

    /**
     * The text of the selected option or step
     */
    protected val elementText: String? = null
}
