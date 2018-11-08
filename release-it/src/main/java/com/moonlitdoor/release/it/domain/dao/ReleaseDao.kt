package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity

@Dao
interface ReleaseDao {

  @Query("SELECT * FROM releases WHERE repo_id == :id")
  fun get(id: Long): LiveData<List<ReleaseEntity>>

}