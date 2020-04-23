package dev.shog.osm.quest.handle

import kong.unirest.Unirest
import org.bukkit.entity.Player
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

object Donor {
    private val cache = ConcurrentHashMap<String, Boolean>()

    /**
     * Check a [player] to see if they're able for donor status.
     */
    fun check(player: Player): Boolean {
        if (player.isOp)
            return true

        return check(player.name)
    }

    /**
     * Check a username string if they're able for donor status.
     */
    fun check(user: String): Boolean {
        if (cache.contains(user))
            return cache[user]!!

        val check = Unirest.get("https://www.oldschoolminecraft.com/donators.php")
            .asStringAsync()
            .handleAsync { t, _ -> JSONObject(t.body).getJSONArray("donators") }
            .join().contains(user)

        cache[user] = check

        return check
    }
}