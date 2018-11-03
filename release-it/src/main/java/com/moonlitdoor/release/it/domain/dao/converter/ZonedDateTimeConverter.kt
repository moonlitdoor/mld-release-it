package com.moonlitdoor.release.it.domain.dao.converter

import androidx.room.TypeConverter
import org.threeten.bp.ZonedDateTime

class ZonedDateTimeConverter {

  @TypeConverter
  fun convert(zonedDateTime: ZonedDateTime): String = zonedDateTime.toString()

  @TypeConverter
  fun convert(string: String): ZonedDateTime = ZonedDateTime.parse(string)

}