package com.moonlitdoor.release.it.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlitdoor.release.it.component.BaseAndroidViewModel
import com.moonlitdoor.release.it.domain.model.Release
import com.moonlitdoor.release.it.domain.model.Repo
import com.moonlitdoor.release.it.domain.repository.AuthRepository
import com.moonlitdoor.release.it.domain.repository.ReleaseRepository
import com.moonlitdoor.release.it.domain.repository.RepoRepository
import com.moonlitdoor.release.it.domain.service.GithubViewerService
import com.moonlitdoor.release.it.extension.and
import com.moonlitdoor.release.it.extension.map
import com.moonlitdoor.release.it.extension.switchMap

class RepositoryViewModel(application: Application, authRepository: AuthRepository, private val repoRepository: RepoRepository,
                          private val releaseRepository: ReleaseRepository) : BaseAndroidViewModel(application) {

  val authToken = authRepository.authToken

  private val repoId = MutableLiveData<Long>()

  var id: Long?
    get() = repoId.value
    set(value) {
      repoId.value = value
    }

  val repository: LiveData<Repo> = repoId.switchMap { id ->
    repoRepository.repo(id)
  }

  val releases: LiveData<List<Release>> = repoId.switchMap { id ->
    releaseRepository.releases(id)
  }

  private val repos = repoRepository.repos.and {
    if (it.isEmpty()) GithubViewerService.start(application)
  }

  val repoNames = repos.map { repos -> repos.groupBy { it.owner }.values }

}