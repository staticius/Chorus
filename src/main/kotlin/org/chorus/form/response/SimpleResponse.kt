package org.chorus.form.response

import org.chorus.form.element.simple.ElementButton


/**
 * The response of a [org.chorus.form.window.ModalForm]
 */
class SimpleResponse(
    /**
     * The ordinal of the selected button
     * -1 if invalid
     */
    var buttonId: Int = -1,
    /**
     * The button, if pressed
     */
    var button: ElementButton? = null
) : Response()
