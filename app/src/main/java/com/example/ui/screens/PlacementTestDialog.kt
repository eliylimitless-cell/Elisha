package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.InitialCurriculumData
import com.example.data.model.PlacementQuizResult
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.HskBadge
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun PlacementTestDialog(
  currentIndex: Int,
  result: PlacementQuizResult?,
  onAnswerSelected: (questionIndex: Int, optionIndex: Int) -> Unit,
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
        .testTag("placement_test_dialog")
    ) {
      if (result != null) {
        // Result Screen
        Column(
          modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(JadeBambooSecondary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.School,
              contentDescription = null,
              tint = JadeBambooSecondary,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Placement Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Based on your adaptive responses, your optimal starting point is:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          Card(
            colors = CardDefaults.cardColors(
              containerColor = ImperialRedPrimary.copy(alpha = 0.08f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              HskBadge(level = result.recommendedHskLevel, showLabel = true)
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = result.estimatedTrack.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ImperialRedPrimary
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = result.estimatedTrack.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Start Learning with AI Tutor")
          }
        }
      } else {
        // Active Question Screen
        val questions = InitialCurriculumData.PLACEMENT_QUESTIONS
        val currentQ = questions.getOrNull(currentIndex) ?: questions[0]
        var selectedOption by remember(currentIndex) { mutableStateOf<Int?>(null) }

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
            Column {
              Text(
                text = "Adaptive Placement Quiz",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Question ${currentIndex + 1} of ${questions.size} · ${currentQ.targetSkill}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          LinearProgressIndicator(
            progress = { (currentIndex + 1f) / questions.size },
            modifier = Modifier.fillMaxWidth(),
            color = ImperialRedPrimary
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Prompt Card
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                HskBadge(level = currentQ.hskLevel)
                AudioPlayButton(
                  isPlaying = false,
                  onClick = { onPlayAudio(currentQ.promptAudioText) },
                  size = 36.dp
                )
              }

              Spacer(modifier = Modifier.height(8.dp))
              PinyinHanziText(
                hanzi = currentQ.promptChinese,
                pinyin = currentQ.promptPinyin,
                hanziSize = 20.sp,
                pinyinSize = 13.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = currentQ.questionText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(12.dp))

          currentQ.options.forEachIndexed { optIndex, optionText ->
            val isSelected = selectedOption == optIndex
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) ImperialRedPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ImperialRedPrimary) else null,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { selectedOption = optIndex }
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.outlineVariant),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${('A' + optIndex)}",
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = optionText,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              if (selectedOption != null) {
                onAnswerSelected(currentIndex, selectedOption!!)
              }
            },
            enabled = selectedOption != null,
            colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(if (currentIndex + 1 == questions.size) "Complete Placement" else "Next Question")
          }
        }
      }
    }
  }
}
