package dev.shog.osm.quest.handle

import org.json.JSONObject

/**
 * The message handler.
 */
object MessageHandler {
    private val data: JSONObject by lazy {
        val reader = MessageHandler::class.java.getResourceAsStream("/messages.json")

        JSONObject(String(reader.readBytes()))
    }

    /**
     * Get a message. This uses "object.object.string"
     */
    fun getMessage(message: String): String {
        val split = message.split(".").toMutableList()
        val msg = split.last()
        split.removeAt(split.size - 1)

        var pointer = data
        for (spl in split) {
            pointer = pointer.getJSONObject(spl)
        }

        return pointer.getString(msg)
    }

    /**
     * Get a message and input arguments into it.
     */
    fun getMessage(message: String, vararg args: Any?): String {
        var newString = getMessage(message)

        args.forEachIndexed { i, arg ->
            if (newString.contains("{$i}"))
                newString = newString.replace("{$i}", arg?.toString() ?: "null")
        }

        return newString
    }
}