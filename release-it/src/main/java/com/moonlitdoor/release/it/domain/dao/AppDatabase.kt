package com.moonlitdoor.release.it.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moonlitdoor.release.it.domain.dao.converter.LocalDateTimeConverter
import com.moonlitdoor.release.it.domain.dao.converter.RepositoryPermissionConverter
import com.moonlitdoor.release.it.domain.dao.converter.UriConverter
import com.moonlitdoor.release.it.domain.dao.converter.ZonedDateTimeConverter
import com.moonlitdoor.release.it.domain.entity.BranchEntity
import com.moonlitdoor.release.it.domain.entity.OwnerEntity
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity

@TypeConverters(
  value = [
    LocalDateTimeConverter::class,
    ZonedDateTimeConverter::class,
    UriConverter::class,
    RepositoryPermissionConverter::class
  ]
)
@Database(
  version = Migrations.VERSION,
  entities = [
    OwnerEntity::class,
    RepositoryEntity::class,
    BranchEntity::class,
    ReleaseEntity::class
  ]
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun branchDao(): BranchDao
  abstract fun ownerDao(): OwnerDao
  abstract fun repositoryDao(): RepositoryDao
  abstract fun releaseDao(): ReleaseDao

  companion object {
    const val DATABASE_NAME = "release-it.db"
  }

}
