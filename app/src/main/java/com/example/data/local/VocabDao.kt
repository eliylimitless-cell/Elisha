package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MasteryStatus
import com.example.data.model.VocabItem
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {
  @Query("SELECT * FROM vocab_items ORDER BY hskLevel ASC, id ASC")
  fun getAllVocab(): Flow<List<VocabItem>>

  @Query("SELECT * FROM vocab_items WHERE hskLevel = :level ORDER BY id ASC")
  fun getVocabByLevel(level: Int): Flow<List<VocabItem>>

  @Query("SELECT * FROM vocab_items WHERE isStruggled = 1 ORDER BY lastReviewedTimestamp DESC")
  fun getStruggledVocab(): Flow<List<VocabItem>>

  @Query("SELECT * FROM vocab_items WHERE masteryStatus = :status ORDER BY id ASC")
  fun getVocabByMastery(status: MasteryStatus): Flow<List<VocabItem>>

  @Query("SELECT COUNT(*) FROM vocab_items")
  suspend fun getVocabCount(): Int

  @Query("SELECT COUNT(*) FROM vocab_items WHERE masteryStatus = 'MASTERED'")
  fun getMasteredCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVocab(item: VocabItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<VocabItem>)

  @Update
  suspend fun updateVocab(item: VocabItem)

  @Query("UPDATE vocab_items SET masteryStatus = :status, reviewCount = reviewCount + 1, lastReviewedTimestamp = :timestamp WHERE id = :id")
  suspend fun updateReviewStatus(id: Long, status: MasteryStatus, timestamp: Long)

  @Query("UPDATE vocab_items SET isStruggled = :struggled WHERE id = :id")
  suspend fun setStruggled(id: Long, struggled: Boolean)

  @Query("DELETE FROM vocab_items WHERE id = :id")
  suspend fun deleteById(id: Long)
}
