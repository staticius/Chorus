package org.chorus.utils

import com.google.common.collect.ImmutableList
import lombok.EqualsAndHashCode
import lombok.ToString

@ToString
@EqualsAndHashCode
class PersonaPieceTint(val pieceType: String, colors: List<String>) {
    val colors: ImmutableList<String> =
        ImmutableList.copyOf(colors)
}
