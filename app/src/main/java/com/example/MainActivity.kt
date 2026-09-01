package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CorrectionDetailDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ExamPrepDialog
import com.example.ui.screens.GrammarExplainerDialog
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonUnitDialog
import com.example.ui.screens.PlacementTestDialog
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.SubscriptionDialog
import com.example.ui.screens.SummarizerDialog
import com.example.ui.screens.TranslatorDialog
import com.example.ui.screens.VocabReviewScreen
import com.example.ui.theme.HanyuMateTheme
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.HanyuViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: HanyuViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      HanyuMateTheme {
        HanyuMateApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun HanyuMateApp(viewModel: HanyuViewModel) {
  val uiState by viewModel.uiState.collectAsState()
  val speechState by viewModel.speechState.collectAsState()
  val liveSpokenText by viewModel.liveSpokenText.collectAsState()
  val audioRms by viewModel.audioRms.collectAsState()
  val filteredVocab = viewModel.getFilteredVocab()
  val masteredCount = uiState.vocabList.count { it.masteryStatus == com.example.data.model.MasteryStatus.MASTERED }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("hanyumate_main_scaffold"),
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("main_bottom_nav")
      ) {
        AppTab.values().forEach { tab ->
          val isSelected = uiState.currentTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { viewModel.selectTab(tab) },
            icon = {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) ImperialRedPrimary.copy(alpha = 0.15f) else Color.Transparent),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = tab.chineseChar,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            },
            label = {
              Text(
                text = tab.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = ImperialRedPrimary,
              selectedTextColor = ImperialRedPrimary,
              indicatorColor = Color.Transparent
            )
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (uiState.currentTab) {
        AppTab.LEARN -> {
          LearnScreen(
            userProfile = uiState.userProfile,
            onOpenPlacementTest = { viewModel.openPlacementDialog() },
            onOpenExamPrep = { viewModel.openExamPrepDialog() },
            onOpenGrammarExplainer = { viewModel.openGrammarDialog() },
            onOpenSummarizer = { viewModel.openSummarizerDialog() },
            onOpenLessonUnit = { unit -> viewModel.openLessonUnit(unit) },
            onPlayAudio = { text -> viewModel.speakChinese(text) }
          )
        }
        AppTab.CHAT -> {
          ChatScreen(
            chatMessages = uiState.chatHistory,
            userProfile = uiState.userProfile,
            inputText = uiState.currentChatInput,
            isSending = uiState.isSendingMessage,
            speechState = speechState,
            liveSpokenText = liveSpokenText,
            audioRms = audioRms,
            onInputChange = { text -> viewModel.updateChatInput(text) },
            onSendMessage = { preset -> viewModel.sendMessage(preset) },
            onPlayAudio = { text -> viewModel.speakChinese(text) },
            onSaveMessageToVocab = { msgId, hanzi, pinyin, english, level ->
              viewModel.markChatMessageSaved(msgId, hanzi, pinyin, english, level)
            },
            onShowCorrection = { msg -> viewModel.showCorrectionDetails(msg) },
            onOpenTranslator = { viewModel.openTranslatorDialog() },
            onOpenGrammar = { viewModel.openGrammarDialog() },
            onOpenSummarizer = { viewModel.openSummarizerDialog() },
            onClearHistory = { viewModel.clearChatHistory() },
            onStartRecording = { viewModel.startSpeechRecording() },
            onStopRecording = { viewModel.stopSpeechRecording() },
            onCancelRecording = { viewModel.cancelSpeechRecording() },
            onApplySpokenText = { viewModel.applySpokenTextToInput(it) }
          )
        }
        AppTab.VOCAB -> {
          VocabReviewScreen(
            vocabList = filteredVocab,
            currentCardIndex = uiState.currentCardIndex,
            isCardFlipped = uiState.isCardFlipped,
            selectedLevelFilter = uiState.vocabFilterLevel,
            searchQuery = uiState.vocabSearchQuery,
            isStruggledOnly = uiState.vocabFilterStruggledOnly,
            onFlipCard = { viewModel.flipCard() },
            onNextCard = { viewModel.nextCard() },
            onPrevCard = { viewModel.previousCard() },
            onUpdateMastery = { id, status -> viewModel.updateCardMastery(id, status) },
            onToggleStruggled = { id, currentVal -> viewModel.toggleStruggledWord(id, currentVal) },
            onLevelFilterChange = { lvl -> viewModel.setVocabFilterLevel(lvl) },
            onSearchChange = { q -> viewModel.setVocabSearchQuery(q) },
            onToggleStruggledFilter = { struggled -> viewModel.setVocabFilterStruggled(struggled) },
            onPlayAudio = { text -> viewModel.speakChinese(text) }
          )
        }
        AppTab.ANALYTICS -> {
          AnalyticsScreen(
            userProfile = uiState.userProfile,
            masteredVocabCount = masteredCount,
            totalVocabCount = uiState.vocabList.size
          )
        }
        AppTab.PROFILE -> {
          ProfileSettingsScreen(
            userProfile = uiState.userProfile,
            onOpenAuthDialog = { viewModel.openAuthDialog() },
            onOpenSubscriptionDialog = { viewModel.openSubscriptionDialog() },
            onUpdateSettings = { level, voice, speed, pinyinMode, goal, prevSystem ->
              viewModel.updateLearnerSettings(level, voice, speed, pinyinMode, goal, prevSystem)
            }
          )
        }
      }

      // Dialogs & Modals
      if (uiState.showPlacementDialog) {
        PlacementTestDialog(
          currentIndex = uiState.placementCurrentIndex,
          result = uiState.placementResult,
          onAnswerSelected = { qIdx, optIdx -> viewModel.submitPlacementAnswer(qIdx, optIdx) },
          onPlayAudio = { text -> viewModel.speakChinese(text) },
          onDismiss = { viewModel.closePlacementDialog() }
        )
      }

      if (uiState.showExamPrepDialog) {
        ExamPrepDialog(
          currentIndex = uiState.examCurrentIndex,
          selectedOptionIndex = uiState.examSelectedOptionIndex,
          score = uiState.examScore,
          isCompleted = uiState.examCompleted,
          onSelectOption = { optIdx -> viewModel.selectExamOption(optIdx) },
          onNext = { viewModel.nextExamQuestion() },
          onPlayAudio = { text -> viewModel.speakChinese(text) },
          onDismiss = { viewModel.closeExamPrepDialog() }
        )
      }

      if (uiState.showTranslatorDialog) {
        TranslatorDialog(
          sourceText = uiState.translatorSourceText,
          isToChinese = uiState.isTranslatingToChinese,
          result = uiState.translatorResult,
          isLoading = uiState.isTranslatingLoading,
          onSourceChange = { text -> viewModel.updateTranslatorSource(text) },
          onToggleDirection = { viewModel.toggleTranslationDirection() },
          onTranslate = { viewModel.executeTranslation() },
          onPlayAudio = { text -> viewModel.speakChinese(text) },
          onSaveToVocab = { hanzi, pinyin, english ->
            viewModel.saveWordToBank(hanzi, pinyin, english, uiState.userProfile.currentHskLevel)
          },
          onDismiss = { viewModel.closeTranslatorDialog() }
        )
      }

      if (uiState.showGrammarDialog) {
        GrammarExplainerDialog(
          initialQuery = uiState.grammarQuery,
          resultText = uiState.grammarResultText,
          isLoading = uiState.isGrammarLoading,
          onQuerySubmit = { q -> viewModel.executeGrammarExplain(q) },
          onDismiss = { viewModel.closeGrammarDialog() }
        )
      }

      if (uiState.showSummarizerDialog) {
        SummarizerDialog(
          inputText = uiState.summarizerInputText,
          result = uiState.summarizerResult,
          isLoading = uiState.isSummarizingLoading,
          onInputChange = { text -> viewModel.updateSummarizerInput(text) },
          onSummarize = { viewModel.executeSummarize() },
          onPlayAudio = { text -> viewModel.speakChinese(text) },
          onDismiss = { viewModel.closeSummarizerDialog() }
        )
      }

      if (uiState.showSubscriptionDialog) {
        SubscriptionDialog(
          isProUnlocked = uiState.userProfile.isProUnlocked,
          onTogglePro = { isPro -> viewModel.toggleProTier(isPro) },
          onDismiss = { viewModel.closeSubscriptionDialog() }
        )
      }

      if (uiState.showAuthDialog) {
        AuthDialog(
          onEmailLogin = { email, name -> viewModel.loginWithEmail(email, name) },
          onPhoneLogin = { phone, name -> viewModel.loginWithZimbabwePhone(phone, name) },
          onDismiss = { viewModel.closeAuthDialog() }
        )
      }

      if (uiState.activeCorrectionMessage != null) {
        CorrectionDetailDialog(
          message = uiState.activeCorrectionMessage!!,
          onDismiss = { viewModel.dismissCorrectionDetails() },
          onExplainMore = { snippet ->
            viewModel.openGrammarDialog(snippet)
          }
        )
      }

      if (uiState.activeLessonUnit != null) {
        LessonUnitDialog(
          unit = uiState.activeLessonUnit!!,
          onPlayAudio = { text -> viewModel.speakChinese(text) },
          onPracticeWithAI = { topicPrompt ->
            viewModel.selectTab(AppTab.CHAT)
            viewModel.sendMessage(topicPrompt)
          },
          onDismiss = { viewModel.closeLessonUnit() }
        )
      }
    }
  }
}
