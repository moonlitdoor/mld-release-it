package com.moonlitdoor.release.it.domain.model

import com.moonlitdoor.release.it.domain.entity.RepoEntity

data class Repo(
  val id: Long = 0,
  val name: String
) {

  companion object {
    fun from(entity: RepoEntity) = Repo(
      entity.id,
      entity.name
    )
  }
}