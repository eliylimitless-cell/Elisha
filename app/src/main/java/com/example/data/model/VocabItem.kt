package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MasteryStatus {
  NEW,
  LEARNING,
  REVIEW,
  MASTERED
}

@Entity(tableName = "vocab_items")
data class VocabItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val hanzi: String,
  val pinyin: String,
  val english: String,
  val partOfSpeech: String = "Word",
  val hskLevel: Int = 1,
  val exampleChinese: String = "",
  val examplePinyin: String = "",
  val exampleEnglish: String = "",
  val masteryStatus: MasteryStatus = MasteryStatus.NEW,
  val reviewCount: Int = 0,
  val correctCount: Int = 0,
  val lastReviewedTimestamp: Long = 0,
  val isStruggled: Boolean = false,
  val isBookmarked: Boolean = false,
  val notes: String = ""
)
