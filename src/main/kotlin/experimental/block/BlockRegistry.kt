package org.chorus_oss.chorus.experimental.block

object BlockRegistry {
    /** Identifier -> Definition */
    private val definitions = mutableMapOf<String, BlockDefinition>()

    /** Hash -> Permutation */
    private val permutations = mutableMapOf<Int, BlockPermutation>()

    fun register(vararg definitions: BlockDefinition) = definitions.forEach(::register)

    fun register(definitions: Iterable<BlockDefinition>) = definitions.forEach(::register)

    fun register(definition: BlockDefinition) {
        require(definitions.putIfAbsent(definition.identifier, definition) == null) {
            "BlockDefinition already registered for \"${definition.identifier}\""
        }

        definition.permutations.forEach {
            permutations[it.hash] = it
        }
    }

    operator fun plusAssign(definition: BlockDefinition) = definition.let(::register)

    operator fun plusAssign(definitions: Iterable<BlockDefinition>) = definitions.let(::register)

    operator fun plus(definition: BlockDefinition) = definition.let(::register)

    operator fun plus(definitions: Iterable<BlockDefinition>) = definitions.let(::register)

    operator fun get(identifier: String): BlockDefinition? = definitions[identifier]

    operator fun get(hash: Int): BlockPermutation? = permutations[hash]
}