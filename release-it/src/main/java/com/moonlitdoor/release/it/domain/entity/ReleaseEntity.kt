package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.graph.ReleaseGraph

@Entity(tableName = "releases", foreignKeys = [ForeignKey(entity = RepoEntity::class, parentColumns = ["id"], childColumns = ["repo_id"])])
data class ReleaseEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "repo_id")
  val repoId: Long,
  @ColumnInfo(name = "github_id")
  val githubId: String,
  val name: String?,
  val draft: Boolean,
  val description: String?
) {
  companion object {
    fun from(repoId: Long, graph: ReleaseGraph) = ReleaseEntity(
      repoId = repoId,
      githubId = graph.id,
      name = graph.name,
      draft = graph.draft,
      description = graph.description
    )
  }
}