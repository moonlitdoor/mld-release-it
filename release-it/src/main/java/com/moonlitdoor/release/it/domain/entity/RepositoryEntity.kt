package com.moonlitdoor.release.it.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Repository

@Entity(tableName = "repository", foreignKeys = [ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class RepositoryEntity(
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
    fun from(userId: Long, owner: String, graph: Repository) = RepositoryEntity(
      userId = userId,
      githubId = graph.id,
      owner = owner,
      name = graph.name,
      description = graph.description
    )
  }
}
