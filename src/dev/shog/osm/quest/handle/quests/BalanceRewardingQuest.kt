package dev.shog.osm.quest.handle.quests

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player

class BalanceRewardingQuest(
    name: String, tasks:
    List<QuestTask>,
    osmQuests: OsmQuests,
    val balReward: Double,
    override val rewardString: String
) : Quest(name, tasks, osmQuests) {
    override fun onComplete(player: Player) {
        Economy.add(player.name, balReward)

        // TODO add to messages.yml
        player.sendMessage("You have completed $questName quest and have retrieved $balReward")
    }

    override val identifier: String = "BALANCE_REWARD"
}