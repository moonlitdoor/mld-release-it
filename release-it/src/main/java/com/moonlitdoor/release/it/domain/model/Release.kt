package com.moonlitdoor.release.it.domain.model

import com.moonlitdoor.release.it.domain.entity.ReleaseEntity

data class Release(
  val id: Long,
  val name: String?,
  val draft: Boolean,
  val description: String?
) {
  companion object {
    fun from(entity: ReleaseEntity) = Release(
      id = entity.id,
      name = entity.name,
      draft = entity.draft,
      description = entity.description
    )
  }
}