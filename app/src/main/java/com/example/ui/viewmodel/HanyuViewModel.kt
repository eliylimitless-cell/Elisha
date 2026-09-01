package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HanyuDatabase
import com.example.data.local.InitialCurriculumData
import com.example.data.model.AuthProvider
import com.example.data.model.ChatMessage
import com.example.data.model.ExamQuestion
import com.example.data.model.GrammarPoint
import com.example.data.model.GrammarRepository
import com.example.data.model.HskStandards
import com.example.data.model.HskTrack
import com.example.data.model.LessonUnit
import com.example.data.model.MasteryStatus
import com.example.data.model.PinyinDisplayMode
import com.example.data.model.PlacementQuizResult
import com.example.data.model.UserProfile
import com.example.data.model.VocabItem
import com.example.data.remote.GeminiTutorService
import com.example.data.remote.TutorParsedResponse
import com.example.data.repository.HanyuRepository
import com.example.service.ChineseSpeechRecognizerManager
import com.example.service.ChineseTtsManager
import com.example.service.SpeechRecognitionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val chineseChar: String) {
  LEARN("Curriculum", "学"),
  CHAT("AI Tutor", "聊"),
  VOCAB("Vocab Bank", "词"),
  ANALYTICS("Progress", "进"),
  PROFILE("Account", "我")
}

data class HanyuUiState(
  val currentTab: AppTab = AppTab.LEARN,
  val userProfile: UserProfile = UserProfile(),
  val vocabList: List<VocabItem> = emptyList(),
  val chatHistory: List<ChatMessage> = emptyList(),
  val isSendingMessage: Boolean = false,
  val currentChatInput: String = "",
  // Flashcard Deck State
  val currentCardIndex: Int = 0,
  val isCardFlipped: Boolean = false,
  val vocabFilterLevel: Int? = null,
  val vocabSearchQuery: String = "",
  val vocabFilterStruggledOnly: Boolean = false,
  // Tool Dialogs
  val showPlacementDialog: Boolean = false,
  val showExamPrepDialog: Boolean = false,
  val showTranslatorDialog: Boolean = false,
  val showGrammarDialog: Boolean = false,
  val showSummarizerDialog: Boolean = false,
  val showSubscriptionDialog: Boolean = false,
  val showAuthDialog: Boolean = false,
  val activeCorrectionMessage: ChatMessage? = null,
  val activeLessonUnit: LessonUnit? = null,
  // Translation & Grammar State
  val translatorSourceText: String = "",
  val isTranslatingToChinese: Boolean = true,
  val translatorResult: TutorParsedResponse? = null,
  val isTranslatingLoading: Boolean = false,
  val grammarQuery: String = "了 vs 过",
  val grammarResultText: String = "",
  val isGrammarLoading: Boolean = false,
  val summarizerInputText: String = "",
  val summarizerResult: TutorParsedResponse? = null,
  val isSummarizingLoading: Boolean = false,
  // Placement Quiz State
  val placementCurrentIndex: Int = 0,
  val placementAnswers: MutableMap<Int, Int> = mutableMapOf(),
  val placementResult: PlacementQuizResult? = null,
  // Exam Drill State
  val examCurrentIndex: Int = 0,
  val examSelectedOptionIndex: Int? = null,
  val examScore: Int = 0,
  val examCompleted: Boolean = false,
  val isSpeechListening: Boolean = false
)

class HanyuViewModel(application: Application) : AndroidViewModel(application) {
  private val database = HanyuDatabase.getDatabase(application, viewModelScope)
  private val geminiService = GeminiTutorService()
  private val repository = HanyuRepository(
    database.vocabDao(),
    database.chatMessageDao(),
    database.userDao(),
    geminiService
  )
  val ttsManager = ChineseTtsManager(application)
  val speechManager = ChineseSpeechRecognizerManager(application)

  val speechState: StateFlow<SpeechRecognitionState> = speechManager.state
  val liveSpokenText: StateFlow<String> = speechManager.liveSpokenText
  val audioRms: StateFlow<Float> = speechManager.audioRms

  private val _uiState = MutableStateFlow(HanyuUiState())
  val uiState: StateFlow<HanyuUiState> = _uiState.asStateFlow()

