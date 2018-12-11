package com.moonlitdoor.release.it.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.moonlitdoor.release.it.component.BaseAndroidViewModel
import com.moonlitdoor.release.it.domain.model.Release
import com.moonlitdoor.release.it.domain.model.Repo
import com.moonlitdoor.release.it.domain.repository.ReleaseRepository
import com.moonlitdoor.release.it.domain.repository.RepoRepository
import com.moonlitdoor.release.it.domain.service.GithubService
import com.moonlitdoor.release.it.extension.and
import com.moonlitdoor.release.it.extension.map

class RepositoryViewModel(application: Application, private val repoRepository: RepoRepository, private val releaseRepository: ReleaseRepository) : BaseAndroidViewModel(application) {

  private val repoId = MutableLiveData<Long>()

  var id: Long?
    get() = repoId.value
    set(value) {
      repoId.value = value
    }

  val repository: LiveData<Repo> = Transformations.switchMap(repoId) { id ->
    repoRepository.repo(id)
  }

  val releases: LiveData<List<Release>> = Transformations.switchMap(repoId) { id ->
    releaseRepository.releases(id)
  }

  val repos = repoRepository.repos.and {
    if (it.isEmpty()) GithubService.start(application)
  }

  val repoNames = repos.map { repos -> repos.groupBy { it.owner }.values }

}