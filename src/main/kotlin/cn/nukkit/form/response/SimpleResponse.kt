package cn.nukkit.form.response

import cn.nukkit.form.element.simple.ElementButton
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.experimental.Accessors

/**
 * The response of a [cn.nukkit.form.window.ModalForm]
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
@AllArgsConstructor
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
