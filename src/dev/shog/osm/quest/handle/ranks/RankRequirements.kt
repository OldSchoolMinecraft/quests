package dev.shog.osm.quest.handle.ranks

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.handle.ranks.user.User
import org.bukkit.entity.Player

/**
 * @param xp The amount of XP.
 * @param balance The amount of money.
 * @param timeHr The time required in hours.
 */
class RankRequirements(val xp: Long, val balance: Double, val timeHr: Long) {
    /**
     * If [player] meets the requirements.
     */
    fun hasRequirements(player: Player): Boolean {
        val user = User.getUser(player.name)

        // TODO check for time
        return user.xp >= xp && Economy.hasEnough(player.name, balance)
    }
}