package clickgui

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
    val onClick: () -> Unit = {},
    var expanded: Boolean = false
)
