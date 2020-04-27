package dev.shog.osm.quest.handle.quests

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player

/**
 * A quest that rewards money.
 *
 * @param name The name of the quest.
 * @param tasks The tasks you must complete to finish the quest.
 * @param osmQuests The OSM quests instance.
 * @param balReward The balance reward the player is rewarded with when finishing.
 * @param rewardString The reward string. This could be "500 dollaryoos"
 */
class BalanceRewardingQuest(
    name: String,
    tasks: List<QuestTask>,
    osmQuests: OsmQuests,
    donor: Boolean,
    val balReward: Double,
    override val rewardString: String
) : Quest(name, tasks, donor, osmQuests) {
    /**
     * On complete give [player] $[balReward]
     */
    override fun onComplete(player: Player) {
        Economy.add(player.name, balReward)

        player.sendMessage(MessageHandler.getMessage("quests.quest-complete", questName, rewardString))
    }

    override val identifier: String = "BALANCE_REWARD"
}