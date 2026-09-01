package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiGenerateContentRequest(
  val contents: List<GeminiContent>,
  val systemInstruction: GeminiContent? = null,
  val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
  val role: String? = null,
  val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
  val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
  val temperature: Float = 0.7f,
  val topP: Float = 0.95f,
  val topK: Int = 40
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateContentResponse(
  val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
  val content: GeminiContent? = null
)

data class TutorParsedResponse(
  val hanzi: String,
  val pinyin: String,
  val english: String,
  val hasCorrection: Boolean = false,
  val correctionSnippet: String = "",
  val correctionExplanation: String = ""
)
