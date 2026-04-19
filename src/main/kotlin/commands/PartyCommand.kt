package commands

interface PartyCommand {
    val name: String
    fun execute(args: List<String>): String?
}