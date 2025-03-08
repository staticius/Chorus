/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package cn.nukkit.utils

class InvalidIdentifierException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
}

