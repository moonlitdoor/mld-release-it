package com.moonlitdoor.release.it.domain.repository

import androidx.lifecycle.LiveData
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.model.Repo
import com.moonlitdoor.release.it.domain.model.Repos
import com.moonlitdoor.release.it.extension.map

class RepoRepository(private val repositoryDao: RepositoryDao) {

  fun repo(id: Long): LiveData<Repo> = repositoryDao.getRepo(id).map { Repo.from(it) }

  val repos: LiveData<Repos> = repositoryDao.getRepos().map { entities ->
    entities.map { Repo.from(it) }
  }.map { Repos.from(it) }

}