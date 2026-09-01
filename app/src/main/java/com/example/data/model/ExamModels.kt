package com.example.data.model

enum class DrillType {
  LISTENING_CHOICE,
  READING_COMPREHENSION,
  SENTENCE_REORDER,
  SPEAKING_RECAST,
  PINYIN_MATCH
}

data class ExamQuestion(
  val id: Int,
  val hskLevel: Int,
  val drillType: DrillType,
  val promptChinese: String,
  val promptPinyin: String,
  val promptAudioText: String,
  val questionText: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanation: String,
  val targetSkill: String
)

data class PlacementQuizResult(
  val recommendedHskLevel: Int,
  val estimatedTrack: HskTrack,
  val totalScore: Int,
  val maxScore: Int,
  val listeningAccuracy: Int,
  val readingAccuracy: Int,
  val speakingAccuracy: Int
)

data class LessonUnit(
  val id: Int,
  val hskLevel: Int,
  val titleEn: String,
  val titleZh: String,
  val pinyin: String,
  val description: String,
  val vocabularyCount: Int,
  val keySentenceChinese: String,
  val keySentencePinyin: String,
  val keySentenceEnglish: String,
  val isCompleted: Boolean = false
)
