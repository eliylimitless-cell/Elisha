package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole
import com.example.data.model.UserProfile
import com.example.service.SpeechRecognitionState
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.HskBadge
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun ChatScreen(
  chatMessages: List<ChatMessage>,
  userProfile: UserProfile,
  inputText: String,
  isSending: Boolean,
  speechState: SpeechRecognitionState = SpeechRecognitionState.Idle,
  liveSpokenText: String = "",
  audioRms: Float = 0f,
  onInputChange: (String) -> Unit,
  onSendMessage: (preset: String?) -> Unit,
  onPlayAudio: (String) -> Unit,
  onSaveMessageToVocab: (messageId: Long, hanzi: String, pinyin: String, english: String, level: Int) -> Unit,
  onShowCorrection: (ChatMessage) -> Unit,
  onOpenTranslator: () -> Unit,
  onOpenGrammar: () -> Unit,
  onOpenSummarizer: () -> Unit,
  onClearHistory: () -> Unit,
  onStartRecording: () -> Unit = {},
  onStopRecording: () -> Unit = {},
  onCancelRecording: () -> Unit = {},
  onApplySpokenText: (String) -> Unit = {}
) {
  val context = LocalContext.current
  val listState = rememberLazyListState()

  val isRecordingActive = speechState is SpeechRecognitionState.Listening || speechState is SpeechRecognitionState.Initializing

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      onStartRecording()
    } else {
      Toast.makeText(context, "Microphone permission required for speech practice", Toast.LENGTH_SHORT).show()
    }
  }

  fun checkAndStartRecording() {
    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
      if (isRecordingActive) {
        onStopRecording()
      } else {
        onStartRecording()
      }
    } else {
      permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
  }

  LaunchedEffect(chatMessages.size) {
    if (chatMessages.isNotEmpty()) {
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  val promptSuggestions = listOf(
    "How do I use '把' sentences?",
    "Let's practice ordering food in Chinese",
    "Explain difference between 以后 and 后来",
    "HSK 3 spoken dialogue practice",
    "Correct my sentence: 我很喜欢吃中国菜"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("chat_screen")
  ) {
    // Tutor Header
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.ai_tutor_avatar),
            contentDescription = "Xiao Lin AI Tutor Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "小林 Xiao Lin",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(8.dp))
              HskBadge(level = userProfile.currentHskLevel, showLabel = false)
            }
            Text(
              text = "Personal Mandarin Chinese Assistant",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        IconButton(onClick = onClearHistory) {
          Icon(
            Icons.Default.DeleteOutline,
            contentDescription = "Clear Chat History",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Floating Tool Pills Bar
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = ImperialRedPrimary.copy(alpha = 0.1f),
          modifier = Modifier.clickable { onOpenTranslator() }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Translate, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Translator", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialRedPrimary)
          }
        }
      }

      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = ImperialGoldTertiary.copy(alpha = 0.12f),
          modifier = Modifier.clickable { onOpenGrammar() }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ImperialGoldTertiary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Grammar Guide", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialGoldTertiary)
          }
        }
      }

      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = JadeBambooSecondary.copy(alpha = 0.1f),
          modifier = Modifier.clickable { onOpenSummarizer() }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Compress, contentDescription = null, tint = JadeBambooSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Simplifier", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JadeBambooSecondary)
          }
        }
      }
    }

    // Messages List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .testTag("chat_messages_list"),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 12.dp)
    ) {
      items(chatMessages) { message ->
        if (message.role == MessageRole.USER) {
          // User Bubble
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("user_message_row"),
            horizontalArrangement = Arrangement.End
          ) {
            Surface(
              shape = RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp),
              color = ImperialRedPrimary,
              modifier = Modifier
                .widthIn(max = 290.dp)
                .testTag("user_message_bubble")
            ) {
              Text(
                text = message.hanzi,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
              )
            }
          }
        } else {
          // Assistant Bubble
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("ai_message_row"),
            horizontalArrangement = Arrangement.Start
          ) {
            Card(
              shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("ai_message_bubble")
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                // Top Hanzi + Pinyin
                PinyinHanziText(
                  hanzi = message.hanzi,
                  pinyin = message.pinyin,
                  pinyinMode = userProfile.pinyinDisplayMode,
                  hanziSize = 17.sp,
                  pinyinSize = 12.sp
                )

                if (message.english.isNotBlank()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = message.english,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                // Inline soft recast alert
                if (message.hasCorrection) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ImperialGoldTertiary.copy(alpha = 0.15f),
                    modifier = Modifier
                      .fillMaxWidth()
                      .clickable { onShowCorrection(message) }
                      .testTag("correction_badge")
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                      Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = ImperialGoldTertiary,
                        modifier = Modifier.size(16.dp)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "💡 Tap to view gentle tutor feedback",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGoldTertiary
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Actions: Listen Audio & Save to Bank
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  AudioPlayButton(
                    isPlaying = false,
                    onClick = { onPlayAudio(message.hanzi) },
                    size = 32.dp
                  )

                  IconButton(
                    onClick = {
                      onSaveMessageToVocab(
                        message.id,
                        message.hanzi.take(15),
                        message.pinyin.take(25),
                        message.english.take(30),
                        message.hskLevel
                      )
                    },
                    modifier = Modifier.testTag("save_vocab_button")
                  ) {
                    Icon(
                      imageVector = if (message.isSavedToVocab) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                      contentDescription = "Save to Vocab Bank",
                      tint = if (message.isSavedToVocab) JadeBambooSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      if (isSending) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
          ) {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
              modifier = Modifier.testTag("ai_typing_indicator")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
              ) {
                CircularProgressIndicator(
                  color = ImperialRedPrimary,
                  strokeWidth = 2.dp,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Xiao Lin is replying...", style = MaterialTheme.typography.bodySmall)
              }
            }
          }
        }
      }
    }

    // Suggestion Chips Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("prompt_suggestions_row")
    ) {
      items(promptSuggestions) { prompt ->
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
          modifier = Modifier.clickable { onSendMessage(prompt) }
        ) {
          Text(
            text = prompt,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }
    }

    // Active Speech Recognition (STT) Panel
    AnimatedVisibility(
      visible = isRecordingActive,
      enter = expandVertically() + fadeIn(),
      exit = shrinkVertically() + fadeOut()
    ) {
      ActiveRecordingBanner(
        liveSpokenText = liveSpokenText,
        audioRms = audioRms,
        onStop = onStopRecording,
        onCancel = onCancelRecording,
        onSendDirectly = {
          val speech = liveSpokenText.trim()
          if (speech.isNotBlank()) {
            onSendMessage(speech)
            onCancelRecording()
          } else {
            onStopRecording()
          }
        }
      )
    }

    // Error Notice for Speech
    if (speechState is SpeechRecognitionState.Error) {
      Surface(
        color = ImperialRedPrimary.copy(alpha = 0.1f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .clip(RoundedCornerShape(8.dp))
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.MicOff, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = speechState.message,
            fontSize = 11.sp,
            color = ImperialRedPrimary,
            modifier = Modifier.weight(1f)
          )
          IconButton(
            onClick = onCancelRecording,
            modifier = Modifier.size(24.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = ImperialRedPrimary, modifier = Modifier.size(14.dp))
          }
        }
      }
    }

    // Input Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("chat_input_bar")
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp)
      ) {
        OutlinedTextField(
          value = inputText,
          onValueChange = onInputChange,
          placeholder = { Text("Ask Xiao Lin or tap mic to speak...") },
          shape = RoundedCornerShape(24.dp),
          maxLines = 3,
          trailingIcon = {
            IconButton(
              onClick = { checkAndStartRecording() },
              modifier = Modifier.testTag("chat_mic_button")
            ) {
              Icon(
                imageVector = if (isRecordingActive) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = "Voice Pronunciation Input (STT)",
                tint = if (isRecordingActive) ImperialRedPrimary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
              )
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ImperialRedPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input_field")
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = { onSendMessage(null) },
          enabled = inputText.isNotBlank() && !isSending,
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (inputText.isNotBlank() && !isSending) ImperialRedPrimary else MaterialTheme.colorScheme.surfaceVariant)
            .testTag("chat_send_button")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send message",
            tint = if (inputText.isNotBlank() && !isSending) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }
  }
}

@Composable
fun ActiveRecordingBanner(
  liveSpokenText: String,
  audioRms: Float,
  onStop: () -> Unit,
  onCancel: () -> Unit,
  onSendDirectly: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_mic")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(650, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mic_scale"
  )

  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("active_recording_banner")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(ImperialRedPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              Icons.Default.Mic,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "Listening in Mandarin Chinese (普通话)",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = ImperialRedPrimary
            )
            Text(
              text = "Speak clearly to practice tones & pronunciation",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Cancel Recording", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Spoken Speech Preview Area
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "LIVE SPEECH TRANSCRIPTION:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (liveSpokenText.isNotBlank()) liveSpokenText else "“Speak in Chinese now... e.g. 你好，很高兴认识你”",
            fontSize = 15.sp,
            fontWeight = if (liveSpokenText.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
            color = if (liveSpokenText.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Control Action Buttons
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedButton(
          onClick = onStop,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("speech_insert_button")
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Insert into Input", fontSize = 12.sp)
        }

        Button(
          onClick = onSendDirectly,
          colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("speech_send_button")
        ) {
          Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Send Speech", fontSize = 12.sp)
        }
      }
    }
  }
}
