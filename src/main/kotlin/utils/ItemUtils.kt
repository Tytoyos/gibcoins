package utils

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import java.util.Locale
import kotlin.jvm.optionals.getOrNull

object ItemUtils {
    val ItemStack.customData: NbtCompound
        get() = getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt()

    val ItemStack.lore: List<String>
        get() = getOrDefault(DataComponentTypes.LORE, LoreComponent.DEFAULT).styledLines().map { it.string }

    val ItemStack.itemUUID: String
        get() = customData.getString("uuid").getOrNull() ?: ""

    val ItemStack.skyblockEnchantments: Map<String, Int>
        get() {
            if (isEmpty) {
                return emptyMap()
            }

            val enchantments = customData.getCompound("enchantments").getOrNull() ?: return emptyMap()
            return enchantments.keys.associateWith { key ->
                enchantments.getInt(key, 0)
            }.filterValues { level ->
                level > 0
            }
        }

    val ItemStack.skyblockId: String
        get() {
            if (isEmpty) {
                return ""
            }

            val itemData = customData
            var sbItemID: String? = null

            if (itemData.contains("id")) {
                sbItemID = itemData.getString("id").getOrNull()?.replace(":", "-")
            }

            return sbItemID.orEmpty()
        }

    fun ItemStack.getSkyblockEnchantLevel(vararg enchantIds: String): Int {
        if (enchantIds.isEmpty()) {
            return 0
        }

        val normalizedIds = enchantIds.mapTo(mutableSetOf(), ::normalizeSkyblockKey)
        return skyblockEnchantments.entries.maxOfOrNull { (id, level) ->
            if (normalizeSkyblockKey(id) in normalizedIds) level else 0
        } ?: 0
    }

    fun ItemStack.hasSkyblockEnchantAtLeast(enchantId: String, minLevel: Int): Boolean {
        return getSkyblockEnchantLevel(enchantId) >= minLevel
    }

    fun ItemStack.hasSkyblockEnchantAtLeast(enchantIds: Collection<String>, minLevel: Int): Boolean {
        return getSkyblockEnchantLevel(*enchantIds.toTypedArray()) >= minLevel
    }

    private fun normalizeSkyblockKey(key: String): String {
        return key
            .lowercase(Locale.US)
            .replace(" ", "_")
            .replace("-", "_")
    }
}
