package org.chorus.inventory.request

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequest
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import lombok.Getter
import lombok.Setter

class ItemStackRequestContext(@field:Getter private val itemStackRequest: ItemStackRequest) {
    
    
    private val currentActionIndex = 0
    private val extraData: MutableMap<String, Any> =
        HashMap()

    fun put(key: String, value: Any) {
        extraData[key] = value
    }

    fun has(key: String): Boolean {
        return extraData.containsKey(key)
    }

    fun <T> get(key: String): T? {
        return extraData[key] as T?
    }

    fun error(): ActionResponse {
        return ActionResponse(false, listOf())
    }

    fun success(containers: List<ItemStackResponseContainer>): ActionResponse {
        return ActionResponse(true, containers)
    }
}
