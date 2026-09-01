package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AuthProvider {
  GUEST,
  EMAIL,
  ZIMBABWE_PHONE
}

enum class PinyinDisplayMode {
  ALWAYS,
  ON_TAP,
  OFF
}

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey
  val id: Int = 1,
  val displayName: String = "Learner",
  val email: String = "",
  val phone: String = "",
  val authProvider: AuthProvider = AuthProvider.GUEST,
  val isProUnlocked: Boolean = false,
  val currentHskLevel: Int = 1,
  val previousHskSystemStudied: String = "HSK 3.0",
  val dailyStreakDays: Int = 3,
  val totalMinutesChatted: Int = 45,
  val wordsLearnedCount: Int = 38,
  val selectedVoice: String = "Female (Li)",
  val speechRate: Float = 0.85f, // 0.7f slow, 1.0f normal, 1.3f native
  val pinyinDisplayMode: PinyinDisplayMode = PinyinDisplayMode.ALWAYS,
  val dailyGoalMinutes: Int = 15,
  val studyGoal: String = "Daily Conversation & HSK Prep",
  val listeningScore: Int = 78,
  val speakingScore: Int = 65,
  val readingScore: Int = 82,
  val writingScore: Int = 60
)
