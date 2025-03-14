package org.chorus.nbt

import com.google.common.primitives.Bytes
import com.google.common.primitives.Ints
import org.chorus.nbt.snbt.*
import org.chorus.nbt.snbt.ast.*
import org.chorus.nbt.tag.*
import java.io.*
import java.util.Map

class SNBTParser private constructor(SNBT: String) {
    private val root: Node?

    init {
        val parser = SNBTParserImplement(StringReader(SNBT))
        parser.Root()
        root = parser.rootNode()
    }

    @Throws(ParseException::class)
    private fun parseNode(node: Node?): Tag? {
        var tag: Tag? = null
        if (node is ByteArrayNBT) {
            val tmp = ArrayList<Byte>()
            val it: Iterator<Node?> = node.iterator()
            while (it.hasNext()) {
                val child = it.next()
                if (child is Token) {
                    val s = child.normalizedText
                    if (isLiteralValue(child)) {
                        tmp.add(s.substring(0, s.length - 1).toByte())
                    }
                }
            }
            tag = ByteArrayTag(Bytes.toArray(tmp))
        } else if (node is IntArrayNBT) {
            val tmp = ArrayList<Int>()
            val it: Iterator<Node?> = node.iterator()
            while (it.hasNext()) {
                val child = it.next()
                if (child is Token) {
                    if (isLiteralValue(child)) {
                        tmp.add(child.normalizedText.toInt())
                    }
                }
            }
            tag = IntArrayTag(Ints.toArray(tmp))
        } else if (node is ListNBT) { //only Value
            tag = parseListTag(node)
        } else if (node is CompoundNBT) { //only KeyValuePair
            tag = parseCompoundNBT(node)
        }
        return tag
    }

    @Throws(ParseException::class)
    private fun parseCompoundNBT(node: Node): CompoundTag {
        val result = LinkedCompoundTag()
        val it: Iterator<Node?> = node.iterator()
        while (it.hasNext()) {
            val child = it.next()
            if (child is KeyValuePair) {
                val s = child.getFirstToken().normalizedText
                val key = s!!.substring(1, s!!.length - 1) //only STRING TOKEN
                if (child.getChildCount() == 3) {
                    val value = child.getChild(2)
                    if (value!!.hasChildNodes()) {
                        result.put(key, parseNode(value))
                    } else {
                        val token = value as Token
                        if (isLiteralValue(token)) {
                            result.put(key, parseToken(token))
                        }
                    }
                } else {
                    result.put(key, EndTag())
                }
            }
        }
        return result
    }

    private fun parseListTag(node: Node): ListTag<*> {
        val result = ListTag<Tag>()
        val it: Iterator<Node?> = node.iterator()
        while (it.hasNext()) {
            val child = it.next()
            if (child is Token) {
                if (isLiteralValue(child)) {
                    result.add(parseToken(child))
                }
            } else if (child!!.hasChildNodes()) {
                result.add(parseNode(child)!!)
            }
        }
        return result
    }

    private fun parseToken(token: Token): Tag {
        try {
            val s = token.normalizedText
            return when (token.type) {
                SNBTConstants.TokenType.FLOAT -> {
                    FloatTag(s.substring(0, s.length - 1).toFloat())
                }

                SNBTConstants.TokenType.DOUBLE -> {
                    DoubleTag(s.substring(0, s.length - 1).toDouble())
                }

                SNBTConstants.TokenType.BOOLEAN -> {
                    ByteTag(if (token.normalizedText.toBoolean()) 1 else 0)
                }

                SNBTConstants.TokenType.BYTE -> {
                    ByteTag(s.substring(0, s.length - 1).toByte().toInt())
                }

                SNBTConstants.TokenType.SHORT -> {
                    ShortTag(s.substring(0, s.length - 1).toShort().toInt())
                }

                SNBTConstants.TokenType.INTEGER -> {
                    IntTag(token.normalizedText.toInt())
                }

                SNBTConstants.TokenType.LONG -> {
                    LongTag(s.substring(0, s.length - 1).toLong())
                }

                SNBTConstants.TokenType.STRING -> {
                    StringTag(s.substring(1, s.length - 1))
                }

                else -> {
                    EndTag()
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return EndTag()
        }
    }

    private fun isLiteralValue(token: Token): Boolean {
        return when (token.type) {
            SNBTConstants.TokenType.FLOAT, SNBTConstants.TokenType.DOUBLE, SNBTConstants.TokenType.STRING, SNBTConstants.TokenType.SHORT, SNBTConstants.TokenType.INTEGER, SNBTConstants.TokenType.LONG, SNBTConstants.TokenType.BYTE, SNBTConstants.TokenType.BOOLEAN -> true
            else -> false
        }
    }

    companion object {
        @Throws(ParseException::class)
        fun parse(SNBT: String): CompoundTag {
            val parser = SNBTParser(SNBT)
            val tag = parser.parseNode(parser.root.getFirstChild())
            if (tag is CompoundTag) return tag
            return CompoundTag(Map.of("", tag))
        }
    }
}

