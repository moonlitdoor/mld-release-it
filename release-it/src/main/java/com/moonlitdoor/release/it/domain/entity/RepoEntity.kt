package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.graph.RepoGraph

@Entity(tableName = "repo", foreignKeys = [ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["user_id"])])
data class RepoEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "user_id", index = true)
  var userId: Long = 0,
  @ColumnInfo(name = "github_id")
  val githubId: String,
  val owner: String,
  val name: String,
  val description: String?
) {
  companion object {
    fun from(userId: Long, graph: RepoGraph) = RepoEntity(
      userId = userId,
      githubId = graph.id,
      owner = graph.owner,
      name = graph.name,
      description = graph.description
    )
  }
}