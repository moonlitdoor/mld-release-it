package com.moonlitdoor.release.it.domain.dao.converter

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime

class LocalDateTimeConverter {

  @TypeConverter
  fun convert(localDateTime: LocalDateTime): String = localDateTime.toString()

  @TypeConverter
  fun convert(string: String): LocalDateTime = LocalDateTime.parse(string)

}