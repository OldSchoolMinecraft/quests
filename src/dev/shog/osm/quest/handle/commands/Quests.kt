package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.sendMultiline
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

val VIEW_QUESTS = { osmQuests: OsmQuests ->
    CommandExecutor { sender, cmd, label, args ->
        if (sender is Player) {
            osmQuests.quests.forEach { quest ->
                val message = if (quest.isComplete(sender)) {
                    MessageHandler.getMessage(
                        "commands.view-quest.complete-quest",
                        quest.questName, quest.rewardString,
                        buildString {
                            quest.tasks.forEach { task ->
                                append(MessageHandler.getMessage(
                                    "commands.view-quest.complete-task",
                                    task.name, task.getStatusForPlayer(sender)
                                ))
                            }
                        }
                    )
                } else {
                    MessageHandler.getMessage(
                        "commands.view-quest.uncomplete-quest",
                        quest.questName, quest.rewardString,
                        buildString {
                            quest.tasks.forEach { task ->
                                if (task.isComplete(sender)) {
                                    append(MessageHandler.getMessage(
                                        "commands.view-quest.complete-task",
                                        task.name, task.getStatusForPlayer(sender)
                                    ))
                                } else {
                                    append(MessageHandler.getMessage(
                                        "commands.view-quest.uncomplete-task",
                                        task.name, task.getStatusForPlayer(sender)
                                    ))
                                }
                            }
                        }
                    )
                }

                sender.sendMultiline(message)
            }
        } else {
            sender.sendMessage("You can't do this as console!")
        }

        true
    }
}