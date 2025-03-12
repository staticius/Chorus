package org.chorus.form.window


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.Player
import org.chorus.form.element.custom.ElementCustom
import org.chorus.form.response.CustomResponse
import org.chorus.form.response.ElementResponse
import org.chorus.utils.JSONUtils
import java.lang.reflect.Type
import java.util.function.BiConsumer
import java.util.function.Consumer


@Accessors(chain = true, fluent = true)

class CustomForm(title: String) : Form<CustomResponse?>(title) {
    var elements: ObjectArrayList<ElementCustom> = ObjectArrayList()

    fun addElement(element: ElementCustom): CustomForm {
        elements.add(element)
        return this
    }

    override fun onSubmit(submitted: BiConsumer<Player?, CustomResponse?>?): CustomForm {
        return super.onSubmit(submitted) as CustomForm
    }

    override fun onClose(callback: Consumer<Player?>?): CustomForm {
        return super.onClose(callback) as CustomForm
    }

    override fun send(player: Player): CustomForm {
        return super.send(player) as CustomForm
    }

    override fun send(player: Player, id: Int): CustomForm {
        return super.send(player, id) as CustomForm
    }

    override fun sendUpdate(player: Player): CustomForm {
        return super.sendUpdate(player) as CustomForm
    }

    override fun windowType(): String {
        return "custom_form"
    }

    override fun toJson(): String {
        val `object` = JsonObject()
        `object`.addProperty("type", this.windowType())
        `object`.addProperty("title", this.title)

        val elementArray = JsonArray()
        this.elements().forEach(Consumer { element: ElementCustom -> elementArray.add(element.toJson()) })

        `object`.add("content", elementArray)
        return `object`.toString()
    }

    override fun respond(player: Player, formData: String): CustomResponse? {
        if (!super.handle(player, formData)) {
            this.supplyClosed(player)
            return null
        }

        val response = CustomResponse()

        val elementResponses = JSONUtils.from<List<String>>(formData, LIST_STRING_TYPE)

        var i = 0
        val responseSize = elementResponses.size
        while (i < responseSize) {
            if (i >= elements.size) {
                break
            }

            val responseData = elementResponses[i]
            val element = elements[i]

            var elementResponse: Any? = null

            when (element) {
                -> {
                    val index = responseData.toInt()
                    val option: String = dropdown.options().get(index)
                    elementResponse = ElementResponse(index, option)
                }

                -> elementResponse = responseData
                -> elementResponse = label.text()
                -> elementResponse = java.lang.Float.parseFloat(responseData)
                -> {
                    val index = Integer.parseInt(responseData)
                    val step: String = stepSlider.steps().get(index)
                    elementResponse = ElementResponse(index, step)
                }

                -> elementResponse = java.lang.Boolean.parseBoolean(responseData)
                else -> {}
            }

            response.setResponse(i, elementResponse)
            i++
        }

        this.supplySubmitted(player, response)
        return response
    }

    override fun <M> putMeta(key: String, `object`: M): CustomForm {
        return super.putMeta(key, `object`) as CustomForm
    }

    companion object {
        protected var LIST_STRING_TYPE: Type = object : TypeToken<List<String?>?>() {}.type
    }
}
