/**
 * 与NPC对话框相关的类
 *
 *
 * Classes relevant to NPC dialog
 */
package cn.nukkit.dialog

import cn.nukkit.dialog.element.ElementDialogButton
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.dialog.handler.FormDialogHandler
import java.io.BufferedReader
import java.io.IOException
import cn.nukkit.utils.JSONUtils
import cn.nukkit.dialog.window.ScrollingTextDialog.ScrollingRunner
import cn.nukkit.dialog.element.ElementDialogButton.CmdLine
import cn.nukkit.dialog.response.FormResponseDialog
import cn.nukkit.network.protocol.NPCRequestPacket