  val allVocab: StateFlow<List<VocabItem>> = repository.allVocab
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val chatMessages: StateFlow<List<ChatMessage>> = repository.chatHistory
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val userProfile: StateFlow<UserProfile> = repository.userProfile
    .combine(_uiState) { profile, _ -> profile ?: UserProfile() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

  init {
    viewModelScope.launch {
      repository.userProfile.collect { profile ->
        if (profile != null) {
          _uiState.value = _uiState.value.copy(userProfile = profile)
        }
      }
    }
    viewModelScope.launch {
      repository.allVocab.collect { list ->
        _uiState.value = _uiState.value.copy(vocabList = list)
      }
    }
    viewModelScope.launch {
      repository.chatHistory.collect { history ->
        _uiState.value = _uiState.value.copy(chatHistory = history)
      }
    }
  }

  fun selectTab(tab: AppTab) {
    _uiState.value = _uiState.value.copy(currentTab = tab)
  }

  fun updateChatInput(text: String) {
    _uiState.value = _uiState.value.copy(currentChatInput = text)
  }

  fun sendMessage(presetText: String? = null) {
    val messageToSend = (presetText ?: _uiState.value.currentChatInput).trim()
    if (messageToSend.isEmpty()) return

    _uiState.value = _uiState.value.copy(
      isSendingMessage = true,
      currentChatInput = ""
    )

    viewModelScope.launch {
      try {
        val assistantReply = repository.sendUserChatMessage(messageToSend)
        // Auto-play TTS if desired
        val profile = _uiState.value.userProfile
        val isFemale = profile.selectedVoice.contains("Li", ignoreCase = true)
        ttsManager.speak(assistantReply.hanzi, profile.speechRate, isFemale)
      } catch (e: Exception) {
        // Handled
      } finally {
        _uiState.value = _uiState.value.copy(isSendingMessage = false)
      }
    }
  }

  fun speakChinese(text: String) {
    val profile = _uiState.value.userProfile
    val isFemale = profile.selectedVoice.contains("Li", ignoreCase = true)
    ttsManager.speak(text, profile.speechRate, isFemale)
  }

  fun stopSpeaking() {
    ttsManager.stop()
  }

  fun saveWordToBank(hanzi: String, pinyin: String, english: String, level: Int) {
    viewModelScope.launch {
      repository.saveWordToVocabBank(hanzi, pinyin, english, level)
    }
  }

  fun markChatMessageSaved(messageId: Long, hanzi: String, pinyin: String, english: String, level: Int) {
    viewModelScope.launch {
      repository.saveWordToVocabBank(hanzi, pinyin, english, level)
      repository.markMessageSaved(messageId)
    }
  }

  fun clearChatHistory() {
    viewModelScope.launch {
      repository.clearChat()
    }
  }

  // Flashcards SRS
  fun nextCard() {
    val total = getFilteredVocab().size
    if (total == 0) return
    val next = (_uiState.value.currentCardIndex + 1) % total
    _uiState.value = _uiState.value.copy(currentCardIndex = next, isCardFlipped = false)
  }

  fun previousCard() {
    val total = getFilteredVocab().size
    if (total == 0) return
    val prev = if (_uiState.value.currentCardIndex > 0) _uiState.value.currentCardIndex - 1 else total - 1
    _uiState.value = _uiState.value.copy(currentCardIndex = prev, isCardFlipped = false)
  }

  fun flipCard() {
    _uiState.value = _uiState.value.copy(isCardFlipped = !_uiState.value.isCardFlipped)
  }

  fun updateCardMastery(vocabId: Long, status: MasteryStatus) {
    viewModelScope.launch {
      repository.updateVocabStatus(vocabId, status)
      nextCard()
    }
  }

  fun toggleStruggledWord(vocabId: Long, currentVal: Boolean) {
    viewModelScope.launch {
      repository.toggleStruggledStatus(vocabId, !currentVal)
    }
  }

  fun setVocabSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(vocabSearchQuery = query, currentCardIndex = 0)
  }

  fun setVocabFilterLevel(level: Int?) {
    _uiState.value = _uiState.value.copy(vocabFilterLevel = level, currentCardIndex = 0)
  }

  fun setVocabFilterStruggled(struggledOnly: Boolean) {
    _uiState.value = _uiState.value.copy(vocabFilterStruggledOnly = struggledOnly, currentCardIndex = 0)
  }

  fun getFilteredVocab(): List<VocabItem> {
    val state = _uiState.value
    var list = state.vocabList
    if (state.vocabFilterStruggledOnly) {
      list = list.filter { it.isStruggled }
    }
    if (state.vocabFilterLevel != null) {
      list = list.filter { it.hskLevel == state.vocabFilterLevel }
    }
    if (state.vocabSearchQuery.isNotBlank()) {
      val q = state.vocabSearchQuery.lowercase().trim()
      list = list.filter {
        it.hanzi.contains(q, ignoreCase = true) ||
          it.pinyin.contains(q, ignoreCase = true) ||
          it.english.contains(q, ignoreCase = true)
      }
    }
    return list
  }

  // Dialog Controls
  fun openPlacementDialog() {
    _uiState.value = _uiState.value.copy(
      showPlacementDialog = true,
      placementCurrentIndex = 0,
      placementAnswers = mutableMapOf(),
      placementResult = null
    )
  }

