package org.chorus_oss.chorus.experimental.block

class BlockDefinitionRegistry {
    fun register(definition: BlockDefinition) {
        require(definitions.putIfAbsent(definition.identifier, definition) == null) {
            "BlockDefinition already registered for \"${definition.identifier}\""
        }

        definition.allPermutations.forEach {
            permutations[it.hash] = it

            components[it.hash] = definition.components + definition.permutations.flatMap { permutation ->
                if (permutation.condition(it)) permutation.components
                else emptyList()
            }
        }
    }

    companion object {
        /** Block Identifier -> Block Definition */
        val definitions = mutableMapOf<String, BlockDefinition>()

        /** Permutation Hash -> Block Permutation */
        val permutations = mutableMapOf<Int, BlockPermutation>()

        /** Permutation Hash -> Block Components */
        val components = mutableMapOf<Int, List<BlockComponent>>()
    }
}