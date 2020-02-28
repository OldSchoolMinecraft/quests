package dev.shog.osm.quest

import java.io.File

val DIR by lazy {
    val file = File("questData")

    if (!file.exists())
        file.mkdirs()

    file
}