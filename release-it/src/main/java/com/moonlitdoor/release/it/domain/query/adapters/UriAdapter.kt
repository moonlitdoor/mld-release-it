package com.moonlitdoor.release.it.domain.query.adapters

import android.net.Uri
import com.moonlitdoor.release.it.extension.ignore
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class UriAdapter : JsonAdapter<Uri>() {
  override fun toJson(writer: JsonWriter, value: Uri?) = writer.value(value.toString()).ignore()

  override fun fromJson(reader: JsonReader): Uri? = Uri.parse(reader.readJsonValue() as String)
}