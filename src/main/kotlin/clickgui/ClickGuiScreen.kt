package clickgui

import impl.qol.NearbyPlayerHider
import debug.DebugMode
import impl.qol.InvMeow
import impl.qol.DianaQol
import impl.`fun`.partycommands.PartyCommandSettings
import impl.`fun`.Overlay
import impl.`fun`.SchizoSim
import impl.`fun`.SystemNotifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.lwjgl.glfw.GLFW
import utils.modMessage
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class ClickGuiScreen : Screen(Text.literal("GibCoins Click GUI")) {
    private data class ActiveSlider(
        val categoryIndex: Int,
        val featureIndex: Int,
        val sliderIndex: Int
    )

    private data class ActiveTextInput(
        val categoryIndex: Int,
        val featureIndex: Int,
        val textIndex: Int,
        val cursorIndex: Int
    ) {
        fun matches(categoryIndex: Int, featureIndex: Int, textIndex: Int): Boolean {
            return this.categoryIndex == categoryIndex &&
                this.featureIndex == featureIndex &&
                this.textIndex == textIndex
        }
    }

    private data class VisibleTextWindow(
        val startIndex: Int,
        val text: String
    )

    private data class TooltipData(
        val text: String,
        val anchorX: Int,
        val anchorY: Int
    )

    private data class RowBounds(
        val type: RowType,
        val categoryIndex: Int,
        val featureIndex: Int,
        val settingIndex: Int,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    ) {
        fun contains(mouseX: Double, mouseY: Double): Boolean {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
        }
    }

    private enum class RowType {
        FEATURE,
        SETTING_TOGGLE,
        SETTING_SLIDER,
        SETTING_TEXT
    }

    private val categories = mutableListOf(
        ClickCategory(
            name = "qol",
            features = listOf(
                ClickFeature(
                    name = "Player Hider",
                    description = "Configure nearby player hiding and click-through behavior.",
                    status = { enabledLabel(NearbyPlayerHider.isEnabled()) },
                    toggleSettings = {
                        listOf(
                            ClickToggleSetting(
                                name = "Hide Players",
                                enabled = { NearbyPlayerHider.isRenderHidingEnabled() },
                                onToggle = { NearbyPlayerHider.toggleRenderHiding() }
                            ),
                            ClickToggleSetting(
                                name = "Hide All",
                                enabled = { NearbyPlayerHider.isHideAllEnabled() },
                                onToggle = { NearbyPlayerHider.toggleHideAll() }
                            ),
                            ClickToggleSetting(
                                name = "Ghost Mode",
                                enabled = { NearbyPlayerHider.isGhostModeEnabled() },
                                onToggle = { NearbyPlayerHider.toggleGhostMode() }
                            ),
                            ClickToggleSetting(
                                name = "Click Through Players",
                                enabled = { NearbyPlayerHider.isClickThroughEnabled() },
                                onToggle = { NearbyPlayerHider.toggleClickThrough() }
                            )
                        )
                    },
                    sliders = {
                        listOf(
                            ClickSliderSetting(
                                name = "Opacity",
                                min = NearbyPlayerHider.getMinGhostOpacity(),
                                max = NearbyPlayerHider.getMaxGhostOpacity(),
                                step = 1.0,
                                value = { NearbyPlayerHider.getGhostOpacity() },
                                onChange = NearbyPlayerHider::setGhostOpacity,
                                formatter = { value -> "${value.toInt()}%" },
                                insertAfterToggleName = "Ghost Mode"
                            ),
                            ClickSliderSetting(
                                name = "Distance",
                                min = NearbyPlayerHider.getMinHideDistance(),
                                max = NearbyPlayerHider.getMaxHideDistance(),
                                step = 0.1,
                                value = { NearbyPlayerHider.getHideDistance() },
                                onChange = NearbyPlayerHider::setHideDistance,
                                formatter = { value -> String.format(Locale.US, "%.1f", value) },
                                insertAfterToggleName = "Hide Players"
                            )
                        )
                    },
                    onClick = { NearbyPlayerHider.toggleEnabled() }
                ),
                ClickFeature(
                    name = "InvMeow",
                    description = "Plays cat sounds on invincibility proc.",
                    status = { enabledLabel(InvMeow.isEnabled()) },
                    sliders = {
                        listOf(
                            ClickSliderSetting(
                                name = "Volume",
                                min = InvMeow.getMinVolume(),
                                max = InvMeow.getMaxVolume(),
                                step = 0.2,
                                value = { InvMeow.getVolume() },
                                onChange = InvMeow::setVolume,
                                formatter = { value -> String.format(Locale.US, "%.1f", value) }
                            )
                        )
                    },
                    onClick = { InvMeow.toggleEnabled() }
                ),
                ClickFeature(
                    name = "Diana qol",
                    description = "Helps with annoying hitboxes during Diana Event.",
                    status = { enabledLabel(DianaQol.isEnabled()) },
                    onClick = { DianaQol.toggleEnabled() }
                )
            )
        ),
        ClickCategory(
            name = "fun",
            features = listOf(
                ClickFeature(
                    name = "Party Commands",
                    description = "Enable chat-triggered party commands and control each command separately.",
                    status = { enabledLabel(PartyCommandSettings.isEnabled()) },
                    toggleSettings = {
                        listOf(
                            ClickToggleSetting(
                                name = "Only register own",
                                enabled = { PartyCommandSettings.isOwnerOnlyEnabled() },
                                onToggle = { PartyCommandSettings.toggleOwnerOnly() }
                            ),
                            ClickToggleSetting(
                                name = "Forcefem",
                                enabled = { PartyCommandSettings.isForcefemEnabled() },
                                onToggle = { PartyCommandSettings.toggleForcefem() }
                            ),
                            ClickToggleSetting(
                                name = "GambleKick",
                                enabled = { PartyCommandSettings.isGambleKickEnabled() },
                                onToggle = { PartyCommandSettings.toggleGambleKick() }
                            ),
                            ClickToggleSetting(
                                name = "FunFact",
                                enabled = { PartyCommandSettings.isFunFactEnabled() },
                                onToggle = { PartyCommandSettings.toggleFunFact() }
                            ),
                            ClickToggleSetting(
                                name = "ShitterCheck",
                                enabled = { PartyCommandSettings.isShitterCheckEnabled() },
                                onToggle = { PartyCommandSettings.toggleShitterCheck() }
                            ),
                            ClickToggleSetting(
                                name = "Kill",
                                enabled = { PartyCommandSettings.isKillEnabled() },
                                onToggle = { PartyCommandSettings.toggleKill() }
                            )
                        )
                    },
                    textSettings = {
                        listOf(
                            ClickTextSetting(
                                name = "Blacklist",
                                buttonText = {
                                    val count = PartyCommandSettings.getBlacklistCount()
                                    if (count == 0) "Edit" else "$count names"
                                },
                                value = { PartyCommandSettings.getBlacklist() },
                                onChange = { value -> PartyCommandSettings.setBlacklist(value) },
                                placeholder = "name1, name2"
                            )
                        )
                    },
                    onClick = { PartyCommandSettings.toggleEnabled() }
                ),
                ClickFeature(
                    name = "Schizophrenia",
                    description = "Pay attention to your chat.",
                    status = { enabledLabel(SchizoSim.isEnabled()) },
                    toggleSettings = {
                        listOf(
                            ClickToggleSetting(
                                name = "Jump Scare",
                                enabled = { Overlay.isEnabled() },
                                onToggle = { Overlay.toggleEnabled() }
                            ),
                            ClickToggleSetting(
                                name = "The Voices",
                                enabled = { SystemNotifier.isEnabled() },
                                onToggle = { SystemNotifier.toggleEnabled() }
                            )
                        )
                    },
                    onClick = { SchizoSim.toggleEnabled() }
                )
            )
        ),
        ClickCategory(
            name = "debug",
            features = listOf(
                ClickFeature(
                    name = "Show Overlay Now",
                    description = "Left click immediately shows the overlay once.",
                    settings = {
                        listOf(
                            "Type" to "Action",
                            "Hint" to "Left click to run",
                            "Preview" to "Shows the overlay immediately"
                        )
                    },
                    onClick = {
                        Overlay.show()
                        modMessage("Overlay shown")
                    }
                ),
                ClickFeature(
                    name = "Roll Now",
                    description = "Left click performs one roll immediately.",
                    settings = {
                        listOf(
                            "Type" to "Action",
                            "Hint" to "Left click to run",
                            "Effect" to "Rolls one notifier message"
                        )
                    },
                    onClick = {
                        SystemNotifier.roll()
                        modMessage("System rolled once")
                    }
                )
            )
        )
    )

    private val rowBounds = mutableListOf<RowBounds>()
    private var searchQuery = ""
    private val scrollOffsets = MutableList(categories.size) { 0 }
    private var tooltip: TooltipData? = null
    private var activeSlider: ActiveSlider? = null
    private var activeTextInput: ActiveTextInput? = null

    private val categoryWidth = 240
    private val categoryGap = 10
    private val panelPadding = 0
    private val headerHeight = 32
    private val featureHeight = 32
    private val settingHeight = 32
    private val rowSpacing = 0
    private val settingInset = 8
    private val columnRightPadding = 10
    private val sliderTrackWidth = 92
    private val sliderTrackHeight = 6
    private val sliderKnobSize = 10
    private val searchWidth = 350
    private val searchHeight = 40
    private val accentColor = 0xFFF59CBA.toInt()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        renderInGameBackground(context)

        rowBounds.clear()
        tooltip = null

        val visibleCategoryIndices = categories.indices.filter { isCategoryVisible(categories[it]) }
        val visibleCount = visibleCategoryIndices.size
        visibleCategoryIndices.forEachIndexed { visibleIndex, categoryIndex ->
            val category = categories[categoryIndex]
            val panelX = width / 2 - ((visibleCount * categoryWidth) + ((visibleCount - 1) * categoryGap)) / 2 +
                visibleIndex * (categoryWidth + categoryGap)
            val panelY = max(24, height / 7)
            val totalContentHeight = computeContentHeight(category)
            val maxPanelHeight = height - panelY - 100
            val panelHeight = min(headerHeight + totalContentHeight + panelPadding, maxPanelHeight)
            val visibleContentHeight = max(0, panelHeight - headerHeight)
            val maxScroll = max(0, totalContentHeight - visibleContentHeight)
            scrollOffsets[categoryIndex] = scrollOffsets[categoryIndex].coerceIn(0, maxScroll)

            context.fill(panelX, panelY, panelX + categoryWidth, panelY + panelHeight, 0xD2121212.toInt())
            context.fill(panelX, panelY, panelX + categoryWidth, panelY + headerHeight, 0xEE1A1A1A.toInt())
            drawBorder(context, panelX, panelY, categoryWidth, panelHeight, 0xAA2A2A2A.toInt())
            context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal(category.name).formatted(Formatting.BOLD),
                panelX + categoryWidth / 2,
                panelY + 8,
                0xFFFFFFFF.toInt()
            )

            val clipTop = panelY + headerHeight
            val clipBottom = panelY + panelHeight
            context.enableScissor(panelX, clipTop, panelX + categoryWidth, clipBottom)
            val currentY = panelY + headerHeight + rowSpacing - scrollOffsets[categoryIndex]
            drawCategoryRows(context, categoryIndex, category, panelX + panelPadding, currentY, mouseX, mouseY, clipTop, clipBottom)
            context.disableScissor()
        }

        drawTooltip(context)
        drawSearchBar(context)
    }

    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        val clickedRow = rowBounds.lastOrNull { it.contains(click.x(), click.y()) } ?: run {
            activeTextInput = null
            return super.mouseClicked(click, doubled)
        }
        val button = click.button()

        return when (clickedRow.type) {
            RowType.FEATURE -> {
                val feature = categories[clickedRow.categoryIndex].features[clickedRow.featureIndex]

                if (button == 0) {
                    activeTextInput = null
                    feature.onClick()
                    true
                } else if (button == 1 && hasExpandableSettings(feature)) {
                    activeTextInput = null
                    feature.expanded = !feature.expanded
                    true
                } else {
                    super.mouseClicked(click, doubled)
                }
            }

            RowType.SETTING_TOGGLE -> {
                val feature = categories[clickedRow.categoryIndex].features[clickedRow.featureIndex]
                val toggleSetting = feature.toggleSettings().getOrNull(clickedRow.settingIndex)
                if (button == 0) {
                    activeTextInput = null
                    toggleSetting?.onToggle?.invoke()
                    true
                } else {
                    super.mouseClicked(click, doubled)
                }
            }

            RowType.SETTING_SLIDER -> {
                if (button != 0) {
                    return super.mouseClicked(click, doubled)
                }

                activeTextInput = null
                updateSliderValue(clickedRow, click.x())
                activeSlider = ActiveSlider(clickedRow.categoryIndex, clickedRow.featureIndex, clickedRow.settingIndex)
                true
            }

            RowType.SETTING_TEXT -> {
                if (button != 0) {
                    return super.mouseClicked(click, doubled)
                }

                activeSlider = null
                val textSetting = getTextSetting(clickedRow.categoryIndex, clickedRow.featureIndex, clickedRow.settingIndex)
                val currentValue = textSetting?.value().orEmpty()
                val activeInput = activeTextInput
                val cursorIndex = if (activeInput?.matches(clickedRow.categoryIndex, clickedRow.featureIndex, clickedRow.settingIndex) == true) {
                    cursorIndexForClick(currentValue, activeInput.cursorIndex, clickedRow.x, clickedRow.width, click.x())
                } else {
                    currentValue.length
                }
                activeTextInput = ActiveTextInput(
                    clickedRow.categoryIndex,
                    clickedRow.featureIndex,
                    clickedRow.settingIndex,
                    cursorIndex.coerceIn(0, currentValue.length)
                )
                true
            }
        }
    }

    override fun mouseDragged(click: Click, offsetX: Double, offsetY: Double): Boolean {
        val slider = activeSlider ?: return super.mouseDragged(click, offsetX, offsetY)
        if (click.button() != 0) {
            return super.mouseDragged(click, offsetX, offsetY)
        }

        val row = rowBounds.lastOrNull {
            it.type == RowType.SETTING_SLIDER &&
                it.categoryIndex == slider.categoryIndex &&
                it.featureIndex == slider.featureIndex &&
                it.settingIndex == slider.sliderIndex
        } ?: return true

        updateSliderValue(row, click.x())
        return true
    }

    override fun mouseReleased(click: Click): Boolean {
        if (click.button() == 0) {
            activeSlider = null
        }
        return super.mouseReleased(click)
    }

    override fun keyPressed(input: KeyInput): Boolean {
        val textInput = activeTextInput
        if (textInput != null) {
            val textSetting = getTextSetting(textInput) ?: run {
                activeTextInput = null
                return super.keyPressed(input)
            }
            val currentValue = textSetting.value()
            val cursorIndex = textInput.cursorIndex.coerceIn(0, currentValue.length)

            if (isPasteShortcut(input)) {
                pasteClipboardText(textSetting, textInput, currentValue, cursorIndex)
                return true
            }

            when (input.key()) {
                GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                    activeTextInput = null
                    return true
                }

                GLFW.GLFW_KEY_LEFT -> {
                    activeTextInput = textInput.copy(cursorIndex = (cursorIndex - 1).coerceAtLeast(0))
                    return true
                }

                GLFW.GLFW_KEY_RIGHT -> {
                    activeTextInput = textInput.copy(cursorIndex = (cursorIndex + 1).coerceAtMost(currentValue.length))
                    return true
                }

                GLFW.GLFW_KEY_HOME -> {
                    activeTextInput = textInput.copy(cursorIndex = 0)
                    return true
                }

                GLFW.GLFW_KEY_END -> {
                    activeTextInput = textInput.copy(cursorIndex = currentValue.length)
                    return true
                }

                GLFW.GLFW_KEY_BACKSPACE -> {
                    if (cursorIndex > 0) {
                        textSetting.onChange(
                            currentValue.substring(0, cursorIndex - 1) + currentValue.substring(cursorIndex)
                        )
                        activeTextInput = textInput.copy(cursorIndex = cursorIndex - 1)
                    }
                    return true
                }

                GLFW.GLFW_KEY_DELETE -> {
                    if (cursorIndex < currentValue.length) {
                        textSetting.onChange(
                            currentValue.substring(0, cursorIndex) + currentValue.substring(cursorIndex + 1)
                        )
                        activeTextInput = textInput.copy(cursorIndex = cursorIndex)
                    }
                    return true
                }
            }

            return true
        }

        when (input.key()) {
            GLFW.GLFW_KEY_ESCAPE -> return super.keyPressed(input)
            GLFW.GLFW_KEY_BACKSPACE -> {
                if (searchQuery.isNotEmpty()) {
                    searchQuery = searchQuery.dropLast(1)
                    return true
                }
            }
        }

        return super.keyPressed(input)
    }

    override fun charTyped(input: CharInput): Boolean {
        val textInput = activeTextInput
        if (textInput != null) {
            val textSetting = getTextSetting(textInput) ?: run {
                activeTextInput = null
                return super.charTyped(input)
            }

            if (input.isValidChar()) {
                val typed = String(Character.toChars(input.codepoint()))
                val currentValue = textSetting.value()
                val cursorIndex = textInput.cursorIndex.coerceIn(0, currentValue.length)
                if (currentValue.length + typed.length <= textSetting.maxLength) {
                    textSetting.onChange(
                        currentValue.substring(0, cursorIndex) + typed + currentValue.substring(cursorIndex)
                    )
                    activeTextInput = textInput.copy(cursorIndex = cursorIndex + typed.length)
                }
            }
            return true
        }

        if (input.isValidChar()) {
            val typed = String(Character.toChars(input.codepoint()))
            if (searchQuery.length + typed.length <= 16) {
                searchQuery += typed
                return true
            }
        }

        return super.charTyped(input)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        val scrollStep = if (verticalAmount > 0) -16 else if (verticalAmount < 0) 16 else 0
        if (scrollStep == 0) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
        }

        val visibleCategoryIndices = categories.indices.filter { isCategoryVisible(categories[it]) }
        val visibleCount = visibleCategoryIndices.size
        visibleCategoryIndices.forEachIndexed { visibleIndex, categoryIndex ->
            val category = categories[categoryIndex]
            val panelX = width / 2 - ((visibleCount * categoryWidth) + ((visibleCount - 1) * categoryGap)) / 2 +
                visibleIndex * (categoryWidth + categoryGap)
            val panelY = max(24, height / 7)
            val totalContentHeight = computeContentHeight(category)
            val maxPanelHeight = height - panelY - 100
            val panelHeight = min(headerHeight + totalContentHeight + panelPadding, maxPanelHeight)
            val clipTop = panelY + headerHeight
            val clipBottom = panelY + panelHeight

            if (mouseX >= panelX && mouseX <= panelX + categoryWidth && mouseY >= clipTop && mouseY <= clipBottom) {
                val visibleContentHeight = max(0, panelHeight - headerHeight)
                val maxScroll = max(0, totalContentHeight - visibleContentHeight)
                if (maxScroll <= 0) {
                    return true
                }

                scrollOffsets[categoryIndex] = (scrollOffsets[categoryIndex] + scrollStep).coerceIn(0, maxScroll)
                return true
            }
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun shouldPause(): Boolean = false

    private fun drawFeatureRow(
        context: DrawContext,
        categoryIndex: Int,
        featureIndex: Int,
        feature: ClickFeature,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        clipTop: Int,
        clipBottom: Int
    ) {
        val rowWidth = categoryWidth - panelPadding * 2
        val hovered = mouseX in x..(x + rowWidth) && mouseY in y..(y + featureHeight)
        val statusText = feature.status?.invoke().orEmpty()
        val enabled = statusText == "ON"
        val backgroundColor = when {
            enabled && hovered -> accentColor
            enabled -> accentColor
            hovered -> 0xFF2F2F2F.toInt()
            else -> 0xFF242424.toInt()
        }
        val indicator = if (feature.expanded) "v" else ">"
        val hasDetails = hasExpandableSettings(feature)
        val labelWidth = textRenderer.getWidth(feature.name)
        val statusWidth = textRenderer.getWidth(statusText)

        context.fill(x, y, x + rowWidth, y + featureHeight, backgroundColor)
        val borderColor = if (enabled) accentColor else 0xAA2E2E2E.toInt()
        drawBorder(context, x, y, rowWidth, featureHeight, borderColor)
        context.drawTextWithShadow(
            textRenderer,
            Text.literal(feature.name),
            x + rowWidth / 2 - labelWidth / 2,
            y + 11,
            0xFFFFFFFF.toInt()
        )

        if (statusText.isNotEmpty() && !enabled) {
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(statusText),
                x + rowWidth - statusWidth - 32,
                y + 11,
                0xFFD0D0D0.toInt()
            )
        }

        if (hasDetails) {
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(indicator),
                x + rowWidth - 16,
                y + 11,
                0xFFFFFFFF.toInt()
            )
        }

        if (hovered && feature.description.isNotBlank()) {
            tooltip = TooltipData(
                text = feature.description,
                anchorX = x + rowWidth + 10,
                anchorY = y
            )
        }

        addRowBoundsIfVisible(RowType.FEATURE, categoryIndex, featureIndex, -1, x, y, rowWidth, featureHeight, clipTop, clipBottom)
    }

    private fun drawSettingsBox(
        context: DrawContext,
        feature: ClickFeature,
        categoryIndex: Int,
        featureIndex: Int,
        x: Int,
        y: Int,
        clipTop: Int,
        clipBottom: Int
    ): Int {
        val boxWidth = categoryWidth - panelPadding * 2 - columnRightPadding - settingInset
        val toggleRows = feature.toggleSettings()
        val infoRows = feature.settings()
        val sliders = feature.sliders()
        val textRows = feature.textSettings()
        var currentY = y

        toggleRows.forEachIndexed { settingIndex, toggleRow ->
            context.fill(x, currentY, x + boxWidth, currentY + settingHeight, 0xA5141414.toInt())
            val labelX = x + 6
            val labelY = currentY + settingHeight / 2 - 8
            context.drawTextWithShadow(textRenderer, Text.literal(toggleRow.name), labelX, labelY, 0xFFFFFFFF.toInt())

            val toggleX = x + boxWidth - 40
            val toggleY = currentY + settingHeight / 2 - 10
            val enabled = toggleRow.enabled()
            val trackColor = if (enabled) accentColor else 0xFF262626.toInt()
            val trackBorder = accentColor
            context.fill(toggleX, toggleY, toggleX + 34, toggleY + 20, trackColor)
            drawRoundedOutline(context, toggleX, toggleY, 34, 20, trackBorder)
            val knobX = if (enabled) toggleX + 20 else toggleX + 4
            context.fill(knobX, toggleY + 4, knobX + 12, toggleY + 16, 0xFFFFFFFF.toInt())
            addRowBoundsIfVisible(RowType.SETTING_TOGGLE, categoryIndex, featureIndex, settingIndex, toggleX, toggleY, 34, 20, clipTop, clipBottom)

            currentY += settingHeight

            sliders.forEachIndexed { sliderIndex, slider ->
                if (slider.insertAfterToggleName == toggleRow.name) {
                    currentY = drawSliderRow(
                        context,
                        slider,
                        categoryIndex,
                        featureIndex,
                        sliderIndex,
                        x,
                        currentY,
                        boxWidth,
                        clipTop,
                        clipBottom
                    )
                }
            }
        }

        textRows.forEachIndexed { textIndex, textSetting ->
            currentY = drawTextSettingRow(
                context,
                textSetting,
                categoryIndex,
                featureIndex,
                textIndex,
                x,
                currentY,
                boxWidth,
                clipTop,
                clipBottom
            )
        }

        infoRows.forEach { (label, value) ->
            context.fill(x, currentY, x + boxWidth, currentY + settingHeight, 0xA5141414.toInt())
            val labelX = x + 6
            val labelY = currentY + settingHeight / 2 - 8
            context.drawTextWithShadow(textRenderer, Text.literal(label), labelX, labelY, 0xFFFFFFFF.toInt())
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(value),
                x + boxWidth - textRenderer.getWidth(value) - 8,
                labelY,
                0xFFBFBFBF.toInt()
            )
            currentY += settingHeight
        }

        sliders.forEachIndexed { sliderIndex, slider ->
            if (slider.insertAfterToggleName == null) {
                currentY = drawSliderRow(
                    context,
                    slider,
                    categoryIndex,
                    featureIndex,
                    sliderIndex,
                    x,
                    currentY,
                    boxWidth,
                    clipTop,
                    clipBottom
                )
            }
        }

        return currentY + rowSpacing
    }

    private fun drawSliderRow(
        context: DrawContext,
        slider: ClickSliderSetting,
        categoryIndex: Int,
        featureIndex: Int,
        sliderIndex: Int,
        x: Int,
        y: Int,
        boxWidth: Int,
        clipTop: Int,
        clipBottom: Int
    ): Int {
        context.fill(x, y, x + boxWidth, y + settingHeight, 0xA5141414.toInt())

        val labelX = x + 6
        val labelY = y + settingHeight / 2 - 8
        val valueText = slider.formatter(slider.value())
        val valueWidth = textRenderer.getWidth(valueText)
        context.drawTextWithShadow(textRenderer, Text.literal(slider.name), labelX, labelY, 0xFFFFFFFF.toInt())
        context.drawTextWithShadow(
            textRenderer,
            Text.literal(valueText),
            x + boxWidth - valueWidth - 8,
            labelY,
            0xFFBFBFBF.toInt()
        )

        val trackX = x + boxWidth - sliderTrackWidth - valueWidth - 18
        val trackY = y + settingHeight / 2 - sliderTrackHeight / 2
        val clampedValue = slider.value().coerceIn(slider.min, slider.max)
        val progress = if (slider.max == slider.min) {
            0.0f
        } else {
            ((clampedValue - slider.min) / (slider.max - slider.min)).toFloat()
        }
        val fillWidth = (sliderTrackWidth * progress).toInt()

        context.fill(trackX, trackY, trackX + sliderTrackWidth, trackY + sliderTrackHeight, 0xFF262626.toInt())
        if (fillWidth > 0) {
            context.fill(trackX, trackY, trackX + fillWidth, trackY + sliderTrackHeight, accentColor)
        }
        drawBorder(context, trackX, trackY, sliderTrackWidth, sliderTrackHeight, 0xAA2E2E2E.toInt())

        val knobX = (trackX + (sliderTrackWidth - sliderKnobSize) * progress).toInt()
        val knobY = y + settingHeight / 2 - sliderKnobSize / 2
        context.fill(knobX, knobY, knobX + sliderKnobSize, knobY + sliderKnobSize, 0xFFFFFFFF.toInt())
        drawBorder(context, knobX, knobY, sliderKnobSize, sliderKnobSize, 0xAA2E2E2E.toInt())

        addRowBoundsIfVisible(
            RowType.SETTING_SLIDER,
            categoryIndex,
            featureIndex,
            sliderIndex,
            trackX,
            knobY,
            sliderTrackWidth,
            sliderKnobSize,
            clipTop,
            clipBottom
        )

        return y + settingHeight
    }

    private fun drawTextSettingRow(
        context: DrawContext,
        textSetting: ClickTextSetting,
        categoryIndex: Int,
        featureIndex: Int,
        textIndex: Int,
        x: Int,
        y: Int,
        boxWidth: Int,
        clipTop: Int,
        clipBottom: Int
    ): Int {
        context.fill(x, y, x + boxWidth, y + settingHeight, 0xA5141414.toInt())

        val labelX = x + 6
        val labelY = y + settingHeight / 2 - 8
        context.drawTextWithShadow(textRenderer, Text.literal(textSetting.name), labelX, labelY, 0xFFFFFFFF.toInt())

        val activeInput = activeTextInput
        if (activeInput?.matches(categoryIndex, featureIndex, textIndex) == true) {
            val fieldX = x + 74
            val fieldY = y + settingHeight / 2 - 10
            val fieldWidth = boxWidth - 80
            val value = textSetting.value()
            val cursorIndex = activeInput.cursorIndex.coerceIn(0, value.length)
            val color = if (value.isEmpty()) 0xFF9A9A9A.toInt() else 0xFFFFFFFF.toInt()
            val visibleText = visibleTextWindow(value, cursorIndex, fieldWidth - 8)
            val displayText = if (value.isEmpty()) {
                fitTextToWidth(textSetting.placeholder, fieldWidth - 8)
            } else {
                visibleText.text
            }
            val cursorX = fieldX + 4 + textRenderer.getWidth(value.substring(visibleText.startIndex, cursorIndex))

            context.fill(fieldX, fieldY, fieldX + fieldWidth, fieldY + 20, 0xFF262626.toInt())
            drawRoundedOutline(context, fieldX, fieldY, fieldWidth, 20, accentColor)
            context.drawTextWithShadow(textRenderer, Text.literal(displayText), fieldX + 4, fieldY + 6, color)
            context.fill(cursorX, fieldY + 4, cursorX + 1, fieldY + 16, 0xFFFFFFFF.toInt())
            addRowBoundsIfVisible(RowType.SETTING_TEXT, categoryIndex, featureIndex, textIndex, fieldX, fieldY, fieldWidth, 20, clipTop, clipBottom)
        } else {
            val buttonText = textSetting.buttonText()
            val buttonWidth = max(50, textRenderer.getWidth(buttonText) + 16).coerceAtMost(boxWidth - 86)
            val buttonX = x + boxWidth - buttonWidth - 6
            val buttonY = y + settingHeight / 2 - 10

            context.fill(buttonX, buttonY, buttonX + buttonWidth, buttonY + 20, 0xFF262626.toInt())
            drawRoundedOutline(context, buttonX, buttonY, buttonWidth, 20, accentColor)
            context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal(fitTextToWidth(buttonText, buttonWidth - 8)),
                buttonX + buttonWidth / 2,
                buttonY + 6,
                0xFFFFFFFF.toInt()
            )
            addRowBoundsIfVisible(RowType.SETTING_TEXT, categoryIndex, featureIndex, textIndex, buttonX, buttonY, buttonWidth, 20, clipTop, clipBottom)
        }

        return y + settingHeight
    }

    private fun drawCategoryRows(
        context: DrawContext,
        categoryIndex: Int,
        category: ClickCategory,
        x: Int,
        startY: Int,
        mouseX: Int,
        mouseY: Int,
        clipTop: Int,
        clipBottom: Int
    ) {
        var currentY = startY

        category.features.forEachIndexed { featureIndex, feature ->
            if (!matchesSearch(feature)) {
                return@forEachIndexed
            }
            if (rowIntersectsViewport(currentY, featureHeight, clipTop, clipBottom)) {
                drawFeatureRow(context, categoryIndex, featureIndex, feature, x, currentY, mouseX, mouseY, clipTop, clipBottom)
            }
            currentY += featureHeight + rowSpacing

            if (feature.expanded) {
                currentY = drawSettingsBox(context, feature, categoryIndex, featureIndex, x + settingInset, currentY, clipTop, clipBottom)
            }
        }
    }

    private fun computeContentHeight(category: ClickCategory): Int {
        var total = rowSpacing
        category.features.forEach { feature ->
            if (!matchesSearch(feature)) {
                return@forEach
            }
            total += featureHeight + rowSpacing
            if (feature.expanded) {
                total += feature.toggleSettings().size * settingHeight
                total += feature.settings().size * settingHeight
                total += feature.sliders().size * settingHeight
                total += feature.textSettings().size * settingHeight
                total += rowSpacing
            }
        }
        return total
    }

    private fun drawSearchBar(context: DrawContext) {
        val x = width / 2 - searchWidth / 2
        val y = height - 90

        context.fill(x, y, x + searchWidth, y + searchHeight, 0xE2262626.toInt())
        drawRoundedOutline(context, x, y, searchWidth, searchHeight, accentColor)

        val text = if (searchQuery.isEmpty()) "Search here..." else searchQuery
        val color = if (searchQuery.isEmpty()) 0xFFD8D8D8.toInt() else 0xFFFFFFFF.toInt()
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal(text),
            x + searchWidth / 2,
            y + 14,
            color
        )
    }

    private fun updateSliderValue(row: RowBounds, mouseX: Double) {
        val feature = categories[row.categoryIndex].features[row.featureIndex]
        val slider = feature.sliders().getOrNull(row.settingIndex) ?: return
        val progress = ((mouseX - row.x) / row.width).coerceIn(0.0, 1.0)
        val rawValue = slider.min + (slider.max - slider.min) * progress
        val snappedValue = if (slider.step > 0.0) {
            val steps = kotlin.math.round((rawValue - slider.min) / slider.step).toInt()
            slider.min + steps * slider.step
        } else {
            rawValue
        }.coerceIn(slider.min, slider.max)
        slider.onChange(snappedValue)
    }

    private fun isPasteShortcut(input: KeyInput): Boolean {
        val pasteModifier = GLFW.GLFW_MOD_CONTROL or GLFW.GLFW_MOD_SUPER
        return input.key() == GLFW.GLFW_KEY_V && input.modifiers() and pasteModifier != 0
    }

    private fun pasteClipboardText(
        textSetting: ClickTextSetting,
        textInput: ActiveTextInput,
        currentValue: String,
        cursorIndex: Int
    ) {
        val clipboardText = MinecraftClient.getInstance().keyboard.getClipboard()
        val sanitizedText = clipboardText
            .replace(Regex("[\\r\\n\\t]+"), " ")
            .filter { !Character.isISOControl(it) }
        val availableLength = textSetting.maxLength - currentValue.length

        if (sanitizedText.isEmpty() || availableLength <= 0) {
            return
        }

        val insertedText = sanitizedText.take(availableLength)
        textSetting.onChange(
            currentValue.substring(0, cursorIndex) + insertedText + currentValue.substring(cursorIndex)
        )
        activeTextInput = textInput.copy(cursorIndex = cursorIndex + insertedText.length)
    }

    private fun getTextSetting(input: ActiveTextInput): ClickTextSetting? {
        return getTextSetting(input.categoryIndex, input.featureIndex, input.textIndex)
    }

    private fun getTextSetting(categoryIndex: Int, featureIndex: Int, textIndex: Int): ClickTextSetting? {
        return categories
            .getOrNull(categoryIndex)
            ?.features
            ?.getOrNull(featureIndex)
            ?.textSettings()
            ?.getOrNull(textIndex)
    }

    private fun cursorIndexForClick(value: String, currentCursorIndex: Int, fieldX: Int, fieldWidth: Int, clickX: Double): Int {
        if (value.isEmpty()) {
            return 0
        }

        val visibleText = visibleTextWindow(value, currentCursorIndex.coerceIn(0, value.length), fieldWidth - 8)
        val localX = (clickX - fieldX - 4).coerceAtLeast(0.0)

        visibleText.text.indices.forEach { index ->
            val charStart = textRenderer.getWidth(visibleText.text.substring(0, index))
            val charEnd = textRenderer.getWidth(visibleText.text.substring(0, index + 1))
            val midpoint = charStart + (charEnd - charStart) / 2.0
            if (localX < midpoint) {
                return visibleText.startIndex + index
            }
        }

        return (visibleText.startIndex + visibleText.text.length).coerceIn(0, value.length)
    }

    private fun visibleTextWindow(value: String, cursorIndex: Int, maxWidth: Int): VisibleTextWindow {
        if (maxWidth <= 0 || value.isEmpty()) {
            return VisibleTextWindow(0, "")
        }
        if (textRenderer.getWidth(value) <= maxWidth) {
            return VisibleTextWindow(0, value)
        }

        val cursor = cursorIndex.coerceIn(0, value.length)
        var start = cursor
        var end = cursor

        while (start > 0) {
            val candidate = value.substring(start - 1, end)
            if (textRenderer.getWidth(candidate) > maxWidth) {
                break
            }
            start--
        }

        while (end < value.length) {
            val candidate = value.substring(start, end + 1)
            if (textRenderer.getWidth(candidate) > maxWidth) {
                break
            }
            end++
        }

        return VisibleTextWindow(start, value.substring(start, end))
    }

    private fun fitTextToWidth(text: String, maxWidth: Int): String {
        if (maxWidth <= 0 || text.isEmpty()) {
            return ""
        }
        if (textRenderer.getWidth(text) <= maxWidth) {
            return text
        }

        val prefix = "..."
        if (textRenderer.getWidth(prefix) > maxWidth) {
            return ""
        }

        var trimmed = text
        while (trimmed.isNotEmpty() && textRenderer.getWidth(prefix + trimmed) > maxWidth) {
            trimmed = trimmed.drop(1)
        }
        return if (trimmed.isEmpty()) prefix else prefix + trimmed
    }

    private fun drawTooltip(context: DrawContext) {
        val tooltipData = tooltip ?: return
        val maxTextWidth = 220
        val wrapped = textRenderer.wrapLines(Text.literal(tooltipData.text), maxTextWidth)
        if (wrapped.isEmpty()) {
            return
        }

        val textWidth = wrapped.maxOf { textRenderer.getWidth(it) }
        val padding = 8
        val boxWidth = textWidth + padding * 2
        val boxHeight = wrapped.size * (textRenderer.fontHeight + 2) + padding * 2
        val maxY = height - searchHeight - 28 - boxHeight

        var x = tooltipData.anchorX
        var y = tooltipData.anchorY

        if (x + boxWidth > width - 12) {
            x = width - 12 - boxWidth
        }
        if (x < 12) {
            x = 12
        }
        if (y + boxHeight > maxY) {
            y = maxY
        }
        if (y < 12) {
            y = 12
        }

        context.fill(x, y, x + boxWidth, y + boxHeight, 0xEE1A1A1A.toInt())
        drawRoundedOutline(context, x, y, boxWidth, boxHeight, accentColor)

        var lineY = y + padding
        wrapped.forEach { line ->
            context.drawTextWithShadow(textRenderer, line, x + padding, lineY, 0xFFFFFFFF.toInt())
            lineY += textRenderer.fontHeight + 2
        }
    }

    private fun matchesSearch(feature: ClickFeature): Boolean {
        return searchQuery.isBlank() || feature.name.contains(searchQuery, ignoreCase = true)
    }

    companion object {
        private fun enabledLabel(enabled: Boolean): String = if (enabled) "ON" else "OFF"
    }

    private fun hasExpandableSettings(feature: ClickFeature): Boolean {
        return feature.toggleSettings().isNotEmpty() ||
            feature.settings().isNotEmpty() ||
            feature.sliders().isNotEmpty() ||
            feature.textSettings().isNotEmpty()
    }

    private fun isCategoryVisible(category: ClickCategory): Boolean {
        return category.name != "debug" || DebugMode.isEnabled()
    }

    private fun rowIntersectsViewport(y: Int, height: Int, clipTop: Int, clipBottom: Int): Boolean {
        return y + height > clipTop && y < clipBottom
    }

    private fun addRowBoundsIfVisible(
        type: RowType,
        categoryIndex: Int,
        featureIndex: Int,
        settingIndex: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        clipTop: Int,
        clipBottom: Int
    ) {
        if (!rowIntersectsViewport(y, height, clipTop, clipBottom)) {
            return
        }
        rowBounds += RowBounds(type, categoryIndex, featureIndex, settingIndex, x, y, width, height)
    }

    private fun drawBorder(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Int) {
        context.fill(x, y, x + width, y + 1, color)
        context.fill(x, y + height - 1, x + width, y + height, color)
        context.fill(x, y, x + 1, y + height, color)
        context.fill(x + width - 1, y, x + width, y + height, color)
    }

    private fun drawRoundedOutline(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawBorder(context, x, y, width, height, color)
        context.fill(x + 1, y + 1, x + width - 1, y + 2, color)
        context.fill(x + 1, y + height - 2, x + width - 1, y + height - 1, color)
    }
}
