package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HskStandards
import com.example.data.model.UserProfile
import com.example.ui.components.HskBadge
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary
import com.example.ui.theme.StreakFlame

@Composable
fun AnalyticsScreen(
  userProfile: UserProfile,
  masteredVocabCount: Int,
  totalVocabCount: Int
) {
  val currentHskInfo = HskStandards.getInfo(userProfile.currentHskLevel)
  val progressPercent = ((masteredVocabCount.toFloat() / currentHskInfo.cumulativeVocab.toFloat().coerceAtLeast(1f)) * 100).toInt().coerceIn(5, 95)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("analytics_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp)
  ) {
    item {
      Text(
        text = "Learning Analytics & HSK Readiness",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Track sub-skills, daily study streaks, and error patterns.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Streak & Quick Stats Banner
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceAround,
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = StreakFlame, modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "${userProfile.dailyStreakDays} Days", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = StreakFlame)
            }
            Text(text = "Daily Streak", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Box(modifier = Modifier.size(width = 1.dp, height = 36.dp).background(MaterialTheme.colorScheme.outlineVariant))

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "${userProfile.totalMinutesChatted}m", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Text(text = "AI Conversation", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Box(modifier = Modifier.size(width = 1.dp, height = 36.dp).background(MaterialTheme.colorScheme.outlineVariant))

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JadeBambooSecondary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "$masteredVocabCount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = JadeBambooSecondary)
            }
            Text(text = "Words Mastered", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Milestone Progress Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            HskBadge(level = userProfile.currentHskLevel)
            Text(
              text = "$progressPercent% Completed",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = ImperialRedPrimary
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          LinearProgressIndicator(
            progress = { progressPercent / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = ImperialRedPrimary
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Target Level: HSK ${userProfile.currentHskLevel} (${currentHskInfo.cumulativeVocab} vocabulary goal)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = currentHskInfo.focusDescription,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // 4 Sub-skills Radar / Bars
    item {
      Text(
        text = "FOUR PILLARS OF MANDARIN PROFICIENCY",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          SubSkillRow(
            icon = Icons.Default.Headphones,
            title = "Listening (听力 Tīnglì)",
            score = userProfile.listeningScore,
            barColor = JadeBambooSecondary
          )
          Spacer(modifier = Modifier.height(12.dp))
          SubSkillRow(
            icon = Icons.Default.Mic,
            title = "Speaking & Pronunciation (口语 Kǒuyǔ)",
            score = userProfile.speakingScore,
            barColor = ImperialRedPrimary
          )
          Spacer(modifier = Modifier.height(12.dp))
          SubSkillRow(
            icon = Icons.Default.MenuBook,
            title = "Reading Comprehension (阅读 Yuèdú)",
            score = userProfile.readingScore,
            barColor = ImperialGoldTertiary
          )
          Spacer(modifier = Modifier.height(12.dp))
          SubSkillRow(
            icon = Icons.Default.PieChart,
            title = "Grammar & Structure (书写 Shūxiě)",
            score = userProfile.writingScore,
            barColor = Color(0xFF6366F1)
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Common Mistake Analysis
    item {
      Text(
        text = "PEDAGOGICAL MISTAKE PATTERNS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = ImperialGoldTertiary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Personalized Remediation Insight",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "• Aspect Marker 了 vs 过: In past messages, Xiao Lin detected confusion between finished actions and lifetime experiences.\n• Word Order: Remember that time phrases (e.g. 明天上午) must precede the verb in standard Mandarin.\n• Tone Consistency: Keep practicing 3rd tone sandhi rules (two 3rd tones become 2nd + 3rd tone).",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
private fun SubSkillRow(
  icon: ImageVector,
  title: String,
  score: Int,
  barColor: Color
) {
  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = barColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
      }
      Text(text = "$score%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = barColor)
    }
    Spacer(modifier = Modifier.height(6.dp))
    LinearProgressIndicator(
      progress = { score / 100f },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = barColor
    )
  }
}
