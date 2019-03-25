package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity

@Dao
interface ReleaseDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(release: ReleaseEntity)

  @Query("SELECT * FROM releases WHERE repository_id == :repositoryId")
  fun get(repositoryId: Long): LiveData<List<ReleaseEntity>>

}