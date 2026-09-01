package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class GeminiTutorService {
  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  suspend fun chatWithTutor(
    userMessage: String,
    learnerHskLevel: Int,
    chatHistory: List<ChatMessage>
  ): TutorParsedResponse = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.trim()
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext generateFallbackTutorResponse(userMessage, learnerHskLevel)
    }

    try {
      val systemPrompt = """
You are HanyuMate's AI Chinese Language Tutor (named 小林 Xiao Lin).
Learner target level: HSK $learnerHskLevel (HSK 3.0 standard).

MANDATORY RULES:
1. SCOPE RESTRICTION: You MUST ONLY answer topics related to the Chinese language (vocabulary, grammar, pronunciation, pinyin, Chinese culture in language context, translation, and HSK test prep). If the user asks about off-topic subjects (e.g. programming, gaming, celebrity gossip, world politics), politely decline in simple Chinese and English, gently redirecting them back to learning Chinese.
2. ADAPTIVE LEVEL SCAFFOLDING:
   - HSK 1-3: Keep sentences short, high-frequency words, gentle tone, encourage the learner.
   - HSK 4-6: Natural conversational speed, compound sentences, useful connectors (不但...而且, 根据, 积累).
   - HSK 7-9: Advanced register, idioms (成语), nuanced cultural expressions.
3. GENTLE IN-FLOW CORRECTIONS: If the user made a grammar, tone, or word choice mistake in their Chinese input, continue the conversation naturally while modeling the correct form. If there is a noticeable error, specify it in the CORRECTION tag.
4. STRICT OUTPUT FORMAT: You must format your final reply strictly with these sections so the Android app can parse them:
[HANZI]
(Chinese characters for your response)
[PINYIN]
(Accurate Pinyin with tone marks)
[ENGLISH]
(Natural English translation)
[CORRECTION]
(If the user made a mistake in Chinese, write: ERROR: <what user said> | FIX: <correct phrasing> | WHY: <brief friendly 1-sentence grammar reason>. If no mistake, write: NONE)
""".trimIndent()

      val contentsList = mutableListOf<GeminiContent>()

      // Add recent history for context
      chatHistory.takeLast(4).forEach { msg ->
        val roleStr = if (msg.role == MessageRole.USER) "user" else "model"
        contentsList.add(
          GeminiContent(
            role = roleStr,
            parts = listOf(GeminiPart(text = msg.hanzi))
          )
        )
      }

      // Add current user turn
      contentsList.add(
        GeminiContent(
          role = "user",
          parts = listOf(GeminiPart(text = userMessage))
        )
      )

      val requestPayload = GeminiGenerateContentRequest(
        contents = contentsList,
        systemInstruction = GeminiContent(
          parts = listOf(GeminiPart(text = systemPrompt))
        ),
        generationConfig = GeminiGenerationConfig(
          temperature = 0.7f
        )
      )

      val adapter = moshi.adapter(GeminiGenerateContentRequest::class.java)
      val jsonBody = adapter.toJson(requestPayload)

      val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val request = Request.Builder()
        .url(endpoint)
        .post(jsonBody.toRequestBody(jsonMediaType))
        .build()

      val response = okHttpClient.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (response.isSuccessful && responseBodyString.isNotEmpty()) {
        val responseAdapter = moshi.adapter(GeminiGenerateContentResponse::class.java)
        val parsedJson = responseAdapter.fromJson(responseBodyString)
        val rawText = parsedJson?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        if (!rawText.isNullOrBlank()) {
          return@withContext parseStructuredTutorOutput(rawText, learnerHskLevel)
        }
      }
      return@withContext generateFallbackTutorResponse(userMessage, learnerHskLevel)
    } catch (e: Exception) {
      Log.e("GeminiTutorService", "API call failed, using fallback", e)
      return@withContext generateFallbackTutorResponse(userMessage, learnerHskLevel)
    }
  }

  suspend fun translateText(text: String, toChinese: Boolean): TutorParsedResponse = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.trim()
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext if (toChinese) {
        TutorParsedResponse(
          hanzi = "学习汉语很有意思！",
          pinyin = "Xuéxí Hànyǔ hěn yǒu yìsi!",
          english = text
        )
      } else {
        TutorParsedResponse(
          hanzi = text,
          pinyin = "Nǐ hǎo",
          english = "Hello / How are you"
        )
      }
    }

    try {
      val prompt = if (toChinese) {
        "Translate this English text into natural Chinese (HSK appropriate). Provide Hanzi, Pinyin with tones, and English.\n[TEXT]: $text\n\nFormat output strictly as:\n[HANZI]\n<Chinese>\n[PINYIN]\n<Pinyin with tone marks>\n[ENGLISH]\n<English>"
      } else {
        "Translate this Chinese text into English. Provide Hanzi, Pinyin with tones, and English.\n[TEXT]: $text\n\nFormat output strictly as:\n[HANZI]\n<Chinese>\n[PINYIN]\n<Pinyin with tone marks>\n[ENGLISH]\n<English>"
      }

      val requestPayload = GeminiGenerateContentRequest(
        contents = listOf(
          GeminiContent(role = "user", parts = listOf(GeminiPart(text = prompt)))
        )
      )

      val adapter = moshi.adapter(GeminiGenerateContentRequest::class.java)
      val jsonBody = adapter.toJson(requestPayload)
      val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val request = Request.Builder().url(endpoint).post(jsonBody.toRequestBody(jsonMediaType)).build()
      val response = okHttpClient.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (response.isSuccessful && responseBodyString.isNotEmpty()) {
        val responseAdapter = moshi.adapter(GeminiGenerateContentResponse::class.java)
        val parsedJson = responseAdapter.fromJson(responseBodyString)
        val rawText = parsedJson?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        if (!rawText.isNullOrBlank()) {
          return@withContext parseStructuredTutorOutput(rawText, 1)
        }
      }
      generateFallbackTutorResponse(text, 1)
    } catch (e: Exception) {
      generateFallbackTutorResponse(text, 1)
    }
  }

  suspend fun explainGrammar(query: String, level: Int): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.trim()
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext "In Chinese grammar, '$query' is used to connect ideas or indicate aspect. For HSK $level learners, remember word order follows Subject + Time + Location + Verb + Object. Always use the appropriate measure word with numbers."
    }

    try {
      val prompt = "Explain the Chinese grammar concept or phrase '$query' for an HSK $level learner in clear, simple English. Include formula, key difference/reasoning, common mistakes, and 2 example sentences with Hanzi, Pinyin, and English."
      val requestPayload = GeminiGenerateContentRequest(
        contents = listOf(GeminiContent(role = "user", parts = listOf(GeminiPart(text = prompt))))
      )
      val adapter = moshi.adapter(GeminiGenerateContentRequest::class.java)
      val jsonBody = adapter.toJson(requestPayload)
      val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val request = Request.Builder().url(endpoint).post(jsonBody.toRequestBody(jsonMediaType)).build()
      val response = okHttpClient.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (response.isSuccessful && responseBodyString.isNotEmpty()) {
        val responseAdapter = moshi.adapter(GeminiGenerateContentResponse::class.java)
        val parsedJson = responseAdapter.fromJson(responseBodyString)
        val rawText = parsedJson?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        if (!rawText.isNullOrBlank()) {
          return@withContext rawText
        }
      }
      "Grammar Explanation for $query (HSK $level): Pay special attention to word order and tone consistency."
    } catch (e: Exception) {
      "Grammar Explanation for $query (HSK $level): Pay special attention to word order and tone consistency."
    }
  }

  suspend fun summarizeChineseText(passage: String, targetLevel: Int): TutorParsedResponse = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.trim()
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext TutorParsedResponse(
        hanzi = "这篇文章主要讲了中国语言和文化的魅力。通过每天积累词汇，我们可以更好地交流。",
        pinyin = "Zhè piān wénzhāng zhǔyào jiǎng le Zhōngguó yǔyán hé wénhuà de mèilì. Tōngguò měitiān jīlěi cíhuì, wǒmen kěyǐ gèng hǎo de jiāoliú.",
        english = "This passage mainly discusses the charm of Chinese language and culture. By accumulating words daily, we communicate better."
      )
    }

    try {
      val prompt = """
Summarize and simplify the following Chinese passage so that an HSK $targetLevel learner can easily comprehend it. Provide simplified Hanzi summary, Pinyin with tone marks, and English translation.
Passage:
$passage

Format output strictly as:
[HANZI]
<Simplified Chinese Summary>
[PINYIN]
<Pinyin with tone marks>
[ENGLISH]
<English translation>
""".trimIndent()

      val requestPayload = GeminiGenerateContentRequest(
        contents = listOf(GeminiContent(role = "user", parts = listOf(GeminiPart(text = prompt))))
      )
      val adapter = moshi.adapter(GeminiGenerateContentRequest::class.java)
      val jsonBody = adapter.toJson(requestPayload)
      val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val request = Request.Builder().url(endpoint).post(jsonBody.toRequestBody(jsonMediaType)).build()
      val response = okHttpClient.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (response.isSuccessful && responseBodyString.isNotEmpty()) {
        val responseAdapter = moshi.adapter(GeminiGenerateContentResponse::class.java)
        val parsedJson = responseAdapter.fromJson(responseBodyString)
        val rawText = parsedJson?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        if (!rawText.isNullOrBlank()) {
          return@withContext parseStructuredTutorOutput(rawText, targetLevel)
        }
      }
      generateFallbackTutorResponse("Summary of passage", targetLevel)
    } catch (e: Exception) {
      generateFallbackTutorResponse("Summary of passage", targetLevel)
    }
  }

  private fun parseStructuredTutorOutput(text: String, level: Int): TutorParsedResponse {
    var hanzi = ""
    var pinyin = ""
    var english = ""
    var hasCorrection = false
    var correctionSnippet = ""
    var correctionExplanation = ""

    val hanziMatch = Regex("\\[HANZI\\]([\\s\\S]*?)(?=\\[PINYIN\\]|\\[ENGLISH\\]|\\[CORRECTION\\]|$)").find(text)
    val pinyinMatch = Regex("\\[PINYIN\\]([\\s\\S]*?)(?=\\[HANZI\\]|\\[ENGLISH\\]|\\[CORRECTION\\]|$)").find(text)
    val englishMatch = Regex("\\[ENGLISH\\]([\\s\\S]*?)(?=\\[HANZI\\]|\\[PINYIN\\]|\\[CORRECTION\\]|$)").find(text)
    val correctionMatch = Regex("\\[CORRECTION\\]([\\s\\S]*?)$").find(text)

    if (hanziMatch != null) hanzi = hanziMatch.groupValues[1].trim()
    if (pinyinMatch != null) pinyin = pinyinMatch.groupValues[1].trim()
    if (englishMatch != null) english = englishMatch.groupValues[1].trim()

    val corrText = correctionMatch?.groupValues?.getOrNull(1)?.trim().orEmpty()
    if (corrText.isNotEmpty() && !corrText.contains("NONE", ignoreCase = true)) {
      hasCorrection = true
      correctionSnippet = corrText
      correctionExplanation = corrText
    }

    if (hanzi.isEmpty()) {
      hanzi = text.trim()
      pinyin = ""
      english = ""
    }

    return TutorParsedResponse(
      hanzi = hanzi,
      pinyin = pinyin,
      english = english,
      hasCorrection = hasCorrection,
      correctionSnippet = correctionSnippet,
      correctionExplanation = correctionExplanation
    )
  }

  private fun generateFallbackTutorResponse(query: String, level: Int): TutorParsedResponse {
    val lower = query.lowercase().trim()

    // Topic restriction check
    if (lower.contains("python") || lower.contains("javascript") || lower.contains("bitcoin") || lower.contains("crypto") || lower.contains("football scores") || lower.contains("politics")) {
      return TutorParsedResponse(
        hanzi = "抱歉，我是一名专注中文学习的AI导师。让我们聊聊汉语、汉字或HSK考试吧！你想学习什么主题？",
        pinyin = "Bàoqiàn, wǒ shì yì míng zhuānzhù Zhōngwén xuéxí de AI dǎoshī. Ràng wǒmen liáo liao Hànyǔ, Hànzì huò HSK kǎoshì ba! Nǐ xiǎng xuéxí shénme zhǔtí?",
        english = "Sorry, I am an AI tutor dedicated to Chinese language learning. Let's talk about Mandarin, characters, or HSK exam preparation! What topic would you like to study?"
      )
    }

    if (lower.contains("你好") || lower.contains("hello") || lower.contains("hi")) {
      return TutorParsedResponse(
        hanzi = "你好！很高兴和你聊天。今天你的中文学习目标是什么呢？",
        pinyin = "Nǐ hǎo! Hěn gāoxìng hé nǐ liáotiān. Jīntiān nǐ de Zhōngwén xuéxí mùbiāo shì shénme ne?",
        english = "Hello! Very happy to chat with you. What is your Chinese learning goal for today?"
      )
    }

    if (lower.contains("了") || lower.contains("过") || lower.contains("le vs guo")) {
      return TutorParsedResponse(
        hanzi = "“了”通常表示动作已经完成（例如：我吃了晚饭），而“过”强调过去的经历（例如：我去过中国）。",
        pinyin = "\"le\" tōngcháng biǎoshì dòngzuò yǐjīng wánchéng, ér \"guò\" qiángdiào guòqù de jīnglì.",
        english = "'le' usually indicates an action is completed, while 'guò' emphasizes past lifetime experience.",
        hasCorrection = true,
        correctionSnippet = "Tip: 了 vs 过",
        correctionExplanation = "Remember: 'Verb + 了' indicates finished event; 'Verb + 过' indicates experienced at least once."
      )
    }

    if (lower.contains("hsk") || lower.contains("exam") || lower.contains("test")) {
      return TutorParsedResponse(
        hanzi = "HSK 3.0标准分九个级别：HSK 1-3是初级，HSK 4-6是中级，HSK 7-9是高级。从HSK 3开始考察口语能力！",
        pinyin = "HSK 3.0 biāozhǔn fēn jiǔ gè jíbié: HSK 1-3 shì chūjí, HSK 4-6 shì zhōngjí, HSK 7-9 shì gāojí. Cóng HSK 3 kāishǐ kǎochá kǒuyǔ nénglì!",
        english = "The HSK 3.0 standard has 9 levels: HSK 1–3 is Elementary, HSK 4–6 is Intermediate, HSK 7–9 is Advanced. Speaking is tested starting from HSK 3!"
      )
    }

    if (lower.contains("zimbabwe") || lower.contains("津巴布韦") || lower.contains("ecocash")) {
      return TutorParsedResponse(
        hanzi = "津巴布韦 (Jīnbābùwéi) 的很多朋友都在学习中文。无论你在哈拉雷还是世界任何地方，HanyuMate都能通过手机支付和便捷离线学习陪伴你！",
        pinyin = "Jīnbābùwéi de hěn duō péngyou dōu zài xuéxí Zhōngwén. Wúlùn nǐ zài Hālāléi háishì shìjiè rènhé dìfang, HanyuMate dōu néng péibàn nǐ!",
        english = "Many friends in Zimbabwe are studying Chinese. Whether in Harare or anywhere in the world, HanyuMate is here with mobile money and easy learning!"
      )
    }

    // Default encouragement level-adapted response
    return if (level <= 2) {
      TutorParsedResponse(
        hanzi = "你说得很好！我们继续练习吧。你可以尝试用中文说一句话，我会帮助你检查语法。",
        pinyin = "Nǐ shuō de hěn hǎo! Wǒmen jìxù liànxí ba. Nǐ kěyǐ chángshì yòng Zhōngwén shuō yí jù huà, wǒ huì bāngzhù nǐ jiǎnchá yǔfǎ.",
        english = "You said that very well! Let's keep practicing. You can try saying a sentence in Chinese, and I'll help you check the grammar."
      )
    } else {
      TutorParsedResponse(
        hanzi = "很有深度的想法！在中文表达中，逻辑连贯性和成语的运用能够让表达更加地道。请问你想进一步讨论语法细节还是词汇搭配？",
        pinyin = "Hěn yǒu shēndù de xiǎngfǎ! Zài Zhōngwén biǎodá zhōng, luóji liánguànxìng hé chéngyǔ de yùnyòng nénggòu ràng biǎodá gèngjiā dìdao. Qǐngwèn nǐ xiǎng jìnyíbù tǎolùn yǔfǎ xìjié háishì cíhuì dāpèi?",
        english = "A profound thought! In Chinese expression, logical coherence and idioms make communication much more authentic. Would you like to discuss grammar details or vocabulary collocations further?"
      )
    }
  }
}
