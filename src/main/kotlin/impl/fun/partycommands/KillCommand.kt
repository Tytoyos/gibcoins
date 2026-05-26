package impl.`fun`.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class KillCommand : PartyCommand {
    override val name = "kill"
    override fun execute(sender: String, args: List<String>): String {
        val target = args[0].replaceFirstChar { c -> c.uppercase() }
        val user = sender.replaceFirstChar { c -> c.uppercase() }
        val kill = listOf(
            "$target was stabbed.", "$target had their head smashed by a hammer.",
            "$target was dissolved in acid.", "WOAH ${sender.uppercase()} FORGOT WHAT THEY CAME FOR AND ENDED UP SUCKING ${target.uppercase()}'S TOES!!",
            "$target sat on $target's face for too long...", "Nuked $target.", "$target was choked by $user's thunderclappingly enormous thighs!", "$target slipped on a banana peel and exploded instantly.",
            "$target was crushed by a comically large spoon.", "$target opened Twitter and never recovered.", "$target got hit by a tactical frying pan.",
            "$target was beaten to death with a pool noodle.", "$target got hit by an anvil", "$target was sent to JakeLegend...", "$target got hit with the forbidden backshot technique.",
            "$target tried to fight a goose...", "$target was publicly executed", "$target was beaten with hammers by 14 angry dwarves.", "$target got vaporized by a microwaved fork.",
            "$target was sacrificed for better drops.", "$target made a 67 joke and fucking imploded", "$target was smothered by $user's astronomically oversized dumptruck.", "FRICK YOU JAKELEGEND!",
            "WOAH!! $user got distracted and ended up meowing for $target's attention instead!!", "${sender.uppercase()} TRIED TO KILL ${target.uppercase()} BUT SOMEHOW ENDED UP GETTING CUDDLED AGGRESSIVELY INSTEAD!!"
            )

        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().networkHandler?.sendChatMessage(kill.random())
            }
        }
        return ""
    }
}