package com.example.data.local

import com.example.data.model.DrillType
import com.example.data.model.ExamQuestion
import com.example.data.model.LessonUnit
import com.example.data.model.MasteryStatus
import com.example.data.model.VocabItem

object InitialCurriculumData {
  val INITIAL_VOCABULARY = listOf(
    // HSK 1
    VocabItem(
      hanzi = "你好",
      pinyin = "nǐ hǎo",
      english = "Hello / Hi",
      partOfSpeech = "Greeting",
      hskLevel = 1,
      exampleChinese = "你好！很高兴认识你。",
      examplePinyin = "Nǐ hǎo! Hěn gāoxìng rènshi nǐ.",
      exampleEnglish = "Hello! Nice to meet you.",
      masteryStatus = MasteryStatus.MASTERED,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "谢谢",
      pinyin = "xièxie",
      english = "Thank you / Thanks",
      partOfSpeech = "Verb",
      hskLevel = 1,
      exampleChinese = "谢谢你的帮助。",
      examplePinyin = "Xièxie nǐ de bāngzhù.",
      exampleEnglish = "Thank you for your help.",
      masteryStatus = MasteryStatus.MASTERED,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "中国",
      pinyin = "Zhōngguó",
      english = "China",
      partOfSpeech = "Proper Noun",
      hskLevel = 1,
      exampleChinese = "我想去中国旅游。",
      examplePinyin = "Wǒ xiǎng qù Zhōngguó lǚyóu.",
      exampleEnglish = "I want to travel to China.",
      masteryStatus = MasteryStatus.LEARNING,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "学习",
      pinyin = "xuéxí",
      english = "To study / To learn",
      partOfSpeech = "Verb",
      hskLevel = 1,
      exampleChinese = "我们一起学习汉语吧。",
      examplePinyin = "Wǒmen yìqǐ xuéxí Hànyǔ ba.",
      exampleEnglish = "Let's study Chinese together.",
      masteryStatus = MasteryStatus.LEARNING,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "朋友",
      pinyin = "péngyou",
      english = "Friend",
      partOfSpeech = "Noun",
      hskLevel = 1,
      exampleChinese = "他是我的好朋友。",
      examplePinyin = "Tā shì wǒ de hǎo péngyou.",
      exampleEnglish = "He is my good friend.",
      masteryStatus = MasteryStatus.MASTERED,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "多少",
      pinyin = "duōshao",
      english = "How much / How many",
      partOfSpeech = "Pronoun",
      hskLevel = 1,
      exampleChinese = "这个苹果多少钱？",
      examplePinyin = "Zhège píngguǒ duōshao qián?",
      exampleEnglish = "How much is this apple?",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = true
    ),
    // HSK 2
    VocabItem(
      hanzi = "喜欢",
      pinyin = "xǐhuan",
      english = "To like / To be fond of",
      partOfSpeech = "Verb",
      hskLevel = 2,
      exampleChinese = "你喜欢喝中国茶吗？",
      examplePinyin = "Nǐ xǐhuan hē Zhōngguó chá ma?",
      exampleEnglish = "Do you like drinking Chinese tea?",
      masteryStatus = MasteryStatus.LEARNING,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "准备",
      pinyin = "zhǔnbèi",
      english = "To prepare / Ready",
      partOfSpeech = "Verb / Noun",
      hskLevel = 2,
      exampleChinese = "你准备好考试了吗？",
      examplePinyin = "Nǐ zhǔnbèi hǎo kǎoshì le ma?",
      exampleEnglish = "Are you ready for the exam?",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = true
    ),
    VocabItem(
      hanzi = "帮助",
      pinyin = "bāngzhù",
      english = "To help / Assistance",
      partOfSpeech = "Verb / Noun",
      hskLevel = 2,
      exampleChinese = "互相帮助很重要。",
      examplePinyin = "Hùxiāng bāngzhù hěn zhòngyào.",
      exampleEnglish = "Helping each other is very important.",
      masteryStatus = MasteryStatus.LEARNING,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "身体",
      pinyin = "shēntǐ",
      english = "Health / Body",
      partOfSpeech = "Noun",
      hskLevel = 2,
      exampleChinese = "祝你身体健康！",
      examplePinyin = "Zhù nǐ shēntǐ jiànkāng!",
      exampleEnglish = "Wishing you good health!",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    ),
    // HSK 3
    VocabItem(
      hanzi = "解决",
      pinyin = "jiějué",
      english = "To solve / To settle (a problem)",
      partOfSpeech = "Verb",
      hskLevel = 3,
      exampleChinese = "这个问题很难解决。",
      examplePinyin = "Zhège wèntí hěn nán jiějué.",
      exampleEnglish = "This problem is hard to solve.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = true
    ),
    VocabItem(
      hanzi = "热情",
      pinyin = "rèqíng",
      english = "Enthusiastic / Warm-hearted",
      partOfSpeech = "Adjective",
      hskLevel = 3,
      exampleChinese = "大家都很热情地欢迎我们。",
      examplePinyin = "Dàjiā dōu hěn rèqíng de huānyíng wǒmen.",
      exampleEnglish = "Everyone welcomed us warmly.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "环境",
      pinyin = "huánjìng",
      english = "Environment / Surroundings",
      partOfSpeech = "Noun",
      hskLevel = 3,
      exampleChinese = "我们要保护生态环境。",
      examplePinyin = "Wǒmen yào bǎohù shēngtài huánjìng.",
      exampleEnglish = "We must protect the ecological environment.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    ),
    // HSK 4
    VocabItem(
      hanzi = "根据",
      pinyin = "gēnjù",
      english = "According to / Based on / Basis",
      partOfSpeech = "Preposition / Noun",
      hskLevel = 4,
      exampleChinese = "根据调查，很多年轻人喜欢在线学习。",
      examplePinyin = "Gēnjù diàochá, hěn duō niánqīngrén xǐhuan zàixiàn xuéxí.",
      exampleEnglish = "According to the survey, many young people like online learning.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    ),
    VocabItem(
      hanzi = "积累",
      pinyin = "jīlěi",
      english = "To accumulate / Cumulative experience",
      partOfSpeech = "Verb / Noun",
      hskLevel = 4,
      exampleChinese = "学外语需要每天积累词汇。",
      examplePinyin = "Xué wàiyǔ xūyào měitiān jīlěi cíhuì.",
      exampleEnglish = "Learning a foreign language requires accumulating vocabulary daily.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = true
    ),
    // HSK 5
    VocabItem(
      hanzi = "潜移默化",
      pinyin = "qián yí mò huà",
      english = "Imperceptible influence / Subtly influenced",
      partOfSpeech = "Chengyu Idiom",
      hskLevel = 5,
      exampleChinese = "阅读对人的影响是潜移默化的。",
      examplePinyin = "Yuèdú duì rén de yǐngxiǎng shì qián yí mò huà de.",
      exampleEnglish = "The influence of reading on a person is imperceptible and profound.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    ),
    // HSK 6
    VocabItem(
      hanzi = "阐述",
      pinyin = "chǎnshù",
      english = "To expound / Elaborate / Set forth",
      partOfSpeech = "Verb",
      hskLevel = 6,
      exampleChinese = "文章详细阐述了语言演变的规律。",
      examplePinyin = "Wénzhāng xiángxì chǎnshù le yǔyán yǎnbiàn de guīlǜ.",
      exampleEnglish = "The article elaborated in detail on the laws of linguistic evolution.",
      masteryStatus = MasteryStatus.NEW,
      isStruggled = false
    )
  )

  val LESSON_UNITS = listOf(
    LessonUnit(
      id = 1,
      hskLevel = 1,
      titleEn = "Unit 1: Greetings & Introductions",
      titleZh = "问候与介绍",
      pinyin = "Wènhòu yǔ Jièshào",
      description = "Master pinyin tones, polite greetings, and introducing your name and nationality.",
      vocabularyCount = 25,
      keySentenceChinese = "你好！我叫Alex，我是学生。",
      keySentencePinyin = "Nǐ hǎo! Wǒ jiào Alex, wǒ shì xuésheng.",
      keySentenceEnglish = "Hello! My name is Alex, I am a student.",
      isCompleted = true
    ),
    LessonUnit(
      id = 2,
      hskLevel = 1,
      titleEn = "Unit 2: Numbers, Money & Shopping",
      titleZh = "数字与购物",
      pinyin = "Shùzì yǔ Gòuwù",
      description = "Count 1–100, ask prices with 多少钱, and use the measure word 个.",
      vocabularyCount = 30,
      keySentenceChinese = "请问这个多少钱？十块钱。",
      keySentencePinyin = "Qǐngwèn zhège duōshao qián? Shí kuài qián.",
      keySentenceEnglish = "Excuse me, how much is this? Ten yuan.",
      isCompleted = true
    ),
    LessonUnit(
      id = 3,
      hskLevel = 2,
      titleEn = "Unit 3: Ordering Food & Drinks",
      titleZh = "点餐与饮料",
      pinyin = "Diǎncān yǔ Yǐnliào",
      description = "Order Chinese delicacies, express likes/dislikes (喜欢), and request the bill.",
      vocabularyCount = 35,
      keySentenceChinese = "我想点一杯热茶和一碗牛肉面。",
      keySentencePinyin = "Wǒ xiǎng diǎn yì bēi rè chá hé yì wǎn niúròumiàn.",
      keySentenceEnglish = "I'd like to order a cup of hot tea and a bowl of beef noodles.",
      isCompleted = false
    ),
    LessonUnit(
      id = 4,
      hskLevel = 2,
      titleEn = "Unit 4: Asking Directions & Transport",
      titleZh = "问路与交通",
      pinyin = "Wènlù yǔ Jiāotōng",
      description = "Navigate cities, subways, taxis, and use directional words (左边, 右边, 前面).",
      vocabularyCount = 40,
      keySentenceChinese = "地铁站在哪儿？往前走两百米。",
      keySentencePinyin = "Dìtiězhàn zài nǎr? Wǎng qián zǒu liǎng bǎi mǐ.",
      keySentenceEnglish = "Where is the subway station? Walk forward 200 meters.",
      isCompleted = false
    ),
    LessonUnit(
      id = 5,
      hskLevel = 3,
      titleEn = "Unit 5: Daily Life & Time Scheduling",
      titleZh = "日常生活与时间",
      pinyin = "Rìcháng Shēnghuó yǔ Shíjiān",
      description = "Discuss schedules, appointments, and master aspect markers 了 vs 过.",
      vocabularyCount = 50,
      keySentenceChinese = "我们明天下午两点在图书馆见面吧。",
      keySentencePinyin = "Wǒmen míngtiān xiàwǔ liǎng diǎn zài túshūguǎn jiànmiàn ba.",
      keySentenceEnglish = "Let's meet at the library tomorrow at 2 PM.",
      isCompleted = false
    ),
    LessonUnit(
      id = 6,
      hskLevel = 3,
      titleEn = "Unit 6: Travel & HSK Speaking Drills",
      titleZh = "旅行与口语实战",
      pinyin = "Lǚxíng yǔ Kǒuyǔ Shízhàn",
      description = "Plan journeys, book hotels, and practice spoken response questions for HSK 3 oral test.",
      vocabularyCount = 60,
      keySentenceChinese = "虽然天气有点冷，但是风景特别美。",
      keySentencePinyin = "Suīrán tiānqì yǒudiǎn lěng, dànshì fēngjǐng tèbié měi.",
      keySentenceEnglish = "Although the weather is a bit cold, the scenery is remarkably beautiful.",
      isCompleted = false
    )
  )

  val PLACEMENT_QUESTIONS = listOf(
    ExamQuestion(
      id = 101,
      hskLevel = 1,
      drillType = DrillType.LISTENING_CHOICE,
      promptChinese = "你好！请问你叫什么名字？",
      promptPinyin = "Nǐ hǎo! Qǐngwèn nǐ jiào shénme míngzi?",
      promptAudioText = "你好！请问你叫什么名字？",
      questionText = "What is the speaker asking?",
      options = listOf("What time is it?", "What is your name?", "Where are you from?", "How old are you?"),
      correctOptionIndex = 1,
      explanation = "“你叫什么名字？” translates to 'What is your name?'.",
      targetSkill = "Listening Comprehension"
    ),
    ExamQuestion(
      id = 102,
      hskLevel = 1,
      drillType = DrillType.PINYIN_MATCH,
      promptChinese = "谢谢你",
      promptPinyin = "xièxie nǐ",
      promptAudioText = "谢谢你",
      questionText = "Select the correct English translation for '谢谢你':",
      options = listOf("Goodbye", "Thank you", "Sorry", "You are welcome"),
      correctOptionIndex = 1,
      explanation = "“谢谢你” means 'Thank you'.",
      targetSkill = "Vocabulary"
    ),
    ExamQuestion(
      id = 103,
      hskLevel = 2,
      drillType = DrillType.READING_COMPREHENSION,
      promptChinese = "今天天气很好，我想和朋友一起去公园踢足球。",
      promptPinyin = "Jīntiān tiānqì hěn hǎo, wǒ xiǎng hé péngyou yìqǐ qù gōngyuán tī zúqiú.",
      promptAudioText = "今天天气很好，我想和朋友一起去公园踢足球。",
      questionText = "Where does the speaker want to go with their friend?",
      options = listOf("To the cinema", "To the library", "To the park to play soccer", "To the restaurant"),
      correctOptionIndex = 2,
      explanation = "“去公园踢足球” explicitly indicates going to the park to play soccer.",
      targetSkill = "Reading Comprehension"
    ),
    ExamQuestion(
      id = 104,
      hskLevel = 3,
      drillType = DrillType.READING_COMPREHENSION,
      promptChinese = "我已经在中国生活了两年，不但习惯了这里的饮食，而且结交了很多中国朋友。",
      promptPinyin = "Wǒ yǐjīng zài Zhōngguó shēnghuó le liǎng nián, búdàn xíguàn le zhèlǐ de yǐnshí, érqiě jiéjiāo le hěn duō Zhōngguó péngyou.",
      promptAudioText = "我已经在中国生活了两年，不但习惯了这里的饮食，而且结交了很多中国朋友。",
      questionText = "According to the passage, how long has the speaker lived in China?",
      options = listOf("2 months", "2 years", "2 weeks", "5 years"),
      correctOptionIndex = 1,
      explanation = "“生活了两年” specifies two years of living in China.",
      targetSkill = "Grammar & Reading"
    ),
    ExamQuestion(
      id = 105,
      hskLevel = 4,
      drillType = DrillType.SPEAKING_RECAST,
      promptChinese = "请根据你的生活经历，谈谈你为什么想学汉语？",
      promptPinyin = "Qǐng gēnjù nǐ de shēnghuó jīnglì, tán tan nǐ wèishénme xiǎng xué Hànyǔ?",
      promptAudioText = "请根据你的生活经历，谈谈你为什么想学汉语？",
      questionText = "Which Chinese response best answers why someone learns Chinese for career & travel?",
      options = listOf(
        "因为汉语很有趣，并且对我的未来工作和旅行很有帮助。",
        "我不喜欢吃苹果。",
        "昨天上午我去了火车站。",
        "现在是北京时间八点整。"
      ),
      correctOptionIndex = 0,
      explanation = "The first option logically elaborates on interest, future career, and travel.",
      targetSkill = "Oral Reasoning & Expression"
    )
  )

  val MOCK_EXAM_DRILLS = listOf(
    ExamQuestion(
      id = 201,
      hskLevel = 2,
      drillType = DrillType.LISTENING_CHOICE,
      promptChinese = "服务员，请给我们两杯温水和一份菜单。",
      promptPinyin = "Fúwùyuán, qǐng gěi wǒmen liǎng bēi wēnshuǐ hé yí fèn càidān.",
      promptAudioText = "服务员，请给我们两杯温水和一份菜单。",
      questionText = "Where is this dialogue most likely taking place?",
      options = listOf("In a hospital (医院)", "In a restaurant (餐馆)", "In an airport (机场)", "In a bank (银行)"),
      correctOptionIndex = 1,
      explanation = "Keywords '服务员' (waiter), '温水' (warm water), and '菜单' (menu) indicate a restaurant.",
      targetSkill = "HSK 2 Listening"
    ),
    ExamQuestion(
      id = 202,
      hskLevel = 3,
      drillType = DrillType.SENTENCE_REORDER,
      promptChinese = "①把 ②桌子 ③请 ④放 ⑤在 ⑥手机 ⑦上",
      promptPinyin = "qǐng bǎ shǒujī fàng zài zhuōzi shàng",
      promptAudioText = "请把手机放在桌子上。",
      questionText = "Arrange into correct '把' construction order (Please put the phone on the table):",
      options = listOf(
        "③ ① ⑥ ④ ⑤ ② ⑦ (请把手机放在桌子上)",
        "① ③ ⑤ ② ⑦ ⑥ ④",
        "⑥ ④ ⑤ ② ⑦ ③ ①",
        "③ ⑥ ① ④ ⑤ ② ⑦"
      ),
      correctOptionIndex = 0,
      explanation = "Correct syntax: Subject/Imperative (请) + 把 + Object (手机) + Verb (放) + Resultative Direction (在桌子上).",
      targetSkill = "HSK 3 Grammar"
    ),
    ExamQuestion(
      id = 203,
      hskLevel = 4,
      drillType = DrillType.READING_COMPREHENSION,
      promptChinese = "成功往往不是一蹴而就的，它需要长期的积累与坚持不懈的努力。",
      promptPinyin = "Chénggōng wǎngwǎng búshì yí cù ér jiù de, tā xūyào chángqī de jīlěi yǔ jiānchí bú xiè de nǔlì.",
      promptAudioText = "成功往往不是一蹴而就的，它需要长期的积累与坚持不懈的努力。",
      questionText = "What does the sentence suggest is essential for success?",
      options = listOf(
        "Pure luck and rapid speed",
        "Long-term accumulation and persistent effort",
        "Avoiding any hard work",
        "Only studying alone"
      ),
      correctOptionIndex = 1,
      explanation = "“长期的积累与坚持不懈的努力” means long-term accumulation and persistent effort.",
      targetSkill = "HSK 4 Reading Comprehension"
    )
  )
}
