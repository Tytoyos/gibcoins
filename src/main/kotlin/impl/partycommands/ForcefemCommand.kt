package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class ForcefemCommand : PartyCommand {
    override val name = "forcefem"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) {
            return "Usage: !forcefem <player>"
        } else {
            val target = args[0]
            val femRoll = (1..10).random()
            val amount = listOf("100mg", "1mg", "50mg", "1000mg", "500mg").random()
            val responses = listOf(
                "Laced $target's drink with $amount of estrogen!", "No.", "$target is already beyond that stage.",
                "Secretly putting a syringe with $amount of estrogen in $target's ass!!"
            )

            val responsesKaiine = listOf("ESTROGEN OVERLOAD!!!!", "UH OH, I ACCIDENTALLY STABBED HIS HEART. HE DED")

            if (target.lowercase() == "kaiine" && femRoll == 5) {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(responsesKaiine.random())
                    }
                }
                return ""
            }

            // add easter egg for meowcwazy

            if ((1..20).random() == 5) {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage("DIVINE INTERVENTION!!! ${target.uppercase()} HAS BEEN TURNED INTO THE TRUE EMBODIMENT OF ESTROGEN!!!!!")
                    }
                }
                return ""
            }
            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage(responses.random())
                }
            }
            return ""
        }
    }
}