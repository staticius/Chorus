package cn.nukkit.utils

import com.google.gson.JsonObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JSONUtilsTest {
    @Test
    fun test_from_class() {
        val from = JSONUtils.from(
            """
                {
                    "a1": 123,
                    "a2": 22.2
                }
                
                """.trimIndent(), JsonObject::class.java
        )
        Assertions.assertEquals(123, from["a1"].asInt)
        Assertions.assertEquals(22.2, from["a2"].asDouble)
    }

    @Test
    fun test_toPretty() {
        val data: MutableMap<String, Any> = HashMap()
        data["a1"] = 123
        data["a2"] = 22.2
        Assertions.assertEquals(
            """
                {
                  "a1": 123,
                  "a2": 22.2
                }
                """.trimIndent(), JSONUtils.toPretty<Map<String, Any>>(data)
        )
    }
}
