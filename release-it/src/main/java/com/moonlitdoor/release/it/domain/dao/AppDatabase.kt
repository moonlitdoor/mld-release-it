package com.moonlitdoor.release.it.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moonlitdoor.release.it.domain.dao.converter.LocalDateTimeConverter
import com.moonlitdoor.release.it.domain.dao.converter.UriConverter
import com.moonlitdoor.release.it.domain.dao.converter.ZonedDateTimeConverter
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity

@TypeConverters(
  value = [
    LocalDateTimeConverter::class,
    ZonedDateTimeConverter::class,
    UriConverter::class
  ]
)
@Database(
  version = Migrations.VERSION,
  entities = [
    UserEntity::class,
    RepositoryEntity::class,
    ReleaseEntity::class
  ]
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
  abstract fun repoDao(): RepositoryDao
  abstract fun releaseDao(): ReleaseDao

  companion object {
    const val DATABASE_NAME = "release-it.db"
  }

}
