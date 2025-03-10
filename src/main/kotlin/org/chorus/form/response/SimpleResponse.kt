package org.chorus.form.response

import org.chorus.form.element.simple.ElementButton






/**
 * The response of a [org.chorus.form.window.ModalForm]
 */


@Accessors(chain = true, fluent = true)


class SimpleResponse : Response() {
    /**
     * The ordinal of the selected button
     * -1 if invalid
     */
    protected var buttonId: Int = -1

    /**
     * The button, if pressed
     */
    protected var button: ElementButton? = null
}
