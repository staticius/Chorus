package org.chorus_oss.chorus.dialog.handler

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.dialog.response.FormResponseDialog
import java.util.function.Consumer

fun interface FormDialogHandler {
    fun handle(player: Player, response: FormResponseDialog)

    companion object {
        fun withoutPlayer(responseConsumer: Consumer<FormResponseDialog>): FormDialogHandler {
            return FormDialogHandler { _, response ->
                responseConsumer.accept(
                    response
                )
            }
        }
    }
}
