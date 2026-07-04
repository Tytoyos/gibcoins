package impl.`fun`.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class SubCommand : PartyCommand {
    override val name = "sub"
    override fun execute(sender: String, args: List<String>): String {

        val user = sender.replaceFirstChar { c -> c.uppercase() }
        val personalRoll = (1..6).random()

        if (args.isEmpty()) {
            val submit = listOf("I swear I was a good girl (｡>﹏<)", "I swear I was a good boy (｡>﹏<)", "I love getting tied down (˶˃⤙˂˶)", "PLEASE PUT ME ON A LEASH!!", "I'll be good, I promise!",
                "I NEEEEEEEEED ITTT!11!!1")


            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage(submit.random())
                }
            }

            if (sender == "Kaiine".lowercase() && personalRoll == 3) {

                val kaiineSub = listOf("meowcwazy is dominating me")

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(kaiineSub.random())
                    }
                }
            }
        }

        else {
            val target = args[0].replaceFirstChar { c -> c.uppercase() }
            val sub = listOf("Asking $target for headpats. (˶˃ᆺ˂˶)")


            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage(sub.random())
                }
            }

        }
        return ""
    }
}
