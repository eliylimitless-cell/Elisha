package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.InitialCurriculumData
import com.example.data.model.HskStandards
import com.example.data.model.HskTrack
import com.example.data.model.LessonUnit
import com.example.data.model.UserProfile
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.HskBadge
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun LearnScreen(
  userProfile: UserProfile,
  onOpenPlacementTest: () -> Unit,
  onOpenExamPrep: () -> Unit,
  onOpenGrammarExplainer: () -> Unit,
  onOpenSummarizer: () -> Unit,
  onOpenLessonUnit: (LessonUnit) -> Unit,
  onPlayAudio: (String) -> Unit
) {
  var selectedTrack by remember { mutableStateOf(HskTrack.BEGINNER) }
  var showLevelConverter by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("learn_screen"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // Hero Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.hsk_hero_banner),
          contentDescription = "HSK Curriculum Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
              )
            )
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            HskBadge(level = userProfile.currentHskLevel)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "HSK 3.0 Framework",
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Structured Chinese Learning Roadmap",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "From Pinyin basics to advanced Hanzi fluency & HSK certification.",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
          )
        }
      }
    }

    // Quick Action Bar
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = ImperialRedPrimary.copy(alpha = 0.1f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenPlacementTest() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(10.dp)
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Placement Test", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialRedPrimary)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = JadeBambooSecondary.copy(alpha = 0.1f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenExamPrep() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(10.dp)
            ) {
              Icon(Icons.Default.Timer, contentDescription = null, tint = JadeBambooSecondary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Mock Exam Drill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JadeBambooSecondary)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = ImperialGoldTertiary.copy(alpha = 0.1f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenGrammarExplainer() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(10.dp)
            ) {
              Icon(Icons.Default.MenuBook, contentDescription = null, tint = ImperialGoldTertiary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Grammar Guides", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialGoldTertiary)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenSummarizer() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(10.dp)
            ) {
              Icon(Icons.Default.Compress, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Text Simplifier", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }
      }
    }

    // HSK 2.0 vs 3.0 Converter Card
    item {
      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .fillMaxWidth()
          .clickable { showLevelConverter = !showLevelConverter }
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = ImperialRedPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "HSK 2.0 ⇄ HSK 3.0 Converter",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = if (showLevelConverter) "Hide" else "Show",
              fontSize = 12.sp,
              color = ImperialRedPrimary,
              fontWeight = FontWeight.Bold
            )
          }

          if (showLevelConverter) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))
            val currentInfo = HskStandards.getInfo(userProfile.currentHskLevel)
            Text(
              text = "Target Level: HSK ${currentInfo.level} (${currentInfo.stage} · CEFR ${currentInfo.approxCefr})",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = currentInfo.hsk2Mapping,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Cumulative vocabulary requirement: ${currentInfo.cumulativeVocab} words.",
              style = MaterialTheme.typography.bodySmall,
              color = JadeBambooSecondary,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    // Tracks Header
    item {
      Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
        Text(
          text = "CURRICULUM STAGES",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        TabRow(selectedTabIndex = selectedTrack.ordinal) {
          HskTrack.values().forEach { track ->
            Tab(
              selected = selectedTrack == track,
              onClick = { selectedTrack = track },
              text = { Text(if (track == HskTrack.BEGINNER) "HSK 1–3" else if (track == HskTrack.INTERMEDIATE) "HSK 4–6" else "HSK 7–9", fontSize = 12.sp) }
            )
          }
        }
      }
    }

    // Stage Lesson Units
    val units = InitialCurriculumData.LESSON_UNITS.filter {
      selectedTrack.levels.contains(it.hskLevel)
    }

    items(units) { unit ->
      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .clickable { onOpenLessonUnit(unit) }
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              HskBadge(level = unit.hskLevel)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "${unit.vocabularyCount} target words",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            if (unit.isCompleted) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JadeBambooSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mastered", fontSize = 11.sp, color = JadeBambooSecondary, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = unit.titleZh,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${unit.pinyin} · ${unit.titleEn}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = unit.keySentenceChinese,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = unit.keySentenceEnglish,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              AudioPlayButton(
                isPlaying = false,
                onClick = { onPlayAudio(unit.keySentenceChinese) },
                size = 32.dp
              )
            }
          }
        }
      }
    }
  }
}
