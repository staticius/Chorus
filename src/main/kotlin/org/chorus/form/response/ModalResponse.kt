package org.chorus.form.response


/**
 * The response of a [org.chorus.form.window.ModalForm]
 */
class ModalResponse(
    /**
     * The ordinal of the selected option:
     * -1 if invalid
     * 0 if accepted
     * 1 if rejected
     */
    protected var buttonId: Int = -1,
    protected var yes: Boolean = false,
) : Response()
