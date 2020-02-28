package dev.shog.osm.quest.handle

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.DIR
import java.io.File

object XpHandler {
    private val file by lazy {
        val fi = File(DIR.path + File.separator + "xp.json")

        if (!fi.exists()) {
            fi.createNewFile()
            fi.outputStream().write("{}".toByteArray())
        }

        fi
    }

    val xp: HashMap<String, Long>

    init {
        val mapper = ObjectMapper()

        xp = mapper.readValue(
            file,
            mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Long::class.java)
        )
    }
}