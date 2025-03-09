/**
 * 用于解析语言文件(resources/lang)的一些类.
 *
 *
 * Classes relevant to parsing language files(Resources/lang).
 */
package org.chorus.lang

import org.chorus.lang.BaseLang
import java.util.HashMap
import java.io.IOException
import java.io.FileNotFoundException
import java.io.FileInputStream
import java.io.BufferedReader
import org.chorus.utils.JSONUtils
import org.chorus.lang.TextContainer
import org.chorus.lang.TranslationContainer
import org.chorus.lang.LangCode
import org.chorus.plugin.PluginBase
import org.chorus.lang.PluginI18n
import org.chorus.lang.PluginI18nManager
import java.util.jar.JarFile
import java.util.Enumeration
import org.chorus.network.protocol.types.CommandOutputMessage
import org.chorus.lang.CommandOutputContainer

