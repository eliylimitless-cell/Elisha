package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.data.model.VocabItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [VocabItem::class, ChatMessage::class, UserProfile::class],
  version = 1,
  exportSchema = false
)
abstract class HanyuDatabase : RoomDatabase() {
  abstract fun vocabDao(): VocabDao
  abstract fun chatMessageDao(): ChatMessageDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile
    private var INSTANCE: HanyuDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): HanyuDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          HanyuDatabase::class.java,
          "hanyumate_database"
        )
          .fallbackToDestructiveMigration()
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              scope.launch(Dispatchers.IO) {
                populateInitialData(getDatabase(context, scope))
              }
            }
          })
          .build()
        INSTANCE = instance
        instance
      }
    }

    private suspend fun populateInitialData(database: HanyuDatabase) {
      // Seed user profile
      val userDao = database.userDao()
      if (userDao.getUserProfileOnce() == null) {
        userDao.insertOrUpdate(UserProfile())
      }

      // Seed vocabulary
      val vocabDao = database.vocabDao()
      if (vocabDao.getVocabCount() == 0) {
        vocabDao.insertAll(InitialCurriculumData.INITIAL_VOCABULARY)
      }

      // Seed welcoming chat message
      val chatDao = database.chatMessageDao()
      chatDao.insertMessage(
        ChatMessage(
          role = com.example.data.model.MessageRole.ASSISTANT,
          hanzi = "你好！我是你的AI中文导师小林 (Xiao Lin)。今天你想练习什么？你可以随时打字、发语音或者问我任何语法和HSK问题！",
          pinyin = "Nǐ hǎo! Wǒ shì nǐ de AI Zhōngwén dǎoshī Xiǎolín. Jīntiān nǐ xiǎng liànxí shénme? Nǐ kěyǐ suíshí dǎzì, fā yǔyīn huòzhě wèn wǒ rènhé yǔfǎ hé HSK wèntí!",
          english = "Hello! I am your AI Chinese tutor Xiao Lin. What would you like to practice today? You can type, speak, or ask me any grammar and HSK questions anytime!",
          hskLevel = 1
        )
      )
    }
  }
}
