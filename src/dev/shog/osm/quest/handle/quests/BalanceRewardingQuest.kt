package dev.shog.osm.quest.handle.quests

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.Quests
import dev.shog.osm.quest.handle.XpHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player

class BalanceRewardingQuest(name: String, tasks: List<QuestTask>, quests: Quests, val balReward: Double)
    : Quest(name, tasks, quests) {
    override fun onComplete(player: Player) {
        Economy.add(player.name, balReward)

        // TODO add to messages.yml
        player.sendMessage("You have completed $questName quest and have retrieved $balReward")
    }
}