package com.moonlitdoor.release.it.domain.dao

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object Migrations {

  private const val VERSION_1 = 1
  private const val VERSION_2 = VERSION_1 + 1
  private const val VERSION_3 = VERSION_2 + 1
  const val VERSION = VERSION_1

  val ALL = arrayOf<Migration>(
  )

  private class M(startVersion: Int, endVersion: Int, private val migration: (db: SupportSQLiteDatabase) -> Unit) : Migration(startVersion, endVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
      migration(database)
    }
  }
}
