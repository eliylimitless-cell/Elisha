package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  suspend fun getUserProfileOnce(): UserProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(profile: UserProfile)

  @Update
  suspend fun update(profile: UserProfile)

  @Query("UPDATE user_profile SET currentHskLevel = :level WHERE id = 1")
  suspend fun updateHskLevel(level: Int)

  @Query("UPDATE user_profile SET isProUnlocked = :isPro WHERE id = 1")
  suspend fun setProUnlocked(isPro: Boolean)

  @Query("UPDATE user_profile SET wordsLearnedCount = wordsLearnedCount + :count WHERE id = 1")
  suspend fun incrementWordsLearned(count: Int)
}
