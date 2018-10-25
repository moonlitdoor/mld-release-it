package com.moonlitdoor.release.it.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.json.RepoJson

@Entity(tableName = "repo")
data class RepoEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String
) {
  companion object {
    fun from(json: RepoJson) = RepoEntity(
      name = json.name
    )
  }
}
