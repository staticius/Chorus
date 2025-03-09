package org.chorus.network.protocol.types

enum class AuthInteractionModel {
    TOUCH,
    CROSSHAIR,
    CLASSIC;

    companion object {
        private val VALUES = entries.toTypedArray()

        fun fromOrdinal(ordinal: Int): AuthInteractionModel {
            return VALUES[ordinal]
        }
    }
}