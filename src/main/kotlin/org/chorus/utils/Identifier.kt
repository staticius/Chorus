/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 *
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package org.chorus.utils

/**
 * 命名空间标识符
 */
class Identifier protected constructor(id: Array<String>) {
    val namespace: String
    val path: String

    init {
        this.namespace = if (id[0].isEmpty()) DEFAULT_NAMESPACE else id[0]
        this.path = id[1]
        if (!isNamespaceValid(this.namespace)) {
            throw InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ":" + this.path)
        }
        if (!isPathValid(this.path)) {
            throw InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ":" + this.path)
        }
    }

    constructor(id: String) : this(split(id, ':'))

    constructor(namespace: String, path: String) : this(arrayOf<String>(namespace, path))

    override fun toString(): String {
        return this.namespace + ":" + this.path
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o is Identifier) {
            return this.namespace == o.namespace && this.path == o.path
        }
        return false
    }

    override fun hashCode(): Int {
        return 31 * namespace.hashCode() + path.hashCode()
    }

    companion object {
        const val NAMESPACE_SEPARATOR: Char = ':'
        const val DEFAULT_NAMESPACE: String = "minecraft"

        /**
         * 通过自定义的命名空间分割符分割并返回一个Identifier对象
         *
         * @param id        字符串
         * @param delimiter 分割符
         * @return 命名空间对象
         */
        fun splitOn(id: String, delimiter: Char): Identifier {
            return Identifier(split(id, delimiter))
        }

        @JvmStatic
        fun tryParse(id: String): Identifier? {
            return try {
                Identifier(id)
            } catch (lv: InvalidIdentifierException) {
                null
            }
        }

        fun of(namespace: String, path: String): Identifier? {
            return try {
                Identifier(namespace, path)
            } catch (lv: InvalidIdentifierException) {
                null
            }
        }

        protected fun split(id: String, delimiter: Char): Array<String> {
            val strings = arrayOf(DEFAULT_NAMESPACE, id)
            val i = id.indexOf(delimiter)
            if (i >= 0) {
                strings[1] = id.substring(i + 1, id.length)
                if (i >= 1) {
                    strings[0] = id.substring(0, i)
                }
            }
            return strings
        }

        fun isCharValid(c: Char): Boolean {
            return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-'
        }

        private fun isPathValid(path: String): Boolean {
            for (i in 0..<path.length) {
                if (isPathCharacterValid(path[i])) continue
                return false
            }
            return true
        }

        private fun isNamespaceValid(namespace: String): Boolean {
            for (i in 0..<namespace.length) {
                if (isNamespaceCharacterValid(namespace[i])) continue
                return false
            }
            return true
        }

        fun isPathCharacterValid(character: Char): Boolean {
            return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/' || character == '.'
        }

        private fun isNamespaceCharacterValid(character: Char): Boolean {
            return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.'
        }

        fun isValid(id: String): Boolean {
            val strings = split(id, ':')
            return isNamespaceValid(if (strings[0].isEmpty()) DEFAULT_NAMESPACE else strings[0]) && isPathValid(
                strings[1]
            )
        }

        @JvmStatic
        fun assertValid(id: String) {
            val strings = split(id, ':')
            val namespace = if (strings[0].isEmpty()) DEFAULT_NAMESPACE else strings[0]
            val path = strings[1]
            if (!isNamespaceValid(namespace)) {
                throw InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: $namespace:$path")
            }
            if (!isPathValid(path)) {
                throw InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: $namespace:$path")
            }
        }
    }
}

