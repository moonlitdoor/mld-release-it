package com.moonlitdoor.release.it.domain.repository

import androidx.lifecycle.LiveData
import com.moonlitdoor.release.it.domain.dao.RepoDao
import com.moonlitdoor.release.it.domain.model.Repo
import com.moonlitdoor.release.it.domain.model.Repos
import com.moonlitdoor.release.it.extension.map

class RepoRepository(repoDao: RepoDao) {

  val repos: LiveData<Repos> = repoDao.getRepos().map { entities ->
    entities.map { Repo.from(it) }
  }.map { Repos.from(it) }

}