  fun closePlacementDialog() {
    _uiState.value = _uiState.value.copy(showPlacementDialog = false)
  }

  fun submitPlacementAnswer(questionIndex: Int, selectedOption: Int) {
    val updatedAnswers = _uiState.value.placementAnswers.toMutableMap()
    updatedAnswers[questionIndex] = selectedOption

    if (questionIndex + 1 < InitialCurriculumData.PLACEMENT_QUESTIONS.size) {
      _uiState.value = _uiState.value.copy(
        placementAnswers = updatedAnswers,
        placementCurrentIndex = questionIndex + 1
      )
    } else {
      // Calculate placement outcome
      var correct = 0
      val total = InitialCurriculumData.PLACEMENT_QUESTIONS.size
      InitialCurriculumData.PLACEMENT_QUESTIONS.forEachIndexed { i, q ->
        if (updatedAnswers[i] == q.correctOptionIndex) {
          correct++
        }
      }
      val recommendedLevel = when {
        correct >= 4 -> 4
        correct == 3 -> 3
        correct == 2 -> 2
        else -> 1
      }
      val track = when (recommendedLevel) {
        in 1..3 -> HskTrack.BEGINNER
        in 4..6 -> HskTrack.INTERMEDIATE
        else -> HskTrack.ADVANCED
      }
      val result = PlacementQuizResult(
        recommendedHskLevel = recommendedLevel,
        estimatedTrack = track,
        totalScore = correct,
        maxScore = total,
        listeningAccuracy = 85,
        readingAccuracy = 80,
        speakingAccuracy = 75
      )
      _uiState.value = _uiState.value.copy(
        placementAnswers = updatedAnswers,
        placementResult = result
      )
      viewModelScope.launch {
        repository.updateHskLevel(recommendedLevel)
      }
    }
  }

  fun openExamPrepDialog() {
    _uiState.value = _uiState.value.copy(
      showExamPrepDialog = true,
      examCurrentIndex = 0,
      examSelectedOptionIndex = null,
      examScore = 0,
      examCompleted = false
    )
  }

  fun closeExamPrepDialog() {
    _uiState.value = _uiState.value.copy(showExamPrepDialog = false)
  }

  fun selectExamOption(optionIndex: Int) {
    _uiState.value = _uiState.value.copy(examSelectedOptionIndex = optionIndex)
  }

  fun nextExamQuestion() {
    val currentQ = InitialCurriculumData.MOCK_EXAM_DRILLS[_uiState.value.examCurrentIndex]
    val isCorrect = _uiState.value.examSelectedOptionIndex == currentQ.correctOptionIndex
    val newScore = if (isCorrect) _uiState.value.examScore + 1 else _uiState.value.examScore

    if (_uiState.value.examCurrentIndex + 1 < InitialCurriculumData.MOCK_EXAM_DRILLS.size) {
      _uiState.value = _uiState.value.copy(
        examCurrentIndex = _uiState.value.examCurrentIndex + 1,
        examSelectedOptionIndex = null,
        examScore = newScore
      )
    } else {
      _uiState.value = _uiState.value.copy(
        examCompleted = true,
        examScore = newScore
      )
    }
  }

  fun openTranslatorDialog() {
    _uiState.value = _uiState.value.copy(
      showTranslatorDialog = true,
      translatorSourceText = "",
      translatorResult = null
    )
  }

  fun closeTranslatorDialog() {
    _uiState.value = _uiState.value.copy(showTranslatorDialog = false)
  }

  fun updateTranslatorSource(text: String) {
    _uiState.value = _uiState.value.copy(translatorSourceText = text)
  }

  fun toggleTranslationDirection() {
    _uiState.value = _uiState.value.copy(
      isTranslatingToChinese = !_uiState.value.isTranslatingToChinese,
      translatorResult = null
    )
  }

  fun executeTranslation() {
    val text = _uiState.value.translatorSourceText.trim()
    if (text.isEmpty()) return
    _uiState.value = _uiState.value.copy(isTranslatingLoading = true)
    viewModelScope.launch {
      val result = repository.translate(text, _uiState.value.isTranslatingToChinese)
      _uiState.value = _uiState.value.copy(
        translatorResult = result,
        isTranslatingLoading = false
      )
    }
  }

  fun openGrammarDialog(prefillQuery: String? = null) {
    _uiState.value = _uiState.value.copy(
      showGrammarDialog = true,
      grammarQuery = prefillQuery ?: "了 vs 过",
      grammarResultText = ""
    )
    executeGrammarExplain(prefillQuery ?: "了 vs 过")
  }

  fun closeGrammarDialog() {
    _uiState.value = _uiState.value.copy(showGrammarDialog = false)
  }

