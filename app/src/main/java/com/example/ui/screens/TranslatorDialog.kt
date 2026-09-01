package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.remote.TutorParsedResponse
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun TranslatorDialog(
  sourceText: String,
  isToChinese: Boolean,
  result: TutorParsedResponse?,
  isLoading: Boolean,
  onSourceChange: (String) -> Unit,
  onToggleDirection: () -> Unit,
  onTranslate: () -> Unit,
  onPlayAudio: (String) -> Unit,
  onSaveToVocab: (hanzi: String, pinyin: String, english: String) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("translator_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Translate, contentDescription = null, tint = ImperialRedPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "AI Smart Translator",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Language direction switch
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(8.dp)
          ) {
            Text(
              text = if (isToChinese) "English 🇬🇧" else "Chinese 🇨🇳",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            IconButton(onClick = onToggleDirection) {
              Icon(Icons.Default.CompareArrows, contentDescription = "Switch direction", tint = ImperialRedPrimary)
            }
            Text(
              text = if (isToChinese) "Chinese 🇨🇳" else "English 🇬🇧",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = sourceText,
          onValueChange = onSourceChange,
          placeholder = { Text(if (isToChinese) "Enter English text to translate..." else "输入需要翻译的中文...") },
          minLines = 3,
          maxLines = 5,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ImperialRedPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("translator_input_field")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onTranslate,
          enabled = sourceText.isNotBlank() && !isLoading,
          colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("translate_action_button")
        ) {
          if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Translating...")
          } else {
            Text("Translate with Chinese Scaffolding")
          }
        }

        if (result != null) {
          Spacer(modifier = Modifier.height(16.dp))

          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "TRANSLATION RESULT",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = JadeBambooSecondary,
                  letterSpacing = 1.sp
                )
                Row {
                  AudioPlayButton(
                    isPlaying = false,
                    onClick = { onPlayAudio(result.hanzi) },
                    size = 36.dp
                  )
                  IconButton(
                    onClick = { onSaveToVocab(result.hanzi, result.pinyin, result.english) }
                  ) {
                    Icon(
                      imageVector = Icons.Default.BookmarkAdd,
                      contentDescription = "Save to Vocab Bank",
                      tint = ImperialGoldTertiary
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              PinyinHanziText(
                hanzi = result.hanzi,
                pinyin = result.pinyin,
                hanziSize = 20.sp,
                pinyinSize = 13.sp
              )

              if (result.english.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = result.english,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
