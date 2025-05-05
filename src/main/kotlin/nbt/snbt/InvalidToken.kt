package org.chorus_oss.chorus.nbt.snbt

/**
 * Token subclass to represent lexically invalid input
 */
class InvalidToken(tokenSource: SNBTLexer?, beginOffset: Int, endOffset: Int) :
    Token(SNBTConstants.TokenType.INVALID, tokenSource, beginOffset, endOffset) {
    override val normalizedText: String
        get() = "Lexically Invalid Input:${getImage()}"
}


