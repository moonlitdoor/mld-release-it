package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Release

@Entity(tableName = "releases", foreignKeys = [ForeignKey(entity = RepositoryEntity::class, parentColumns = ["id"], childColumns = ["repo_id"], onDelete = ForeignKey.CASCADE)])
data class ReleaseEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "repo_id", index = true)
  val repoId: Long,
  @ColumnInfo(name = "github_id")
  val githubId: String,
  val name: String?,
  val draft: Boolean,
  val description: String?
) {
  companion object {
    fun from(repositoryId: Long, graph: Release) = ReleaseEntity(
      repoId = repositoryId,
      githubId = graph.id,
      name = graph.name,
      draft = graph.isDraft,
      description = graph.description
    )
  }
}