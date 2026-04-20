package commands


class ForcefemCommand : PartyCommand {
    override val name = "forcefem"

    override fun execute(args: List<String>): String {
        if (args.isEmpty()) return "Usage: !forcefem <player>"

        val target = args[0]
        val amount = listOf("100mg", "1mg", "50mg", "1000mg", "500mg")
        val responses = listOf("Laced $target's drink with $amount of estrogen!", "No","$target is already beyond that stage.")

        if ((1..20).random() == 5) {
            return "DIVINE INTERVENTION!!! $target HAS BEEN TURNED INTO THE TRUE EMBODIMENT OF ESTROGEN!!!!!"
        }

        return "forcefem: ${responses.random()}"
    }
}
