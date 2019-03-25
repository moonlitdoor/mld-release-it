package com.moonlitdoor.release.it.domain.model

class Repositories(repositories: List<Repository>) : List<Repository> by repositories {

  companion object {
    fun from(list: List<Repository>) = Repositories(list)
  }
}