package org.chorus.dialog.window

import org.chorus.Player
import org.chorus.dialog.element.ElementDialogButton
import org.chorus.dialog.handler.FormDialogHandler
import org.chorus.entity.Entity
import org.chorus.utils.JSONUtils
import com.google.common.reflect.TypeToken
import it.unimi.dsi.fastutil.objects.ObjectArrayList

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.stream.Collectors


class FormWindowDialog @JvmOverloads constructor(
    @JvmField var title: String?,
    @JvmField var content: String?,
    @JvmField var bindEntity: Entity?,
    private var buttons: MutableList<ElementDialogButton?> = ArrayList()
) :
    Dialog {
    @JvmField
    var skinData: String = ""

    //请不要随意调用此方法，否则可能会导致潜在的bug
    //usually you shouldn't edit this
    //in pnx this value is used to be an identifier
    var sceneName: String = (dialogId++).toString()
        //请不要随意调用此方法，否则可能会导致潜在的bug
        protected set

    @Transient
    protected val handlers: MutableList<FormDialogHandler> = ObjectArrayList()

    init {
        try {
            BufferedReader(InputStreamReader(Objects.requireNonNull(javaClass.classLoader.getResourceAsStream("npc_data.json")))).use { reader ->
                this.skinData = reader.lines().collect(
                    Collectors.joining("\n")
                )
            }
        } catch (e: IOException) {
            FormWindowDialog.log.error("Failed to load npc_data.json: ", e)
        }

        requireNotNull(this.bindEntity) { "bindEntity cannot be null!" }
    }

    fun getButtons(): List<ElementDialogButton?> {
        return buttons
    }

    fun setButtons(buttons: MutableList<ElementDialogButton?>) {
        this.buttons = buttons
    }

    fun addButton(text: String) {
        this.addButton(ElementDialogButton(text, text))
    }

    fun addButton(button: ElementDialogButton?) {
        buttons.add(button)
    }

    val entityId: Long
        get() = bindEntity!!.id

    fun addHandler(handler: FormDialogHandler) {
        handlers.add(handler)
    }

    fun getHandlers(): List<FormDialogHandler> {
        return handlers
    }

    var buttonJSONData: String?
        get() = JSONUtils.to(this.buttons)
        set(json) {
            var buttons =
                JSONUtils.from<MutableList<ElementDialogButton?>>(
                    json,
                    object : TypeToken<List<ElementDialogButton?>?>() {
                    }.type
                )
            //Cannot be null
            if (buttons == null) buttons = ArrayList()
            this.setButtons(buttons)
        }

    fun updateSceneName() {
        this.sceneName = (dialogId++).toString()
    }

    override fun send(player: Player) {
        player.showDialogWindow(this)
    }

    companion object {
        private var dialogId: Long = 0
    }
}
