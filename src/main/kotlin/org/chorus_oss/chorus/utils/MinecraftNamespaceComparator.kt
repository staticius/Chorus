package org.chorus_oss.chorus.utils

import org.chorus_oss.chorus.item.Item
import java.nio.charset.StandardCharsets

object MinecraftNamespaceComparator {
    fun compareItems(itemA: Item, itemB: Item): Int {
        return compare(itemA.id, itemB.id)
    }

    fun compare(idA: String, idB: String): Int {
        val childIdA = idA.substring(idA.indexOf(":") + 1)
        val childIdB = idB.substring(idB.indexOf(":") + 1)

        // Compare by child first
        val childIdComparsion = childIdB.compareTo(childIdA)
        if (childIdComparsion != 0) {
            return childIdComparsion
        }

        // Compare by namespace if childs are equal
        val namespaceA = idA.substring(0, idA.indexOf(":"))
        val namespaceB = idB.substring(0, idB.indexOf(":"))
        return namespaceB.compareTo(namespaceA)
    }

    // https://gist.github.com/SupremeMortal/5e09c8b0eb6b3a30439b317b875bc29c
    // Thank you Supreme
    fun compareFNV(idA: String, idB: String): Int {
        val bytes1 = idA.toByteArray(StandardCharsets.UTF_8)
        val bytes2 = idB.toByteArray(StandardCharsets.UTF_8)
        val hash1: Long = HashUtils.fnv164(bytes1)
        val hash2: Long = HashUtils.fnv164(bytes2)
        return java.lang.Long.compareUnsigned(hash1, hash2)
    }
}
