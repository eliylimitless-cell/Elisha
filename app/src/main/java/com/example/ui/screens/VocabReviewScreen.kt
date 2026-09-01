package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.model.MasteryStatus
import com.example.data.model.VocabItem
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.HskBadge
import com.example.ui.components.PinyinHanziText
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary

@Composable
fun VocabReviewScreen(
  vocabList: List<VocabItem>,
  currentCardIndex: Int,
  isCardFlipped: Boolean,
  selectedLevelFilter: Int?,
  searchQuery: String,
  isStruggledOnly: Boolean,
  onFlipCard: () -> Unit,
  onNextCard: () -> Unit,
  onPrevCard: () -> Unit,
  onUpdateMastery: (id: Long, status: MasteryStatus) -> Unit,
  onToggleStruggled: (id: Long, currentVal: Boolean) -> Unit,
  onLevelFilterChange: (Int?) -> Unit,
  onSearchChange: (String) -> Unit,
  onToggleStruggledFilter: (Boolean) -> Unit,
  onPlayAudio: (String) -> Unit
) {
  val currentCard = vocabList.getOrNull(currentCardIndex)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("vocab_review_screen"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // Header & Search
    item {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Vocabulary Bank & Spaced Repetition",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Active recall flashcards and vocabulary memory retention.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChange,
          placeholder = { Text("Search Hanzi, Pinyin, or English...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ImperialRedPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Level Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          item {
            FilterChip(
              selected = selectedLevelFilter == null && !isStruggledOnly,
              onClick = {
                onLevelFilterChange(null)
                onToggleStruggledFilter(false)
              },
              label = { Text("All Words (${vocabList.size})") }
            )
          }
          item {
            FilterChip(
              selected = isStruggledOnly,
              onClick = { onToggleStruggledFilter(!isStruggledOnly) },
              label = { Text("⚠️ Struggled") }
            )
          }
          items((1..6).toList()) { level ->
            FilterChip(
              selected = selectedLevelFilter == level,
              onClick = { onLevelFilterChange(if (selectedLevelFilter == level) null else level) },
              label = { Text("HSK $level") }
            )
          }
        }
      }
    }

    // Interactive Flashcard Section
    if (currentCard != null) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "FLASHCARD ${currentCardIndex + 1} OF ${vocabList.size}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              letterSpacing = 1.sp
            )
            Text(
              text = "Tap card to flip",
              fontSize = 11.sp,
              color = ImperialRedPrimary,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Flashcard Box
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(230.dp)
              .clickable { onFlipCard() }
              .testTag("srs_flashcard")
          ) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
              ) {
                HskBadge(level = currentCard.hskLevel)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  AudioPlayButton(
                    isPlaying = false,
                    onClick = { onPlayAudio(currentCard.hanzi) },
                    size = 36.dp
                  )
                  IconButton(onClick = { onToggleStruggled(currentCard.id, currentCard.isStruggled) }) {
                    Icon(
                      imageVector = if (currentCard.isStruggled) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                      contentDescription = "Toggle Struggled",
                      tint = if (currentCard.isStruggled) ImperialRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              }

              Column(
                modifier = Modifier
                  .align(Alignment.Center)
                  .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = currentCard.hanzi,
                  fontSize = 38.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )

                if (isCardFlipped) {
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = currentCard.pinyin,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = JadeBambooSecondary
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = currentCard.english,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  if (currentCard.exampleChinese.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = "“${currentCard.exampleChinese}”",
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                } else {
                  Spacer(modifier = Modifier.height(12.dp))
                  Text(
                    text = "Tap to reveal Pinyin & Meaning",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Card Action Buttons
          Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Button(
              onClick = { onUpdateMastery(currentCard.id, MasteryStatus.LEARNING) },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Close, contentDescription = null, tint = ImperialRedPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Still Learning", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
            }

            Button(
              onClick = { onUpdateMastery(currentCard.id, MasteryStatus.MASTERED) },
              colors = ButtonDefaults.buttonColors(containerColor = JadeBambooSecondary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Mastered", color = Color.White, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Word Bank List Header
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "VOCABULARY REPOSITORY (${vocabList.size})",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    // Word Bank Items
    items(vocabList) { item ->
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.padding(14.dp)
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = item.hanzi,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item.pinyin,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = JadeBambooSecondary
              )
              Spacer(modifier = Modifier.width(8.dp))
              HskBadge(level = item.hskLevel, showLabel = false)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = item.english,
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            AudioPlayButton(
              isPlaying = false,
              onClick = { onPlayAudio(item.hanzi) },
              size = 32.dp
            )
            IconButton(
              onClick = { onToggleStruggled(item.id, item.isStruggled) }
            ) {
              Icon(
                imageVector = if (item.isStruggled) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Toggle Struggled",
                tint = if (item.isStruggled) ImperialRedPrimary else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }
  }
}
