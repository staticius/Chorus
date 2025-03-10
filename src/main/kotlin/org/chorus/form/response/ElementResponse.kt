package org.chorus.form.response





/**
 * The response of a [org.chorus.form.element.custom.ElementDropdown] or [org.chorus.form.element.custom.ElementStepSlider]
 */

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
