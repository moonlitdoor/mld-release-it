package com.moonlitdoor.release.it.domain.dao.converter

import androidx.room.TypeConverter
import com.moonlitdoor.release.it.domain.query.RepositoryPermission

class RepositoryPermissionConverter {

  @TypeConverter
  fun convert(permission: RepositoryPermission?) = permission?.name

  @TypeConverter
  fun convert(permission: String?) = permission?.let { RepositoryPermission.valueOf(it) }

}