package Service

import business.Note
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codebot.application.ConfigData
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li
object ServiceManager {

    // Service calls for notes
    fun getNotesData(): MutableList<Note> {
        val dataString = getData(ConfigData.SERVER_NOTES_GET_ADDRESS)
        return Json.decodeFromString<MutableList<Note>>(dataString)
    }

    fun updateNotesData(notes: MutableList<Note>) {
        deleteNotesData()
        postNotesData(notes)
    }

    private fun postNotesData(notes: MutableList<Note>) {
        val string = Json.encodeToString(notes)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(ConfigData.SERVER_NOTES_POST_ADDRESS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun deleteNotesData() {
        deleteData(ConfigData.SERVER_NOTES_DELETE_ADDRESS)
    }


    // Service calls for groups
    fun getGroupsData(): MutableList<String> {
        val dataString = getData(ConfigData.SERVER_GROUPS_GET_ADDRESS)
        return Json.decodeFromString<MutableList<String>>(dataString)

    }

    fun updateGroupsData(groups: MutableList<String>) {
        deleteGroupsData()
        postGroupsData(groups)
    }

    private fun postGroupsData(groups: MutableList<String>) {
        val string = Json.encodeToString(groups)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(ConfigData.SERVER_GROUPS_POST_ADDRESS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun deleteGroupsData() {
        deleteData(ConfigData.SERVER_GROUPS_DELETE_ADDRESS)
    }


    // Service calls for recently deleted notes
    fun getRecentlyDeletedNotesData(): MutableList<Note> {
        val dataString = getData(ConfigData.SERVER_RECENTLY_DELETE_NOTES_GET_ADDRESS)
        return Json.decodeFromString<MutableList<Note>>(dataString)

    }

    fun updateRecentlyDeletedNotesData(notes: MutableList<Note>) {
        deleteRecentlyDeletedNotesData()
        postRecentlyDeletedNotesData(notes)
    }

    private fun postRecentlyDeletedNotesData(notes: MutableList<Note>) {
        val string = Json.encodeToString(notes)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(ConfigData.SERVER_RECENTLY_DELETE_NOTES_POST_ADDRESS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun deleteRecentlyDeletedNotesData() {
        deleteData(ConfigData.SERVER_RECENTLY_DELETE_NOTES_DELETE_ADDRESS)
    }


    // Helper functions
    private fun getData(requestUri: String): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(requestUri))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    private fun deleteData(requestUri: String) {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(requestUri))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}