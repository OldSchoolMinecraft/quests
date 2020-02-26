package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.Quests
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

val VIEW_QUESTS = { quests: Quests ->
    CommandExecutor { sender, cmd, label, args ->
        if (sender is Player) {
            quests.quests.forEach { quest ->
                if (quest.isComplete(sender)) {
                    sender.sendMessage("COMPLETE: ${quest.questName}")
                } else {
                    sender.sendMessage("NOT COMPLETE: ${quest.questName}")
                    sender.sendMessage("MISSING : ${quest.getMissingTasks(sender)}")
                }
            }
        } else {
            sender.sendMessage("You can't do this as console!")
        }

        true
    }
}