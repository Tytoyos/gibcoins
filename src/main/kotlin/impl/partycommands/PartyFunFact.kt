package impl.partycommands

import net.minecraft.client.MinecraftClient

class PartyFunFact : PartyCommand {
    override val name = "funfact"

    override fun execute(sender: String, args: List<String>): String {
        val fact = listOf("Kaiine is everyone's pet ❤","The rift sucks.","About 35,82% of currently active players would lick Diana's toes.")

        Thread {
            Thread.sleep(1000)
            MinecraftClient.getInstance().networkHandler?.sendChatCommand("pc ${fact.random()}")
        }

        return ""
    }
}