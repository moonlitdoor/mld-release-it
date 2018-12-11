package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.UserEntity

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(entity: UserEntity): Long

  @Query("SELECT * FROM user")
  fun getUsers(): LiveData<List<UserEntity>>

}