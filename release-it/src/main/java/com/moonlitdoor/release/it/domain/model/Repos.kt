package com.moonlitdoor.release.it.domain.model

class Repos(repos: List<Repo>) : List<Repo> by repos {

  companion object {
    fun from(list: List<Repo>) = Repos(list)
  }
}