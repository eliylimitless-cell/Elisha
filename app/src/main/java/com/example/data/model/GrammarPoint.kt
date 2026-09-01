package com.example.data.model

data class GrammarPoint(
  val id: String,
  val titleZh: String,
  val titleEn: String,
  val hskLevel: Int,
  val patternFormula: String,
  val explanation: String,
  val commonMistake: String,
  val correction: String,
  val examples: List<GrammarExample>
)

data class GrammarExample(
  val chinese: String,
  val pinyin: String,
  val english: String
)

object GrammarRepository {
  val SAMPLE_GRAMMAR_POINTS = listOf(
    GrammarPoint(
      id = "le_vs_guo",
      titleZh = "了 (le) vs 过 (guò)",
      titleEn = "Completed Action vs Past Experience",
      hskLevel = 2,
      patternFormula = "Verb + 了 (Action finished) VS Verb + 过 (Ever experienced)",
      explanation = "Use '了' when emphasizing that an action occurred or reached completion in a specific timeframe. Use '过' to express that you have ever had the experience of doing something in your life.",
      commonMistake = "我去了北京两次。(If expressing lifetime experience)",
      correction = "我去过北京两次。(I have been to Beijing twice.)",
      examples = listOf(
        GrammarExample("我吃了早饭。", "Wǒ chī le zǎofàn.", "I ate breakfast (completed)."),
        GrammarExample("我吃过北京烤鸭。", "Wǒ chī guò Běijīng kǎoyā.", "I have eaten Peking duck before (experience).")
      )
    ),
    GrammarPoint(
      id = "measure_words",
      titleZh = "量词 (Liàngcí)",
      titleEn = "Classifier / Measure Word System",
      hskLevel = 1,
      patternFormula = "Number + Measure Word + Noun",
      explanation = "In Chinese, you cannot attach a number directly to most nouns. You must insert a classifier matching the noun's shape, nature, or category.",
      commonMistake = "一个书 (Incorrect measure word)",
      correction = "一本书 (Yī běn shū) — '本' is specifically used for bound books.",
      examples = listOf(
        GrammarExample("一个人", "yí gè rén", "one person (general measure word 个)"),
        GrammarExample("两只猫", "liǎng zhī māo", "two cats (animal measure word 只)"),
        GrammarExample("三张纸", "sān zhāng zhǐ", "three sheets of paper (flat surface measure word 张)")
      )
    ),
    GrammarPoint(
      id = "ba_structure",
      titleZh = "把字句 (Bǎ Sentence)",
      titleEn = "Disposal Structure",
      hskLevel = 3,
      patternFormula = "Subject + 把 + Object + Verb + Result/Direction",
      explanation = "The 把 construction shifts the object before the verb to emphasize what action happened to the object and the resulting state or relocation.",
      commonMistake = "我放手机在桌子上。(Less natural in Mandarin)",
      correction = "我把手机放在桌子上了。(I placed the phone on the table.)",
      examples = listOf(
        GrammarExample("请把门关上。", "Qǐng bǎ mén guān shàng.", "Please close the door."),
        GrammarExample("他把作业做完了。", "Tā bǎ zuòyè zuò wán le.", "He finished his homework.")
      )
    ),
    GrammarPoint(
      id = "budan_erqie",
      titleZh = "不但...而且... (búdàn... érqiě...)",
      titleEn = "Not Only... But Also...",
      hskLevel = 3,
      patternFormula = "不但 + Clause 1 + 而且 + Clause 2",
      explanation = "Connects two clauses where the second clause provides an escalating or additional positive/negative characteristic.",
      commonMistake = "他会说中文，也很会写。(Underdeveloped conjunction)",
      correction = "他不但会说中文，而且说得非常流利。(He not only speaks Chinese, but also speaks very fluently.)",
      examples = listOf(
        GrammarExample("这里的菜不但便宜，而且很好吃。", "Zhèlǐ de cài búdàn piányi, érqiě hěn hǎochī.", "The food here is not only cheap, but also delicious.")
      )
    ),
    GrammarPoint(
      id = "chengyu_juyifansan",
      titleZh = "举一反三 (jǔ yī fǎn sān)",
      titleEn = "Idiom: Deduce many things from one instance",
      hskLevel = 5,
      patternFormula = "Chengyu 4-character idiom used as predicate or modifier",
      explanation = "Literally 'raise one corner and infer the other three'. Means to be quick-witted, learning by analogy.",
      commonMistake = "他学东西很快。(Basic vocabulary)",
      correction = "他善于举一反三，学习效率极高。(He is adept at learning by analogy; his study efficiency is exceptional.)",
      examples = listOf(
        GrammarExample("学汉语要善于举一反三。", "Xué Hànyǔ yào shànyú jǔ yī fǎn sān.", "When learning Chinese, one should be good at drawing analogies from one case.")
      )
    )
  )
}
