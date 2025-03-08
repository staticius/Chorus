/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author joserobjr
 * @since 2021-02-14
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class FilterTextPacket : DataPacket() {
    var text: String? = null
    var isFromServer: Boolean = false

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(text!!)
        byteBuf.writeBoolean(isFromServer)
    }

    override fun decode(byteBuf: HandleByteBuf) {
        text = byteBuf.readString()
        isFromServer = byteBuf.readBoolean()
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.FILTER_TEXT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
