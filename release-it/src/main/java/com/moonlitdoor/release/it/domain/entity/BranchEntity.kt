package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Branch

@Entity(tableName = "branch",
    foreignKeys = [ForeignKey(entity = RepositoryEntity::class, parentColumns = ["id"], childColumns = ["repository_id"],
        onDelete = ForeignKey.CASCADE)])
class BranchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "repository_id", index = true)
    val repositoryId: Long,
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
        repositoryId = repositoryId,
        githubId = branch.id,
        name = branch.name,
        targetId = branch.target.id,
        objectId = branch.target.oid
    )
  }
}