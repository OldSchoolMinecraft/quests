package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import dev.shog.osm.quest.handle.ranks.user.User
import org.bukkit.entity.Player

/**
 * A quest that rewards money.
 *
 * @param name The name of the quest.
 * @param tasks The tasks you must complete to finish the quest.
 * @param osmQuests The OSM quests instance.
 * @param xpReward The XP reward the player is rewarded with when finishing.
 * @param rewardString The reward string. This could be "25 experiences"
 */
class XpRewardingQuest(
    name: String,
    tasks: List<QuestTask>,
    osmQuests: OsmQuests,
    donor: Boolean,
    val xpReward: Long,
    override val rewardString: String
) : Quest(name, tasks, donor, osmQuests) {
    /**
     * On complete give [player] [xpReward] xp
     */
    override fun onComplete(player: Player) {
        val user = User.getUser(player.name)

        user.xp += xpReward

        player.sendMessage(MessageHandler.getMessage("quests.quest-complete", questName, rewardString))
    }

    override val identifier: String = "REWARD_XP"
}