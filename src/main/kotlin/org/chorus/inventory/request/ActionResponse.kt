package org.chorus.inventory.request

import cn.nukkit.network.protocol.types.itemstack.response.ItemStackResponseContainer

@JvmRecord
data class ActionResponse(@JvmField val ok: Boolean, @JvmField val containers: List<ItemStackResponseContainer>)
