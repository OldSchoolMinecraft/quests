package dev.shog.osm.quest.handle

import java.util.concurrent.ConcurrentHashMap

object XpHandler {
    val xp = ConcurrentHashMap<String, Long>()
}