package org.chorus.dialog.handler

import org.chorus.Player
import org.chorus.dialog.response.FormResponseDialog
import java.util.function.Consumer

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
