package clickgui

data class ClickToggleSetting(
    val name: String,
    val enabled: () -> Boolean,
    val onToggle: () -> Unit
)

data class ClickSliderSetting(
    val name: String,
    val min: Double,
    val max: Double,
    val step: Double = 0.1,
    val value: () -> Double,
    val onChange: (Double) -> Unit,
    val formatter: (Double) -> String = { it.toString() },
    val insertAfterToggleName: String? = null
)

data class ClickTextSetting(
    val name: String,
    val buttonText: () -> String,
    val value: () -> String,
    val onChange: (String) -> Unit,
    val placeholder: String = "",
    val maxLength: Int = 256
)

data class ClickCategory(
    val name: String,
    val features: List<ClickFeature>,
    var expanded: Boolean = false
)

data class ClickFeature(
    val name: String,
    val description: String,
    val status: (() -> String)? = null,
    val settings: (() -> List<Pair<String, String>>) = { emptyList() },
    val toggleSettings: (() -> List<ClickToggleSetting>) = { emptyList() },
    val sliders: (() -> List<ClickSliderSetting>) = { emptyList() },
    val textSettings: (() -> List<ClickTextSetting>) = { emptyList() },
    val onClick: () -> Unit = {},
    var expanded: Boolean = false
)
