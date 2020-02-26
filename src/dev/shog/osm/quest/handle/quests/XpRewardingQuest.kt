package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.Quests
import dev.shog.osm.quest.handle.XpHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player

class XpRewardingQuest(name: String, tasks: List<QuestTask>, quests: Quests, val xpReward: Long)
    : Quest(name, tasks, quests) {
    override fun onComplete(player: Player) {
        val current = XpHandler.xp[player.name.toLowerCase()] ?: 0

        XpHandler.xp[player.name.toLowerCase()] = current + xpReward

        // TODO add to messages.yml
        player.sendMessage("You have completed $questName quest and have retrieved $xpReward")
    }
}