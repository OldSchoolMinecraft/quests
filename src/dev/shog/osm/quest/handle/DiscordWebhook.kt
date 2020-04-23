package dev.shog.osm.quest.handle

import kong.unirest.Unirest
import kong.unirest.json.JSONObject

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 */
class DiscordWebhook(private val webhookUrl: String) {
    /**
     * Send a message through a file. This avoids the 2000 character limit.
     *
     * @param message A small message.
     * @param file A large message.
     * @param fileName The file's name.
     */
    fun sendBigMessage(message: String, file: String, fileName: String = "content.txt"): Boolean {
        if (message.length > 2000)
            return false

        val bytes = file.toByteArray()

        return Unirest.post(webhookUrl)
            .field("payload_json", getJsonObject().put("content", message))
            .field("file", bytes, fileName)
            .asEmpty()
            .isSuccess
    }

    /**
     * Send a message through the webhook.
     */
    fun sendMessage(message: String): Boolean {
        if (message.length > 2000)
            return false

        val obj = getJsonObject()
            .put("content", message)

        return Unirest.post(webhookUrl)
            .header("Content-Type", "application/json")
            .body(obj.toString())
            .asEmpty()
            .isSuccess
    }

    /**
     * Build the JSON object.
     */
    private fun getJsonObject(): JSONObject =
        JSONObject()
            .put("username", "OSMQuests Logger")
            .put("tts", false)
}