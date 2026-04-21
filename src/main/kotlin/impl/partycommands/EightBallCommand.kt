package impl.partycommands

class EightBallCommand : PartyCommand {
    override val name = "8ball"
    private val responses = listOf("Yes", "No", "Maybe", "Ask again later", "Definitely!")

    override fun execute(args: List<String>): String {
        return responses.random()
    }
}