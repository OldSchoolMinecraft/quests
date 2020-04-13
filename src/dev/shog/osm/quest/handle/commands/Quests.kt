package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import dev.shog.osm.quest.sendMultiline
import org.bukkit.ChatColor
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

private fun getQuestNameLink(quest: Quest, complete: Boolean) =
    if (complete)
        "commands.quests.${if (quest.donor) "supporter-" else ""}complete-quest"
    else "commands.quests.${if (quest.donor) "supporter-" else ""}uncomplete-quest"

private fun getTaskNameLink(complete: Boolean) =
    if (complete)
        "commands.quests.quest-viewer.complete-task"
    else "commands.quests.quest-viewer.uncomplete-task"

/**
 * View your quests.
 */
val VIEW_QUESTS = { osmQuests: OsmQuests ->
    CommandExecutor { sender, cmd, label, args ->
        val player = sender as? Player

        if (player == null) {
            sender.sendMessage("You must be a player for this.")
            return@CommandExecutor true
        }

        if (args.size == 1) {
            val quest = osmQuests.quests.singleOrNull { quest -> quest.questName.equals(args[0], true) }

            if (quest == null) {
                sender.sendMessage(MessageHandler.getMessage("commands.quests.invalid-quest"))
            } else {
                val tasks = quest.tasks

                val taskList = buildString {
                    tasks.map { task ->
                            MessageHandler.getMessage(
                                getTaskNameLink(task.isComplete(sender)),
                                task.name,
                                task.getStatusString(sender)
                            )
                        }.forEach { append(it) }
                }

                sender.sendMultiline(
                    MessageHandler.getMessage(
                        "commands.quests.quest-viewer.${if (quest.isComplete(sender)) "complete" else "uncomplete"}-quest",
                        quest.questName,
                        quest.rewardString
                    ) + taskList
                )
            }

            return@CommandExecutor true
        }

        var message = MessageHandler.getMessage("commands.quests.header")

        message += osmQuests.quests.joinToString { quest ->
            MessageHandler.getMessage(
                getQuestNameLink(quest, quest.isComplete(sender)),
                quest.questName
            )
        }

        sender.sendMultiline(message)

        true
    }
}