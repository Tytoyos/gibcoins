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
            val target = args[0].replaceFirstChar { c -> c.uppercaseChar() }
            val femRoll = (1..15).random()
            val amount = listOf("100mg", "1mg", "50mg", "1000mg", "500mg").random()
            val responses = listOf(
                "Laced $target's drink with $amount of estrogen!", "No.", "$target is already beyond that stage.",
                "Secretly putting a syringe with $amount of estrogen in $target's ass!!", "Practicing feminine voice with $target.",
                "Picking out feminine clothes with $target.", "$target just got assigned thigh-highs by fate.", "Convincing $target to \"ironically\" wear a maid outfit.",
                "Teaching $target how to pose for selfies.", "$target now says 'haiii :3' unironically.", "Introducing $target to eyeliner.", "$target got caught enjoying skirts a little too much.",
                "Sending $target directly to the blahaj containment chamber.", "Making $target watch makeup tutorials at 2x speed.", "$target now owns at least one choker.",
                "Forcing $target to pick a Sanrio character.", "$target has become suspiciously good at eyeliner.", "$target bought programmer socks 'as a joke'.",
                "$target has become emotionally attached to a Blåhaj."
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

            val responsesMeowcwazy = listOf("Meow.", "Putting cat ears and a tail plug on meowcwazy.", "Buying EVEN MORE Blåhajs for meowcwazy.")

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