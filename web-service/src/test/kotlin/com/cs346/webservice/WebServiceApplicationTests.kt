package com.cs346.webservice

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Cors.Origin.any
import com.google.cloud.storage.Storage

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

@SpringBootTest
@AutoConfigureMockMvc
class WebServiceApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@MockBean
	lateinit var storage: Storage

	val blobId1: BlobId = BlobId.of("spring-bucket-aydna66", "notes.json")
	val blobId2: BlobId = BlobId.of("spring-bucket-aydna66", "groupNames.json")
	val blobId3: BlobId = BlobId.of("spring-bucket-aydna66", "recentlyDeleted.json")


	@Test
	fun testGetNotes() {
		val blob = storage.create(BlobInfo.newBuilder(blobId1).build())

		doReturn(blob).`when`(storage).get()

		mockMvc.perform(get("/notes/get"))
			.andExpect(status().isOk)

		verify(storage, times(0)).get(blobId1)
	}


	// groupNames endpoints tests
	@Test
	fun testGetGroup() {
		val blob = storage.create(BlobInfo.newBuilder(blobId2).build())

		doReturn(blob).`when`(storage).get(blobId2)

		mockMvc.perform(get("/groups/get"))
			.andExpect(status().isOk)

		verify(storage, times(0)).get(blobId2)
	}


	// Tests for recentlyDeleted endpoints
	@Test
	fun testGetRecentlyDeleted() {
		val blob = storage.create(BlobInfo.newBuilder(blobId3).build())

		doReturn(blob).`when`(storage).get()

		mockMvc.perform(get("/recently-deleted/get"))
			.andExpect(status().isOk)

		verify(storage, times(0)).get(blobId3)
	}

}
