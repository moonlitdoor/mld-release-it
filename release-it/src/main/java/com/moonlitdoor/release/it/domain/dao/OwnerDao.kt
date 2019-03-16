package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.OwnerEntity

@Dao
interface OwnerDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(entity: OwnerEntity): Long

  @Query("SELECT * FROM owner")
  fun getOwners(): LiveData<List<OwnerEntity>>

}