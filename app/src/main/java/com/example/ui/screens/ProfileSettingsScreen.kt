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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AuthProvider
import com.example.data.model.PinyinDisplayMode
import com.example.data.model.UserProfile
import com.example.ui.components.HskBadge
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun ProfileSettingsScreen(
  userProfile: UserProfile,
  onOpenAuthDialog: () -> Unit,
  onOpenSubscriptionDialog: () -> Unit,
  onUpdateSettings: (hskLevel: Int, voice: String, speed: Float, pinyinMode: PinyinDisplayMode, goal: String, prevSystem: String) -> Unit
) {
  var selectedLevel by remember(userProfile.currentHskLevel) { mutableStateOf(userProfile.currentHskLevel) }
  var selectedVoice by remember(userProfile.selectedVoice) { mutableStateOf(userProfile.selectedVoice) }
  var speechSpeed by remember(userProfile.speechRate) { mutableStateOf(userProfile.speechRate) }
  var pinyinMode by remember(userProfile.pinyinDisplayMode) { mutableStateOf(userProfile.pinyinDisplayMode) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("profile_settings_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp)
  ) {
    // User Profile Card
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                painter = painterResource(id = R.drawable.ic_hanyumate_logo),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(54.dp)
                  .clip(CircleShape)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = userProfile.displayName,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = when (userProfile.authProvider) {
                    AuthProvider.EMAIL -> userProfile.email
                    AuthProvider.ZIMBABWE_PHONE -> userProfile.phone
                    AuthProvider.GUEST -> "Guest Account (Unsynced)"
                  },
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            OutlinedButton(
              onClick = onOpenAuthDialog,
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(if (userProfile.authProvider == AuthProvider.GUEST) "Sign In" else "Switch")
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // VIP Subscription Card
    item {
      Card(
        colors = CardDefaults.cardColors(
          containerColor = if (userProfile.isProUnlocked) JadeBambooSecondary.copy(alpha = 0.1f) else ImperialRedPrimary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenSubscriptionDialog() }
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (userProfile.isProUnlocked) JadeBambooSecondary else ImperialGoldTertiary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (userProfile.isProUnlocked) Icons.Default.LockOpen else Icons.Default.Star,
                contentDescription = null,
                tint = Color.White
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (userProfile.isProUnlocked) "HanyuMate VIP Active" else "Upgrade to HanyuMate VIP",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (userProfile.isProUnlocked) JadeBambooSecondary else ImperialRedPrimary
              )
              Text(
                text = if (userProfile.isProUnlocked) "Unlimited AI Tutor & Mock Exams Unlocked" else "EcoCash · OneMoney · Google Play Billing",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Text(
            text = if (userProfile.isProUnlocked) "Manage" else "Unlock",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ImperialRedPrimary
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Learning Preferences
    item {
      Text(
        text = "LEARNING PREFERENCES",
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
          // Current HSK Target Level
          Text(text = "Target HSK Level", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            (1..6).forEach { lvl ->
              val isSelected = selectedLevel == lvl
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                  .weight(1f)
                  .clickable {
                    selectedLevel = lvl
                    onUpdateSettings(lvl, selectedVoice, speechSpeed, pinyinMode, userProfile.studyGoal, userProfile.previousHskSystemStudied)
                  }
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.padding(vertical = 8.dp)
                ) {
                  Text(
                    text = "$lvl",
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
          Spacer(modifier = Modifier.height(16.dp))

          // Pinyin Display Mode
          Text(text = "Pinyin Annotations", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            val modes = listOf(
              Pair(PinyinDisplayMode.ALWAYS, "Always Show"),
              Pair(PinyinDisplayMode.ON_TAP, "Tap to Reveal"),
              Pair(PinyinDisplayMode.OFF, "Off (Hanzi Only)")
            )
            modes.forEach { (mode, label) ->
              val isSelected = pinyinMode == mode
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) ImperialRedPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, ImperialRedPrimary) else null,
                modifier = Modifier
                  .weight(1f)
                  .clickable {
                    pinyinMode = mode
                    onUpdateSettings(selectedLevel, selectedVoice, speechSpeed, mode, userProfile.studyGoal, userProfile.previousHskSystemStudied)
                  }
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                ) {
                  Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
          Spacer(modifier = Modifier.height(16.dp))

          // Voice Speed Slider
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = "Speech Rate", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(
              text = when {
                speechSpeed <= 0.75f -> "0.7x (Slow Learner)"
                speechSpeed <= 1.0f -> "0.85x (Clear)"
                else -> "1.2x (Native Speed)"
              },
              fontSize = 12.sp,
              color = JadeBambooSecondary,
              fontWeight = FontWeight.Medium
            )
          }
          Slider(
            value = speechSpeed,
            onValueChange = {
              speechSpeed = it
              onUpdateSettings(selectedLevel, selectedVoice, it, pinyinMode, userProfile.studyGoal, userProfile.previousHskSystemStudied)
            },
            valueRange = 0.7f..1.3f,
            steps = 2
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // App Copyright & Attribution Legal Notice
    item {
      Text(
        text = "LEGAL NOTICE & ATTRIBUTION",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "App Copyright & Intellectual Property",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "© 2026 HanyuMate Technologies. All rights reserved.\n\"HanyuMate\", including its UI design, curriculum structure, adaptive pedagogical flows, and original content, is protected intellectual property. Unauthorized reproduction or reverse engineering is prohibited.",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Curriculum Standards: Aligned with the Chinese Testing International (CTI) HSK 3.0 Standards (Chinese Proficiency Grading Standards for International Chinese Language Education, GF 0025-2021).",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
