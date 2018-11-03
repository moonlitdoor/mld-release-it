package com.moonlitdoor.release.it.domain.model

import com.moonlitdoor.release.it.domain.entity.RepoEntity

data class Repo(
  val id: Long = 0,
  val userId: Long,
  val githubId: String,
  val owner: String,
  val name: String,
  val description: String?
) {

  companion object {
    fun from(entity: RepoEntity) = Repo(
      id = entity.id,
      userId = entity.userId,
      githubId = entity.githubId,
      owner = entity.owner,
      name = entity.name,
      description = entity.description
    )
  }
}