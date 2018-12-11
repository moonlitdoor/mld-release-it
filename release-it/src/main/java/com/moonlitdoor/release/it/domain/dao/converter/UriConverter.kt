package com.moonlitdoor.release.it.domain.dao.converter

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter {

  @TypeConverter
  fun convert(uri: Uri): String = uri.toString()

  @TypeConverter
  fun convert(string: String): Uri = Uri.parse(string)

}