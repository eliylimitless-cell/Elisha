package com.example.data.model

enum class HskTrack(val title: String, val subtitle: String, val levels: List<Int>) {
  BEGINNER("Beginner Track", "HSK 1–3 · Elementary (Scaffolded, slow speech, pinyin always on)", listOf(1, 2, 3)),
  INTERMEDIATE("Intermediate Track", "HSK 4–6 · Intermediate (Natural speed, stroke practice, idiomatic)", listOf(4, 5, 6)),
  ADVANCED("Advanced Track", "HSK 7–9 · Superior (Native speed, Chengyu idioms, literature)", listOf(7, 8, 9))
}

data class HskLevelInfo(
  val level: Int,
  val stage: String,
  val cumulativeVocab: Int,
  val approxCefr: String,
  val focusDescription: String,
  val hsk2Mapping: String,
  val speakingTested: Boolean,
  val sampleKeywords: List<String>
)

object HskStandards {
  val ALL_LEVELS = listOf(
    HskLevelInfo(
      level = 1,
      stage = "Elementary",
      cumulativeVocab = 300,
      approxCefr = "A1",
      focusDescription = "Pinyin phonetics, basic greetings, fundamental numbers & high-frequency nouns.",
      hsk2Mapping = "Covers Old HSK 1 + parts of Old HSK 2 (expanded vocabulary).",
      speakingTested = false,
      sampleKeywords = listOf("你好", "谢谢", "再见", "中国", "学习")
    ),
    HskLevelInfo(
      level = 2,
      stage = "Elementary",
      cumulativeVocab = 500,
      approxCefr = "A1–A2",
      focusDescription = "Simple daily conversations, asking directions, ordering food, expressing basic needs.",
      hsk2Mapping = "Equivalent to solid Old HSK 2 to early HSK 3.",
      speakingTested = false,
      sampleKeywords = listOf("明天", "喜欢", "买东西", "医院", "准备")
    ),
    HskLevelInfo(
      level = 3,
      stage = "Elementary",
      cumulativeVocab = 1000,
      approxCefr = "A2",
      focusDescription = "Extended dialogue, travel scenarios, expressing feelings. Speaking is tested from this level onward.",
      hsk2Mapping = "Old HSK 3 ≈ New HSK 3–4 boundary territory.",
      speakingTested = true,
      sampleKeywords = listOf("不但...而且", "决定", "解决", "环境", "热情")
    ),
    HskLevelInfo(
      level = 4,
      stage = "Intermediate",
      cumulativeVocab = 2000,
      approxCefr = "B1",
      focusDescription = "Discuss opinions, future plans, work life, abstract emotions, character stroke logic.",
      hsk2Mapping = "More rigorous than Old HSK 4 (2,000 vs 1,200 words).",
      speakingTested = true,
      sampleKeywords = listOf("根据", "积累", "态度", "负责", "精彩")
    ),
    HskLevelInfo(
      level = 5,
      stage = "Intermediate",
      cumulativeVocab = 3600,
      approxCefr = "B1–B2",
      focusDescription = "Reading news headlines, films, expressing nuanced views and cultural concepts.",
      hsk2Mapping = "Old HSK 5 had 2,500 words; New HSK 5 demands 3,600 words.",
      speakingTested = true,
      sampleKeywords = listOf("概念", "趋势", "不仅如此", "逻辑", "广泛")
    ),
    HskLevelInfo(
      level = 6,
      stage = "Intermediate",
      cumulativeVocab = 5400,
      approxCefr = "B2–C1",
      focusDescription = "Near-fluent daily, academic, and professional media consumption with ease.",
      hsk2Mapping = "Equivalent to Old HSK 6 mastery with broader modern register.",
      speakingTested = true,
      sampleKeywords = listOf("潜移默化", "阐述", "渊博", "推测", "宏观")
    ),
    HskLevelInfo(
      level = 7,
      stage = "Advanced",
      cumulativeVocab = 11000,
      approxCefr = "C1",
      focusDescription = "HSK 7–9 top band: Advanced literary texts, academic papers, Chengyu mastery.",
      hsk2Mapping = "New tier introduced in HSK 3.0 (no direct Old HSK equivalent).",
      speakingTested = true,
      sampleKeywords = listOf("见微知著", "博古通今", "循序渐进", "意气风发")
    ),
    HskLevelInfo(
      level = 8,
      stage = "Advanced",
      cumulativeVocab = 11000,
      approxCefr = "C1–C2",
      focusDescription = "HSK 7–9 top band: Diplomatic, legal, and specialized academic Chinese.",
      hsk2Mapping = "New tier introduced in HSK 3.0.",
      speakingTested = true,
      sampleKeywords = listOf("根深蒂固", "相辅相成", "高屋建瓴", "不可或缺")
    ),
    HskLevelInfo(
      level = 9,
      stage = "Advanced",
      cumulativeVocab = 11000,
      approxCefr = "C2",
      focusDescription = "HSK 7–9 top band: Native-level cultural fluency, classical allusions, debate.",
      hsk2Mapping = "New tier introduced in HSK 3.0 (Mastery tier).",
      speakingTested = true,
      sampleKeywords = listOf("厚德载物", "大巧若拙", "韬光养晦", "举一反三")
    )
  )

  fun getInfo(level: Int): HskLevelInfo {
    return ALL_LEVELS.find { it.level == level } ?: ALL_LEVELS[0]
  }
}
