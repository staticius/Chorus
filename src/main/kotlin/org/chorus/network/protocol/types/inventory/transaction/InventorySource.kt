package org.chorus.network.protocol.types.inventory.transaction

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.chorus.inventory.SpecialWindowId


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class InventorySource {
    private val type: Type? = null
    private val containerId = 0
    private val flag: Flag? = null

    enum class Type(private val id: Int) {
        INVALID(-1),
        CONTAINER(0),
        GLOBAL(1),
        WORLD_INTERACTION(2),
        CREATIVE(3),
        UNTRACKED_INTERACTION_UI(100),
        NON_IMPLEMENTED_TODO(99999);

        fun id(): Int {
            return id
        }

        companion object {
            private val BY_ID: Int2ObjectMap<Type> = Int2ObjectOpenHashMap(6)

            init {
                for (type in entries) {
                    BY_ID.put(type.id, type)
                }
            }

            fun byId(id: Int): Type {
                val type = BY_ID[id]
                return type ?: INVALID
            }
        }
    }

    enum class Flag {
        DROP_ITEM,
        PICKUP_ITEM,
        NONE
    }

    companion object {
        private val CREATIVE_SOURCE = InventorySource(Type.CREATIVE, SpecialWindowId.NONE.id, Flag.NONE)
        private val GLOBAL_SOURCE = InventorySource(Type.GLOBAL, SpecialWindowId.NONE.id, Flag.NONE)
        private val INVALID_SOURCE = InventorySource(Type.INVALID, SpecialWindowId.NONE.id, Flag.NONE)
        fun fromContainerWindowId(containerId: Int): InventorySource {
            return InventorySource(Type.CONTAINER, containerId, Flag.NONE)
        }

        fun fromCreativeInventory(): InventorySource {
            return CREATIVE_SOURCE
        }

        fun fromGlobalInventory(): InventorySource {
            return GLOBAL_SOURCE
        }

        fun fromInvalid(): InventorySource {
            return INVALID_SOURCE
        }

        fun fromNonImplementedTodo(containerId: Int): InventorySource {
            return InventorySource(Type.NON_IMPLEMENTED_TODO, containerId, Flag.NONE)
        }

        fun fromUntrackedInteractionUI(containerId: Int): InventorySource {
            return InventorySource(Type.UNTRACKED_INTERACTION_UI, containerId, Flag.NONE)
        }

        fun fromWorldInteraction(flag: Flag): InventorySource {
            return InventorySource(Type.WORLD_INTERACTION, SpecialWindowId.NONE.id, flag)
        }
    }
}
