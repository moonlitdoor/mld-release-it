package com.moonlitdoor.release.it.domain.relation

import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity

data class UserRepoReleaseRelation(
  val userEntity: UserEntity,
  val repositoryEntities: MutableList<RepositoryEntity> = mutableListOf(),
  val releaseEntities: MutableMap<Long, ReleaseEntity> = mutableMapOf()
)