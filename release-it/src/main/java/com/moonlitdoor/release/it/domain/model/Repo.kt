package com.moonlitdoor.release.it.domain.model

import com.moonlitdoor.release.it.domain.entity.RepositoryEntity

data class Repo(
    val id: Long = 0,
    val ownerId: Long,
    val githubId: String,
    val owner: String,
    val name: String,
    val description: String?
) {

  companion object {
    fun from(entity: RepositoryEntity) = Repo(
      id = entity.id,
        ownerId = entity.ownerId,
      githubId = entity.githubId,
      owner = entity.owner,
      name = entity.name,
      description = entity.description
    )
  }
}