package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageRole {
  USER,
  ASSISTANT,
  SYSTEM
}

data class CorrectionDetail(
  val originalSnippet: String,
  val correctedSnippet: String,
  val reason: String
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val role: MessageRole,
  val hanzi: String,
  val pinyin: String = "",
  val english: String = "",
  val timestamp: Long = System.currentTimeMillis(),
  val hskLevel: Int = 1,
  val hasCorrection: Boolean = false,
  val correctionSnippet: String = "",
  val correctionExplanation: String = "",
  val isAudioPlaying: Boolean = false,
  val isSavedToVocab: Boolean = false
)
