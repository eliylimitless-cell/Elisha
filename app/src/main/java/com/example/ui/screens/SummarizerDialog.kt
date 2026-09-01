package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compress
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.remote.TutorParsedResponse
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun SummarizerDialog(
  inputText: String,
  result: TutorParsedResponse?,
  isLoading: Boolean,
  onInputChange: (String) -> Unit,
  onSummarize: () -> Unit,
  onPlayAudio: (String) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("summarizer_dialog")
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
            Icon(Icons.Default.Compress, contentDescription = null, tint = ImperialRedPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Passage Simplifier",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Paste any Chinese article, news, or long dialogue to simplify into your target HSK reading level.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = inputText,
          onValueChange = onInputChange,
          placeholder = { Text("粘贴中文长文或对话...") },
          minLines = 4,
          maxLines = 6,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ImperialRedPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onSummarize,
          enabled = inputText.isNotBlank() && !isLoading,
          colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simplifying...")
          } else {
            Text("Simplify & Summarize")
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
                  text = "SIMPLIFIED SUMMARY",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = JadeBambooSecondary,
                  letterSpacing = 1.sp
                )
                AudioPlayButton(
                  isPlaying = false,
                  onClick = { onPlayAudio(result.hanzi) },
                  size = 36.dp
                )
              }

              Spacer(modifier = Modifier.height(8.dp))
              PinyinHanziText(
                hanzi = result.hanzi,
                pinyin = result.pinyin,
                hanziSize = 18.sp,
                pinyinSize = 12.sp
              )

              if (result.english.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = result.english,
                  style = MaterialTheme.typography.bodySmall,
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
