package org.chorus.form.response

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.experimental.Accessors

/**
 * The response of a [cn.nukkit.form.window.ModalForm]
 */


@Accessors(chain = true, fluent = true)


class ModalResponse : Response() {
    /**
     * The ordinal of the selected option:
     * -1 if invalid
     * 0 if accepted
     * 1 if rejected
     */
    protected var buttonId: Int = -1
    protected var yes: Boolean = false
}
