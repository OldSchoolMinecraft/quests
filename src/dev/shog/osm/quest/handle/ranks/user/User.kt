package dev.shog.osm.quest.handle.ranks.user

import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.SqlHandler
import dev.shog.osm.quest.handle.ranks.Ladder
import dev.shog.osm.util.api.data.DataManager
import org.bukkit.entity.Player
import ru.tehkode.permissions.bukkit.PermissionsEx
import java.util.concurrent.ConcurrentHashMap

/**
 * A user.
 *
 * @param user The username
 * @param rank The ladder position of the rank.
 * @param xp The user's XP.
 */
class User internal constructor(user: String, rank: Int = 0, xp: Long = 0) {
    companion object {
        private val USER_CACHE = ConcurrentHashMap<String, User>()

        /**
         * Get a user by their [username].
         *
         * If they don't exist, create an empty [User].
         */
        fun getUser(username: String): User {
            if (USER_CACHE.contains(username.toLowerCase()))
                return USER_CACHE[username.toLowerCase()]!!

            val prepared = SqlHandler.getConnection()
                .prepareStatement("SELECT * FROM quests WHERE username = ?")

            prepared.setString(1, username)

            val rs = prepared.executeQuery()

            val user = if (!rs.next()) {
                val newPrepared = SqlHandler.getConnection()
                    .prepareStatement("INSERT INTO quests (username) VALUES (?)")

                newPrepared.setString(1, username)

                newPrepared.executeUpdate()

                User(username, 0, 0)
            } else {
                User(username, rs.getInt("rank"), rs.getLong("xp"))
            }

            USER_CACHE[username.toLowerCase()] = user
            return user
        }
    }

    /**
     * The user's username.
     */
    val username = user

    /**
     * The user's rank.
     * This sets in the database once the field's been set.
     */
    var rank = rank
        set(value) {
            val prepared = SqlHandler.getConnection()
                .prepareStatement("UPDATE `quests` SET `rank`=? WHERE `username`=? ")

            prepared.setInt(1, value)
            prepared.setString(2, username)

            prepared.executeUpdate()

            field = value
        }

    /**
     * The user's XP.
     * This sets in the database once the field's been set.
     */
    var xp = xp
        set(value) {
            val prepared = SqlHandler.getConnection()
                .prepareStatement("UPDATE `quests` SET `xp`=? WHERE username=?")

            prepared.setLong(1, value)
            prepared.setString(2, username)

            prepared.execute()

            field = value
        }

    /**
     * Get a user's playtime from OSMUtil
     */
    fun getPlayTime(): Long {
        val user = DataManager.getUserData(username)
            ?: throw Exception("Invalid user name when getting hours.")

        return user.playTime
    }

    /**
     * Rank up a user, and set their new prefix.
     */
    fun rankUp(player: Player) {
        val rank = Ladder.getUpperRank(this)
        val oldRank = Ladder.getRank(this.rank)

        if (rank != null) {
            val req = rank.requirements

            if (req.hasRequirements(player)) {
                player.sendMessage(MessageHandler.getMessage("ranks.rank-up", oldRank?.name, rank.name))

                this.rank += 1
                PermissionsEx.getPermissionManager().getUser(player).setPrefix(rank.prefix, "world")
            } else player.sendMessage(MessageHandler.getMessage("ranks.not-meet"))
        } else {
            player.sendMessage(MessageHandler.getMessage("ranks.max-rank"))
        }
    }
}