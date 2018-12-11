package com.moonlitdoor.release.it.domain.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Viewer
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
  val avatarUrl: Uri
) {
  companion object {
    fun from(graph: Viewer) = UserEntity(
      githubId = graph.id,
      login = graph.login,
      name = graph.name,
      email = graph.email,
      created = ZonedDateTime.parse(graph.createdAt),
      avatarUrl = Uri.parse(graph.avatarUrl)
    )

  }
}