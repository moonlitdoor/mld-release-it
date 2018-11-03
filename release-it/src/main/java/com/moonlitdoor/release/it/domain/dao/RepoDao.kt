package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.RepoEntity

@Dao
interface RepoDao {

  @Query("SELECT * FROM repo")
  fun getRepos(): LiveData<List<RepoEntity>>

}