package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.XpHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player

class XpRewardingQuest(
    name: String,
    tasks: List<QuestTask>,
    osmQuests: OsmQuests,
    val xpReward: Long,
    override val rewardString: String
) : Quest(name, tasks, osmQuests) {
    override fun onComplete(player: Player) {
        val current = XpHandler.xp[player.name.toLowerCase()] ?: 0

        XpHandler.xp[player.name.toLowerCase()] = current + xpReward

        // TODO add to messages.yml
        player.sendMessage("You have completed $questName quest and have retrieved $xpReward")
    }

    override val identifier: String = "REWARD_XP"
}