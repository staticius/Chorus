package org.chorus.form

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.*
import org.chorus.event.Event
import org.chorus.event.player.PlayerFormRespondedEvent
import org.chorus.form.element.Element
import org.chorus.form.element.custom.*
import org.chorus.form.element.simple.ButtonImage
import org.chorus.form.element.simple.ElementButton
import org.chorus.form.response.CustomResponse
import org.chorus.form.response.ElementResponse
import org.chorus.form.response.ModalResponse
import org.chorus.form.response.SimpleResponse
import org.chorus.form.window.CustomForm
import org.chorus.form.window.ModalForm
import org.chorus.form.window.SimpleForm
import org.chorus.network.protocol.ModalFormResponsePacket
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.util.List

@ExtendWith(GameMockExtension::class)
class FormTest {
    @Test
    fun test_FormWindowCustom(player: TestPlayer, testPluginManager: TestPluginManager) {
        testPluginManager.resetAll()
        val test1 = ElementDropdown("test1", listOf("1", "2", "3"), 1) //default 2
        val test2 = ElementInput("test2", "placeholder", "defaultText")
        val test3 = ElementLabel("test3")
        val test4 = ElementSlider("test4", 0f, 100f, 1, 50f)
        val test5 = ElementStepSlider("test5", listOf("step1", "step2"), 1) //default step2
        val test6 = ElementToggle("test6", true)
        val test = CustomForm("test")
            .elements(
                ObjectArrayList.of(
                    test1,
                    test2,
                    test3,
                    test4,
                    test5,
                    test6
                )
            )

        test.send(player, 1)
        val dataPacketManager = player.session.dataPacketManager
        val playerHandle = PlayerHandle(player)

        val modalFormResponsePacket = ModalFormResponsePacket()
        modalFormResponsePacket.formId = 1
        modalFormResponsePacket.data = "[\"1\",\"input\",\"\",\"6\",\"0\",\"false\"]"
        checkNotNull(dataPacketManager)

        testPluginManager.registerTestEventHandler(
            List.of<TestEventHandler<out Event?>>(
                object : TestEventHandler<PlayerFormRespondedEvent>() {
                    override fun handle(event: PlayerFormRespondedEvent) {
                        val response = event.response as CustomResponse
                        val dropdownResponse = response.getDropdownResponse(0)
                        Assertions.assertEquals("2", dropdownResponse.elementText())
                        val inputResponse = response.getInputResponse(1)
                        Assertions.assertEquals("input", inputResponse)
                        val labelResponse = response.getLabelResponse(2)
                        Assertions.assertEquals("test3", labelResponse)
                        val sliderResponse = response.getSliderResponse(3)
                        Assertions.assertEquals(6f, sliderResponse)
                        val stepSliderResponse = response.getStepSliderResponse(4)
                        Assertions.assertEquals("step1", stepSliderResponse.elementText())
                        val toggleResponse = response.getToggleResponse(5)
                        Assertions.assertFalse(toggleResponse)

                        val genericDropdownResponse = response.getResponse<ElementResponse>(0)
                        Assertions.assertEquals(dropdownResponse.elementId(), genericDropdownResponse.elementId())

                        val responses = response.responses
                        Assertions.assertEquals(6, responses.size())

                        Assertions.assertEquals("test", test.title())
                        Assertions.assertEquals(
                            test1, test.elements()
                                .toArray<Element> { _Dummy_.__Array__() }[0]
                        )
                    }
                }
            ))

        dataPacketManager.processPacket(playerHandle, modalFormResponsePacket)
        testPluginManager.resetAll()
    }


    @Test
    fun test_FormWindowSimple(player: TestPlayer, testPluginManager: TestPluginManager) {
        testPluginManager.resetAll()
        val test = SimpleForm(
            "test_FormWindowSimple",
            "123456"
        )

        test.addButton(ElementButton("button1", ButtonImage.Type.PATH.of("textures/items/compass")))
            .addButton(
                "button2",
                ButtonImage.Type.URL.of("https://static.wikia.nocookie.net/minecraft_gamepedia/images/9/94/Oak_Button_%28S%29_JE4.png")
            )
            .addButton("button3")

        test.send(player, 1)

        val dataPacketManager = player.session.dataPacketManager
        val playerHandle = PlayerHandle(player)

        val modalFormResponsePacket = ModalFormResponsePacket()
        modalFormResponsePacket.formId = 1
        modalFormResponsePacket.data = "1"
        checkNotNull(dataPacketManager)

        testPluginManager.registerTestEventHandler(
            List.of<TestEventHandler<out Event?>>(
                object : TestEventHandler<PlayerFormRespondedEvent>() {
                    override fun handle(event: PlayerFormRespondedEvent) {
                        val response = (event.response as SimpleResponse)
                        val clickedButton = response.button()
                        Assertions.assertEquals("button2", clickedButton.text())
                        val buttonId = response.buttonId()
                        Assertions.assertEquals(1, buttonId)

                        val buttons = test.buttons().keys.toArray<ElementButton>(ElementButton.EMPTY_LIST)

                        Assertions.assertEquals("test_FormWindowSimple", test.title())
                        Assertions.assertEquals("button1", buttons[0].text())
                        Assertions.assertEquals("button3", buttons[2].text())
                        Assertions.assertEquals("textures/items/compass", buttons[0].image().data())
                    }
                }
            ))

        dataPacketManager.processPacket(playerHandle, modalFormResponsePacket)
        testPluginManager.resetAll()
    }

    @Test
    fun test_FormWindowModal(player: TestPlayer, testPluginManager: TestPluginManager) {
        testPluginManager.resetAll()
        val test = ModalForm("test_FormWindowModal").content("1028346237")
        test.send(player, 1)

        val dataPacketManager = player.session.dataPacketManager
        val playerHandle = PlayerHandle(player)

        val modalFormResponsePacket = ModalFormResponsePacket()
        modalFormResponsePacket.formId = 1
        modalFormResponsePacket.data = "false"
        checkNotNull(dataPacketManager)

        testPluginManager.registerTestEventHandler(
            List.of<TestEventHandler<out Event?>>(
                object : TestEventHandler<PlayerFormRespondedEvent>() {
                    override fun handle(event: PlayerFormRespondedEvent) {
                        val response = event.response as ModalResponse
                        val clickedButtonId = response.buttonId()
                        Assertions.assertEquals(1, clickedButtonId)
                        val yes = response.yes()
                        Assertions.assertFalse(yes)

                        Assertions.assertEquals("test_FormWindowModal", test.title())
                        Assertions.assertEquals("1028346237", test.content())
                    }
                }
            ))

        dataPacketManager.processPacket(playerHandle, modalFormResponsePacket)
        testPluginManager.resetAll()
    }
}
