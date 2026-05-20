package impl.`fun`.partycommands

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
            val femRoll = (1..15).random()
            val amount = listOf("100mg", "1mg", "50mg", "1000mg", "500mg").random()
            val responses = listOf(
                "Laced $target's drink with $amount of estrogen!", "No.", "$target is already beyond that stage.",
                "Secretly putting a syringe with $amount of estrogen in $target's ass!!", "Practicing feminine voice with $target.",
                "Picking out feminine clothes with $target."
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

            val responsesRiley = listOf("...wait a second.", "Felt like switching up my game and turned CatgirlRiley into a MAN instead!!")

            if (target.lowercase() == "catgirlriley" && femRoll == 5) {
                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(responsesRiley.random())
                    }
                }
                return ""
            }

            val responsesMeowcwazy = listOf("Meow.", "Putting cat ears and a tail plug on meowcwazy.")

            if (target.lowercase() == "meowcwazy" && femRoll == 5) {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(responsesMeowcwazy.random())
                    }
                }
                return ""
            }

            if ((1..25).random() == 5) {

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