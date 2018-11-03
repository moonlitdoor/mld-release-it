package com.moonlitdoor.release.it.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moonlitdoor.release.it.domain.dao.converter.LocalDateTimeConverter
import com.moonlitdoor.release.it.domain.dao.converter.ZonedDateTimeConverter
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepoEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity

@TypeConverters(
  value = [
    LocalDateTimeConverter::class,
    ZonedDateTimeConverter::class
  ]
)
@Database(
  version = Migrations.VERSION,
  entities = [
    UserEntity::class,
    RepoEntity::class,
    ReleaseEntity::class
  ]
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
  abstract fun repoDao(): RepoDao
  abstract fun serviceDao(): ServiceDao

  companion object {
    const val DATABASE_NAME = "release-it.db"
  }

}
