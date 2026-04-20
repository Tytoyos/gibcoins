package commands


class ForcefemCommand : PartyCommand {
    override val name = "forcefem"

    override fun execute(args: List<String>): String {
        if (args.isEmpty()) return "Usage: !forcefem <player>"

        val target = args[0]
        val femroll = (1..10).random()
        val amount = listOf("100mg", "1mg", "50mg", "1000mg", "500mg")
        val responses = listOf("Laced $target's drink with $amount of estrogen!", "No","$target is already beyond that stage.",
                                "Secretly putting a syringe with $amount of estrogen in $target's ass!!")

        val responseskaiine = listOf("ESTROGEN OVERLOAD!!!!", "UH OH, I ACCIDENTALLY STABBED HIS HEART. HE DED")

        if (target.lowercase() == "kaiine" && femroll == 5) {
            return responseskaiine.random()
        }

        // add easter egg for moocwazy

        if ((1..20).random() == 5) {
            return "DIVINE INTERVENTION!!! $target HAS BEEN TURNED INTO THE TRUE EMBODIMENT OF ESTROGEN!!!!!"
        }

        return responses.random()
    }
}