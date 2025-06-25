package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.entity.Attribute
import org.chorus_oss.protocol.types.attribute.AttributeValue

fun AttributeValue.Companion.from(value: Attribute): AttributeValue {
    return AttributeValue(
        name = value.name,
        value = value.getValue(),
        min = value.minValue,
        max = value.maxValue,
    )
}