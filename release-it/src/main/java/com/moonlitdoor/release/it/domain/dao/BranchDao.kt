package com.moonlitdoor.release.it.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.moonlitdoor.release.it.domain.entity.BranchEntity

@Dao
interface BranchDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(entity: BranchEntity): Long

}