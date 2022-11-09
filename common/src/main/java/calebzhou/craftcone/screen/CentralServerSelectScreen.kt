package calebzhou.craftcone.screen

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

/**
 * Created  on 2022-11-09,11:52.
 */
class CentralServerSelectScreen(val lastScreen: Screen): Screen(Component.translatable("multiplayer.title")) {
    val mc = Minecraft.getInstance()
    private lateinit var editButton: Button
    private lateinit var selectButton: Button
    private lateinit var deleteButton: Button
    init {

    }

    private fun onClickAddButton() {

    }

    private fun onClickDeleteButton() {

    }

    private fun onClickEditButton() {

    }

    private fun joinSelectedServer() {

    }

    override fun init() {
        addRenderableWidget(Button(
            width / 2 + 4 + 50, height - 52, 100, 20, Component.translatable("selectServer.add")
        ) {
            this.onClickAddButton()
        })
        addRenderableWidget(Button(width / 2 + 4, height - 28, 70, 20, Component.translatable("selectServer.refresh")) {
            this.refreshServerList()
        })
        addRenderableWidget(Button(width / 2 + 4 + 76, height - 28, 75, 20, CommonComponents.GUI_CANCEL) {
            mc.setScreen(lastScreen)
        })
        selectButton = addRenderableWidget(Button(width / 2 - 154, height - 52, 100, 20, Component.translatable("selectServer.select")) {
            this.joinSelectedServer()
        })
        editButton = addRenderableWidget(Button(width / 2 - 154, height - 28, 70, 20, Component.translatable("selectServer.edit")) {
            this.onClickEditButton()
        })
        deleteButton = addRenderableWidget(Button(
            width / 2 - 74, height - 28, 70, 20, Component.translatable("selectServer.delete")) {
            this.onClickDeleteButton()
        })
        super.init()

    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(poseStack)
        drawCenteredString(
            poseStack,
            font, "craftCONE Server List", width / 2, 20, 16777215
        )

        super.render(poseStack, mouseX, mouseY, partialTick)
    }

    private fun refreshServerList() {
        mc.setScreen(CentralServerSelectScreen(lastScreen))
    }
}