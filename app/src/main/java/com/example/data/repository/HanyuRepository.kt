package com.example.data.repository

import com.example.data.local.ChatMessageDao
import com.example.data.local.UserDao
import com.example.data.local.VocabDao
import com.example.data.model.ChatMessage
import com.example.data.model.MasteryStatus
import com.example.data.model.MessageRole
import com.example.data.model.PinyinDisplayMode
import com.example.data.model.UserProfile
import com.example.data.model.VocabItem
import com.example.data.remote.GeminiTutorService
import com.example.data.remote.TutorParsedResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class HanyuRepository(
  private val vocabDao: VocabDao,
  private val chatMessageDao: ChatMessageDao,
  private val userDao: UserDao,
  private val geminiService: GeminiTutorService
) {
  val allVocab: Flow<List<VocabItem>> = vocabDao.getAllVocab()
  val struggledVocab: Flow<List<VocabItem>> = vocabDao.getStruggledVocab()
  val chatHistory: Flow<List<ChatMessage>> = chatMessageDao.getAllMessages()
  val userProfile: Flow<UserProfile?> = userDao.getUserProfile()

  suspend fun sendUserChatMessage(text: String): ChatMessage {
    val currentProfile = userDao.getUserProfileOnce() ?: UserProfile()
    val level = currentProfile.currentHskLevel

    // Insert user message
    val userMsg = ChatMessage(
      role = MessageRole.USER,
      hanzi = text,
      pinyin = "",
      english = "",
      hskLevel = level
    )
    val userMsgId = chatMessageDao.insertMessage(userMsg)

    // Call AI Tutor
    val recentHistory = chatHistory.firstOrNull() ?: emptyList()
    val tutorResponse = geminiService.chatWithTutor(
      userMessage = text,
      learnerHskLevel = level,
      chatHistory = recentHistory
    )

    // Insert Assistant message
    val assistantMsg = ChatMessage(
      role = MessageRole.ASSISTANT,
      hanzi = tutorResponse.hanzi,
      pinyin = tutorResponse.pinyin,
      english = tutorResponse.english,
      hskLevel = level,
      hasCorrection = tutorResponse.hasCorrection,
      correctionSnippet = tutorResponse.correctionSnippet,
      correctionExplanation = tutorResponse.correctionExplanation
    )
    val assistantId = chatMessageDao.insertMessage(assistantMsg)
    return assistantMsg.copy(id = assistantId)
  }

  suspend fun saveWordToVocabBank(hanzi: String, pinyin: String, english: String, hskLevel: Int) {
    val item = VocabItem(
      hanzi = hanzi,
      pinyin = pinyin,
      english = english,
      hskLevel = hskLevel,
      masteryStatus = MasteryStatus.LEARNING,
      isBookmarked = true
    )
    vocabDao.insertVocab(item)
    userDao.incrementWordsLearned(1)
  }

  suspend fun updateVocabStatus(id: Long, status: MasteryStatus) {
    vocabDao.updateReviewStatus(id, status, System.currentTimeMillis())
  }

  suspend fun toggleStruggledStatus(id: Long, isStruggled: Boolean) {
    vocabDao.setStruggled(id, isStruggled)
  }

  suspend fun markMessageSaved(id: Long) {
    chatMessageDao.markSavedToVocab(id)
  }

  suspend fun clearChat() {
    chatMessageDao.clearChatHistory()
  }

  suspend fun updateHskLevel(newLevel: Int) {
    userDao.updateHskLevel(newLevel)
  }

  suspend fun setProUnlocked(isPro: Boolean) {
    userDao.setProUnlocked(isPro)
  }

  suspend fun updateUserProfile(profile: UserProfile) {
    userDao.insertOrUpdate(profile)
  }

  suspend fun translate(query: String, toChinese: Boolean): TutorParsedResponse {
    return geminiService.translateText(query, toChinese)
  }

  suspend fun explainGrammar(query: String, level: Int): String {
    return geminiService.explainGrammar(query, level)
  }

  suspend fun summarize(passage: String, level: Int): TutorParsedResponse {
    return geminiService.summarizeChineseText(passage, level)
  }
}
