package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Release

@Entity(tableName = "releases",
    foreignKeys = [ForeignKey(entity = RepositoryEntity::class, parentColumns = ["id"], childColumns = ["repo_id"], onDelete = ForeignKey.CASCADE)])
data class ReleaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "repo_id", index = true)
    val repoId: Long,
    @ColumnInfo(name = "github_id")
    val githubId: String,
    val name: String?,
    val draft: Boolean,
    val description: String?,
    @ColumnInfo(name = "tag_id")
    val tagId: String?,
    @ColumnInfo(name = "tag_name")
    val tagName: String?,
    @ColumnInfo(name = "target_id")
    val targetId: String?,
    @ColumnInfo(name = "object_id")
    val objectId: String?
) {
  companion object {
    fun from(repositoryId: Long, release: Release) = ReleaseEntity(
        repoId = repositoryId,
        githubId = release.id,
        name = release.name,
        draft = release.isDraft,
        description = release.description,
        tagId = release.tag?.id,
        tagName = release.tag?.name,
        targetId = release.tag?.target?.id,
        objectId = release.tag?.target?.oid
    )
  }
}