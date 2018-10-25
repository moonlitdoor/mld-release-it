package com.moonlitdoor.release.it.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moonlitdoor.release.it.domain.entity.RepoEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity

//@TypeConverters(value = [])
@Database(
  version = Migrations.VERSION,
  entities = [
    UserEntity::class,
    RepoEntity::class
  ]
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
  abstract fun repoDao(): RepoDao

  companion object {
    const val DATABASE_NAME = "release-it.db"
  }

}
