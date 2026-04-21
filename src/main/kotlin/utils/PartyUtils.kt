package utils

object PartyUtils {
    val partyMembers = mutableSetOf<String>()

    fun processCharForPartyInfo(cleanText: String) {
        Regex("(?:\\[.*?] )?(\\w+) joined the party\\.").find(cleanText)?.let {
            partyMembers.add(it.groupValues[1])
        }

        Regex("(?:\\[.*?] )?(\\w+) (?:has left|has been removed|was removed) from the party\\.").find(cleanText)?.let {
            partyMembers.remove(it.groupValues[1])
        }

        val disbandMessages = listOf("has disbanded the party!", "You left the party.", "You are not currently in a party.", "The party was disbanded")
        if (disbandMessages.any { cleanText.contains(it) }) {
            partyMembers.clear()
        }

        if (cleanText.startsWith("Party Leader:") || cleanText.startsWith("Party Members:")) {
            cleanText.substringAfter(":").split(" ●").forEach { segment ->
                val name = segment.replace("●", "").trim().split(" ").last()
                if (name.isNotEmpty()) partyMembers.add(name)
            }
        }
    }

    fun findMember(input: String): String {
        return partyMembers.find { it.contains(input, ignoreCase = true) } ?: input
    }
}