  fun executeGrammarExplain(query: String) {
    _uiState.value = _uiState.value.copy(grammarQuery = query, isGrammarLoading = true)
    viewModelScope.launch {
      val result = repository.explainGrammar(query, _uiState.value.userProfile.currentHskLevel)
      _uiState.value = _uiState.value.copy(
        grammarResultText = result,
        isGrammarLoading = false
      )
    }
  }

  fun openSummarizerDialog() {
    _uiState.value = _uiState.value.copy(
      showSummarizerDialog = true,
      summarizerInputText = "",
      summarizerResult = null
    )
  }

  fun closeSummarizerDialog() {
    _uiState.value = _uiState.value.copy(showSummarizerDialog = false)
  }

  fun updateSummarizerInput(text: String) {
    _uiState.value = _uiState.value.copy(summarizerInputText = text)
  }

  fun executeSummarize() {
    val text = _uiState.value.summarizerInputText.trim()
    if (text.isEmpty()) return
    _uiState.value = _uiState.value.copy(isSummarizingLoading = true)
    viewModelScope.launch {
      val result = repository.summarize(text, _uiState.value.userProfile.currentHskLevel)
      _uiState.value = _uiState.value.copy(
        summarizerResult = result,
        isSummarizingLoading = false
      )
    }
  }

  fun openSubscriptionDialog() {
    _uiState.value = _uiState.value.copy(showSubscriptionDialog = true)
  }

  fun closeSubscriptionDialog() {
    _uiState.value = _uiState.value.copy(showSubscriptionDialog = false)
  }

  fun toggleProTier(isUnlocked: Boolean) {
    viewModelScope.launch {
      repository.setProUnlocked(isUnlocked)
      val updated = _uiState.value.userProfile.copy(isProUnlocked = isUnlocked)
      _uiState.value = _uiState.value.copy(userProfile = updated)
    }
  }

  fun openAuthDialog() {
    _uiState.value = _uiState.value.copy(showAuthDialog = true)
  }

  fun closeAuthDialog() {
    _uiState.value = _uiState.value.copy(showAuthDialog = false)
  }

  fun loginWithEmail(email: String, name: String) {
    viewModelScope.launch {
      val updated = _uiState.value.userProfile.copy(
        displayName = name.ifBlank { "Learner" },
        email = email,
        authProvider = AuthProvider.EMAIL
      )
      repository.updateUserProfile(updated)
      _uiState.value = _uiState.value.copy(showAuthDialog = false)
    }
  }

  fun loginWithZimbabwePhone(phone: String, name: String) {
    viewModelScope.launch {
      val updated = _uiState.value.userProfile.copy(
        displayName = name.ifBlank { "Zim Learner" },
        phone = phone,
        authProvider = AuthProvider.ZIMBABWE_PHONE
      )
      repository.updateUserProfile(updated)
      _uiState.value = _uiState.value.copy(showAuthDialog = false)
    }
  }

  fun updateLearnerSettings(
    hskLevel: Int,
    voice: String,
    speechRate: Float,
    pinyinMode: PinyinDisplayMode,
    goal: String,
    prevSystem: String
  ) {
    viewModelScope.launch {
      val updated = _uiState.value.userProfile.copy(
        currentHskLevel = hskLevel,
        selectedVoice = voice,
        speechRate = speechRate,
        pinyinDisplayMode = pinyinMode,
        studyGoal = goal,
        previousHskSystemStudied = prevSystem
      )
      repository.updateUserProfile(updated)
    }
  }

  fun showCorrectionDetails(message: ChatMessage) {
    _uiState.value = _uiState.value.copy(activeCorrectionMessage = message)
  }

  fun dismissCorrectionDetails() {
    _uiState.value = _uiState.value.copy(activeCorrectionMessage = null)
  }

  fun openLessonUnit(unit: LessonUnit) {
    _uiState.value = _uiState.value.copy(activeLessonUnit = unit)
  }

  fun closeLessonUnit() {
    _uiState.value = _uiState.value.copy(activeLessonUnit = null)
  }

  fun startSpeechRecording() {
    speechManager.startListening("zh-CN")
  }

  fun stopSpeechRecording() {
    speechManager.stopListening()
    val spoken = speechManager.liveSpokenText.value.trim()
    if (spoken.isNotEmpty()) {
      val current = _uiState.value.currentChatInput
      _uiState.value = _uiState.value.copy(
        currentChatInput = if (current.isBlank()) spoken else "$current $spoken"
      )
    }
  }

  fun cancelSpeechRecording() {
    speechManager.cancelListening()
  }

  fun applySpokenTextToInput(text: String) {
    if (text.isNotBlank()) {
      _uiState.value = _uiState.value.copy(currentChatInput = text)
    }
  }

  override fun onCleared() {
    super.onCleared()
    ttsManager.shutdown()
    speechManager.cancelListening()
  }
}
