package impl.partycommands

interface PartyCommand {
    val name: String
    fun execute(sender: String, args: List<String>): String
}