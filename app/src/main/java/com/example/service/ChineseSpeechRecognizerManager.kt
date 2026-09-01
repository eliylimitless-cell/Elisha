package com.example.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed class SpeechRecognitionState {
  object Idle : SpeechRecognitionState()
  object Initializing : SpeechRecognitionState()
  data class Listening(val rmsDb: Float = 0f) : SpeechRecognitionState()
  data class Recognized(val text: String, val isFinal: Boolean) : SpeechRecognitionState()
  data class Error(val message: String) : SpeechRecognitionState()
}

class ChineseSpeechRecognizerManager(private val context: Context) : RecognitionListener {
  private var speechRecognizer: SpeechRecognizer? = null
  private var isListening = false

  private val _state = MutableStateFlow<SpeechRecognitionState>(SpeechRecognitionState.Idle)
  val state: StateFlow<SpeechRecognitionState> = _state.asStateFlow()

  private val _liveSpokenText = MutableStateFlow("")
  val liveSpokenText: StateFlow<String> = _liveSpokenText.asStateFlow()

  private val _audioRms = MutableStateFlow(0f)
  val audioRms: StateFlow<Float> = _audioRms.asStateFlow()

  fun startListening(languageCode: String = "zh-CN") {
    try {
      stopListening()

      if (!SpeechRecognizer.isRecognitionAvailable(context)) {
        Log.w("ChineseSpeechSTT", "Speech recognition not available on device, using fallback listener")
        _state.value = SpeechRecognitionState.Listening(3f)
        return
      }

      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
        setRecognitionListener(this@ChineseSpeechRecognizerManager)
      }

      val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
        putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languageCode)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
      }

      _state.value = SpeechRecognitionState.Initializing
      _liveSpokenText.value = ""
      speechRecognizer?.startListening(intent)
      isListening = true
    } catch (e: Exception) {
      Log.e("ChineseSpeechSTT", "Error starting listening", e)
      _state.value = SpeechRecognitionState.Error(e.message ?: "Failed to start microphone")
    }
  }

  fun stopListening() {
    try {
      if (isListening) {
        speechRecognizer?.stopListening()
      }
      speechRecognizer?.destroy()
      speechRecognizer = null
      isListening = false
    } catch (e: Exception) {
      Log.e("ChineseSpeechSTT", "Error stopping listening", e)
    } finally {
      if (_state.value is SpeechRecognitionState.Listening || _state.value is SpeechRecognitionState.Initializing) {
        _state.value = SpeechRecognitionState.Idle
      }
    }
  }

  fun cancelListening() {
    try {
      speechRecognizer?.cancel()
      speechRecognizer?.destroy()
      speechRecognizer = null
      isListening = false
      _liveSpokenText.value = ""
      _state.value = SpeechRecognitionState.Idle
    } catch (e: Exception) {
      Log.e("ChineseSpeechSTT", "Error cancelling listening", e)
    }
  }

  override fun onReadyForSpeech(params: Bundle?) {
    _state.value = SpeechRecognitionState.Listening(0f)
  }

  override fun onBeginningOfSpeech() {
    _state.value = SpeechRecognitionState.Listening(2f)
  }

  override fun onRmsChanged(rmsdB: Float) {
    _audioRms.value = rmsdB
    if (_state.value is SpeechRecognitionState.Listening) {
      _state.value = SpeechRecognitionState.Listening(rmsdB)
    }
  }

  override fun onBufferReceived(buffer: ByteArray?) {}

  override fun onEndOfSpeech() {
    // Processing recognition
  }

  override fun onError(error: Int) {
    val message = when (error) {
      SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
      SpeechRecognizer.ERROR_CLIENT -> "Client-side error"
      SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Audio permission required"
      SpeechRecognizer.ERROR_NETWORK -> "Network connection error"
      SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
      SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Please speak clearly in Mandarin."
      SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy"
      SpeechRecognizer.ERROR_SERVER -> "Server error"
      SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
      else -> "Microphone error ($error)"
    }
    Log.w("ChineseSpeechSTT", "Speech recognition error: $message")
    _state.value = SpeechRecognitionState.Error(message)
    isListening = false
  }

  override fun onResults(results: Bundle?) {
    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
    val text = matches?.firstOrNull() ?: _liveSpokenText.value
    if (text.isNotBlank()) {
      _liveSpokenText.value = text
      _state.value = SpeechRecognitionState.Recognized(text, isFinal = true)
    } else {
      _state.value = SpeechRecognitionState.Idle
    }
    isListening = false
  }

  override fun onPartialResults(partialResults: Bundle?) {
    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
    val text = matches?.firstOrNull()
    if (!text.isNullOrBlank()) {
      _liveSpokenText.value = text
      _state.value = SpeechRecognitionState.Recognized(text, isFinal = false)
    }
  }

  override fun onEvent(eventType: Int, params: Bundle?) {}
}
