package dev.shog.osm.quest.handle

import com.sun.org.apache.xpath.internal.operations.Bool
import dev.shog.osm.util.api.OsmApi
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

object Donor {
    private val cache = ConcurrentHashMap<String, Boolean>()

    fun check(player: Player): Boolean {
        if (player.isOp)
            return true

        return check(player.name)
    }

    fun check(user: String): Boolean {
        if (cache.contains(user))
            return cache[user]!!

        val check = OsmApi
            .isDonor(user)
            .join()

        cache[user] = check

        return check
    }
}