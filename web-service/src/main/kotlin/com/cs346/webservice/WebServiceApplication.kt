package com.cs346.webservice

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.nio.charset.StandardCharsets

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

@SpringBootApplication
class WebServiceApplication

fun main(args: Array<String>) {
	try {
		runApplication<WebServiceApplication>(*args)
	} catch (e: Exception) {
		e.printStackTrace()
	}
}

@RestController
@RequestMapping("/notes")
class NoteResource() {

	val resource = ClassPathResource("cs346-notes-app-key.json").inputStream
	val credentials: GoogleCredentials = GoogleCredentials.fromStream(resource)

	val storage: Storage = StorageOptions.newBuilder().setCredentials(credentials).build().service
	val id: BlobId = BlobId.of("spring-bucket-aydna66", "notes.json")

	@PostMapping("/add")
	fun addNotes(@RequestBody jsonData: String) {

		val info = BlobInfo.newBuilder(id).build()

		storage.create(info, jsonData.toByteArray())
	}

	@GetMapping("/get")
	fun getNotes(): String? {
		val blob: Blob? = storage.get(id)
		val fileContent = blob?.getContent()
		val ret = fileContent?.let { String(it, StandardCharsets.UTF_8) }
		return ret
	}

	@DeleteMapping("/delete")
	fun deleteNotes() {
		storage.delete(id)
	}
}

@RestController
@RequestMapping("/groups")
class GroupsResource() {

	val resource = ClassPathResource("cs346-notes-app-key.json").inputStream
	val credentials: GoogleCredentials = GoogleCredentials.fromStream(resource)

	val storage: Storage = StorageOptions.newBuilder().setCredentials(credentials).build().service
	val id: BlobId = BlobId.of("spring-bucket-aydna66", "groupNames.json")

	@PostMapping("/add")
	fun addGroupNames(@RequestBody jsonData: String) {

		val info = BlobInfo.newBuilder(id).build()

		storage.create(info, jsonData.toByteArray())
	}

	@GetMapping("/get")
	fun getGroupNames(): String? {
		val blob: Blob? = storage.get(id)
		val fileContent = blob?.getContent()
		val ret = fileContent?.let { String(it, StandardCharsets.UTF_8) }
		return ret
	}

	@DeleteMapping("/delete")
	fun deleteGroupNames() {
		storage.delete(id)
	}
}

@RestController
@RequestMapping("/recently-deleted")
class RecentlyDeletedResource() {
	val resource = ClassPathResource("cs346-notes-app-key.json").inputStream
	val credentials: GoogleCredentials = GoogleCredentials.fromStream(resource)

	val storage: Storage = StorageOptions.newBuilder().setCredentials(credentials).build().service
	val id: BlobId = BlobId.of("spring-bucket-aydna66", "recentlyDeleted.json")

	@PostMapping("/add")
	fun addRecentlyDeleted(@RequestBody jsonData: String) {

		val info = BlobInfo.newBuilder(id).build()

		storage.create(info, jsonData.toByteArray())
	}

	@GetMapping("/get")
	fun getRecentlyDeleted(): String? {
		val blob: Blob? = storage.get(id)
		val fileContent = blob?.getContent()
		val ret = fileContent?.let { String(it, StandardCharsets.UTF_8) }
		return ret
	}

	@DeleteMapping("/delete")
	fun deleteRecentlyDeleted() {
		storage.delete(id)
	}
}



