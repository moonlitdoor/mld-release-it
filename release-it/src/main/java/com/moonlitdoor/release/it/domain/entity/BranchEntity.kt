package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Branch

@Entity(tableName = "branch",
    foreignKeys = [ForeignKey(entity = RepositoryEntity::class, parentColumns = ["id"], childColumns = ["repo_id"], onDelete = ForeignKey.CASCADE)])
class BranchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "repo_id", index = true)
    val repoId: Long,
    @ColumnInfo(name = "github_id")
    val githubId: String,
    val name: String?,
    @ColumnInfo(name = "target_id")
    val targetId: String,
    @ColumnInfo(name = "object_id")
    val objectId: String
) {
  companion object {
    fun from(repositoryId: Long, branch: Branch) = BranchEntity(
        repoId = repositoryId,
        githubId = branch.id,
        name = branch.name,
        targetId = branch.target.id,
        objectId = branch.target.oid
    )
  }
}