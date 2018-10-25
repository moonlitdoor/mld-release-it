package com.moonlitdoor.release.it.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0
)