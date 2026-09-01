package com.example.service

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class ChineseTtsManager(context: Context) : TextToSpeech.OnInitListener {
  private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
  private var isInitialized = false

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _currentPlayingText = MutableStateFlow("")
  val currentPlayingText: StateFlow<String> = _currentPlayingText.asStateFlow()

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale.CHINESE)
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        tts?.setLanguage(Locale.SIMPLIFIED_CHINESE)
      }
      isInitialized = true
      setupListener()
    } else {
      Log.e("ChineseTtsManager", "TTS initialization failed status=$status")
    }
  }

  private fun setupListener() {
    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) {
        _isPlaying.value = true
      }

      override fun onDone(utteranceId: String?) {
        _isPlaying.value = false
        _currentPlayingText.value = ""
      }

      override fun onError(utteranceId: String?) {
        _isPlaying.value = false
        _currentPlayingText.value = ""
      }
    })
  }

  fun speak(text: String, speed: Float = 0.85f, isFemaleVoice: Boolean = true) {
    if (!isInitialized || tts == null) return
    stop()
    tts?.setSpeechRate(speed)
    tts?.setPitch(if (isFemaleVoice) 1.15f else 0.85f)
    _currentPlayingText.value = text

    val params = Bundle()
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "HANYUMATE_TTS_${System.currentTimeMillis()}")
  }

  fun stop() {
    tts?.stop()
    _isPlaying.value = false
    _currentPlayingText.value = ""
  }

  fun shutdown() {
    tts?.stop()
    tts?.shutdown()
    tts = null
  }
}
