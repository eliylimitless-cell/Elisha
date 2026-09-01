package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.InitialCurriculumData
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.HskBadge
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun ExamPrepDialog(
  currentIndex: Int,
  selectedOptionIndex: Int?,
  score: Int,
  isCompleted: Boolean,
  onSelectOption: (Int) -> Unit,
  onNext: () -> Unit,
  onPlayAudio: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val drills = InitialCurriculumData.MOCK_EXAM_DRILLS

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("exam_prep_dialog")
    ) {
      if (isCompleted) {
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
              .background(ImperialGoldTertiary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = ImperialGoldTertiary,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Mock Exam Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Score: $score / ${drills.size} (${(score * 100 / drills.size)}% accuracy)",
            style = MaterialTheme.typography.titleMedium,
            color = ImperialRedPrimary,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(16.dp))

          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "HSK 3.0 EXAM READINESS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = JadeBambooSecondary,
                letterSpacing = 1.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (score >= 2) "Great performance! Your listening and grammatical ordering skills are on track for your target HSK certificate." else "Good effort! Review the '把' construction and listening vocabulary in the flashcard section.",
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
            Text("Back to Lessons")
          }
        }
      } else {
        val currentQ = drills.getOrNull(currentIndex) ?: drills[0]

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
              Icon(Icons.Default.Timer, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "HSK Mock Exam Drill",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressIndicator(
            progress = { (currentIndex + 1f) / drills.size },
            modifier = Modifier.fillMaxWidth(),
            color = ImperialRedPrimary
          )

          Spacer(modifier = Modifier.height(16.dp))

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
                hanziSize = 18.sp,
                pinyinSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = currentQ.questionText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(12.dp))

          currentQ.options.forEachIndexed { optIndex, optionText ->
            val isSelected = selectedOptionIndex == optIndex
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) ImperialRedPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ImperialRedPrimary) else null,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onSelectOption(optIndex) }
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.outlineVariant),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${optIndex + 1}",
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = optionText,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = onNext,
            enabled = selectedOptionIndex != null,
            colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(if (currentIndex + 1 == drills.size) "Finish Exam Drill" else "Next Question")
          }
        }
      }
    }
  }
}
