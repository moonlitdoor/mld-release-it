package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moonlitdoor.release.it.domain.entity.UserEntity

@Dao
interface UserDao {

  @Query("SELECT * FROM user")
  fun getUsers(): LiveData<List<UserEntity>>

  @Insert
  fun insert(entity: UserEntity)

}