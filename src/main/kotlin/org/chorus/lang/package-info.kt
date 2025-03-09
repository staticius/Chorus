/**
 * 用于解析语言文件(resources/lang)的一些类.
 *
 *
 * Classes relevant to parsing language files(Resources/lang).
 */
package org.chorus.lang

import cn.nukkit.lang.BaseLang
import java.util.HashMap
import java.io.IOException
import java.io.FileNotFoundException
import java.io.FileInputStream
import java.io.BufferedReader
import cn.nukkit.utils.JSONUtils
import cn.nukkit.lang.TextContainer
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.lang.LangCode
import cn.nukkit.plugin.PluginBase
import cn.nukkit.lang.PluginI18n
import cn.nukkit.lang.PluginI18nManager
import java.util.jar.JarFile
import java.util.Enumeration
import cn.nukkit.network.protocol.types.CommandOutputMessage
import cn.nukkit.lang.CommandOutputContainer

