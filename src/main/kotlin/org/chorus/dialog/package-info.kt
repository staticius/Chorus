/**
 * 与NPC对话框相关的类
 *
 *
 * Classes relevant to NPC dialog
 */
package org.chorus.dialog

import org.chorus.dialog.element.ElementDialogButton
import org.chorus.dialog.window.FormWindowDialog
import org.chorus.dialog.handler.FormDialogHandler
import java.io.BufferedReader
import java.io.IOException
import org.chorus.utils.JSONUtils
import org.chorus.dialog.window.ScrollingTextDialog.ScrollingRunner
import org.chorus.dialog.element.ElementDialogButton.CmdLine
import org.chorus.dialog.response.FormResponseDialog
import org.chorus.network.protocol.NPCRequestPacket

