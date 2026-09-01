package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PinyinDisplayMode
import com.example.ui.theme.JadeBambooSecondary
import com.example.ui.theme.OnWarmParchmentMuted

@Composable
fun PinyinHanziText(
  hanzi: String,
  pinyin: String,
  modifier: Modifier = Modifier,
  pinyinMode: PinyinDisplayMode = PinyinDisplayMode.ALWAYS,
  hanziSize: TextUnit = 20.sp,
  pinyinSize: TextUnit = 13.sp,
  textColor: Color = MaterialTheme.colorScheme.onSurface,
  pinyinColor: Color = JadeBambooSecondary
) {
  var isTappedVisible by remember { mutableStateOf(false) }

  val showPinyin = when (pinyinMode) {
    PinyinDisplayMode.ALWAYS -> true
    PinyinDisplayMode.ON_TAP -> isTappedVisible
    PinyinDisplayMode.OFF -> false
  }

  Column(
    modifier = modifier
      .testTag("pinyin_hanzi_text")
      .clickable(enabled = pinyinMode == PinyinDisplayMode.ON_TAP) {
        isTappedVisible = !isTappedVisible
      },
    verticalArrangement = Arrangement.Center
  ) {
    if (pinyin.isNotBlank()) {
      AnimatedVisibility(visible = showPinyin) {
        Text(
          text = pinyin,
          fontSize = pinyinSize,
          fontWeight = FontWeight.Medium,
          color = pinyinColor,
          lineHeight = (pinyinSize.value * 1.3).sp,
          modifier = Modifier.padding(bottom = 2.dp)
        )
      }
    }
    Text(
      text = hanzi,
      fontSize = hanziSize,
      fontWeight = FontWeight.SemiBold,
      color = textColor,
      lineHeight = (hanziSize.value * 1.35).sp
    )
  }
}
