package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.graph.UserGraph
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "user")
data class UserEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "github_id")
  val githubId: String,
  val login: String,
  val name: String?,
  val email: String,
  val created: ZonedDateTime,
  @ColumnInfo(name = "avatar_url")
  val avatarUrl: String
) {
  companion object {
    fun from(graph: UserGraph) = UserEntity(
      githubId = graph.id,
      login = graph.login,
      name = graph.name,
      email = graph.email,
      created = graph.created,
      avatarUrl = graph.avatarUrl
    )

  }
}