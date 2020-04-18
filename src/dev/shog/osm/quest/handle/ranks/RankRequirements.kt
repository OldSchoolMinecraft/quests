package dev.shog.osm.quest.handle.ranks

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.ranks.user.User
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

/**
 * @param xp The amount of XP.
 * @param balance The amount of money.
 * @param timeHr The time required in hours.
 */
class RankRequirements(val xp: Long, val balance: Double, val timeHr: Long) {
    /**
     * If [player] meets the requirements.
     */
    fun hasRequirements(player: Player): String? {
        val user = User.getUser(player.name)

        return when {
            user.xp < xp ->
                MessageHandler.getMessage("ranks.not-meet.no-xp", xp - user.xp)

            TimeUnit.MILLISECONDS.toHours(user.getPlayTime()) < timeHr ->
                MessageHandler.getMessage("ranks.not-meet.no-time", timeHr - TimeUnit.MILLISECONDS.toHours(user.getPlayTime()))

            !Economy.hasEnough(player.name, balance) ->
                MessageHandler.getMessage("ranks.not-meet.no-bal", balance - Economy.getMoney(player.name))

            else -> null
        }
    }
}