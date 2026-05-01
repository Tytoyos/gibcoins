package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class ShitterCheck : PartyCommand {
       override val name = "shittercheck"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) {
            return "Usage: !shittercheck <player>"
        } else {


            val target = args[0]
            val answer = listOf(
                "$target is a shitter.",
                "$target is not a shitter.",
                "I'm not sure on this one.",
                "STAY AWAY FROM THIS GUY!!!",
                "$target saved my cat from a burning building!",
                "Might wanna checkout $target's hard drive.",
                "I'm not saying he IS a shitter, but i also won't say that he ISN'T one."
            )
            val answerRego = listOf(
                "Regocike? More like Shittercike!", "*redacted*",
                "Definitely checkout this guy's hard drive.", "I don't know, ask moocwazy."
            )
            val answerMeowcwazy = "This player is very pro, trust frfr (not paid)."      //variable, so its expandable
            val answerKaiine = "Kaiine is a good boy!! Not a shitter :3"                 //variable, so its expandable

            if (target.lowercase() == "regocike") {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(answerRego.random())
                    }
                }
                return ""
            }

            if (target.lowercase() == "meowcwazy") {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(answerMeowcwazy)
                    }
                }
                return ""
            }

            if (target.lowercase() == "kaiine") {

                CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                    MinecraftClient.getInstance().execute {
                        MinecraftClient.getInstance().networkHandler?.sendChatMessage(answerKaiine)
                    }
                }
                return ""
            }
            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage(answer.random())
                }
            }
            return ""
        }
    }
}
