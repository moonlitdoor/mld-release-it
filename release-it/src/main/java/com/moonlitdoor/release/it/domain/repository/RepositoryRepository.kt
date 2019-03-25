package com.moonlitdoor.release.it.domain.repository

import androidx.lifecycle.LiveData
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.model.Repositories
import com.moonlitdoor.release.it.domain.model.Repository
import com.moonlitdoor.release.it.extension.map

class RepositoryRepository(private val repositoryDao: RepositoryDao) {

  fun repository(id: Long): LiveData<Repository> = repositoryDao.getRepository(id).map { Repository.from(it) }

  val repositories: LiveData<Repositories> = repositoryDao.getRepositories().map { entities ->
    entities.map { Repository.from(it) }
  }.map { Repositories.from(it) }

}