package cn.nukkit.dialog.handler

import cn.nukkit.Player
import cn.nukkit.dialog.handler.FormDialogHandler
import cn.nukkit.dialog.response.FormResponseDialog
import java.util.function.Consumer
import kotlin.invoke

interface FormDialogHandler {
    fun handle(player: Player?, response: FormResponseDialog?)

    companion object {
        fun withoutPlayer(responseConsumer: Consumer<FormResponseDialog?>): FormDialogHandler {
            return FormDialogHandler { player: Player?, response: FormResponseDialog? ->
                responseConsumer.accept(
                    response
                )
            }
        }
    }
}
