package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity

@Dao
interface RepositoryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(repository: RepositoryEntity): Long

  @Query("SELECT * FROM repository")
  fun getRepos(): LiveData<List<RepositoryEntity>>

  @Query("SELECT * FROM repository WHERE id == :id")
  fun getRepo(id: Long): LiveData<RepositoryEntity>

}