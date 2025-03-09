/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2021  José Roberto de Araújo Júnior
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
package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author joserobjr
 * @since 2021-07-06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SimulationTypePacket : DataPacket() {
    var type: SimulationType = SimulationType.GAME

    override fun decode(byteBuf: HandleByteBuf) {
        type = TYPES[byteBuf.readByte().toInt()]
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(type.ordinal.toByte().toInt())
    }

    enum class SimulationType {
        GAME,
        EDITOR,
        TEST
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SIMULATION_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        private val TYPES = SimulationType.entries.toTypedArray()
    }
}
