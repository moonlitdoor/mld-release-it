package com.moonlitdoor.release.it.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlitdoor.release.it.component.BaseAndroidViewModel
import com.moonlitdoor.release.it.domain.model.Release
import com.moonlitdoor.release.it.domain.model.Repository
import com.moonlitdoor.release.it.domain.repository.AuthRepository
import com.moonlitdoor.release.it.domain.repository.ReleaseRepository
import com.moonlitdoor.release.it.domain.repository.RepositoryRepository
import com.moonlitdoor.release.it.domain.service.GithubOrganizationService
import com.moonlitdoor.release.it.domain.service.GithubViewerService
import com.moonlitdoor.release.it.extension.and
import com.moonlitdoor.release.it.extension.map
import com.moonlitdoor.release.it.extension.switchMap

class RepositoryViewModel(application: Application, authRepository: AuthRepository, private val repositoryRepository: RepositoryRepository,
                          private val releaseRepository: ReleaseRepository) : BaseAndroidViewModel(application) {

  val authToken = authRepository.authToken

  private val repositoryId = MutableLiveData<Long>()

  var id: Long?
    get() = repositoryId.value
    set(value) {
      repositoryId.value = value
    }

  val repository: LiveData<Repository> = repositoryId.switchMap { id ->
    repositoryRepository.repository(id)
  }

  val releases: LiveData<List<Release>> = repositoryId.switchMap { id ->
    releaseRepository.releases(id)
  }

  private val repositories = repositoryRepository.repositories.and {
    if (it.isEmpty()) {
      GithubViewerService.start(application)
      GithubOrganizationService.start(application, "moonlitdoor")
    }
  }

  val repositoryNames = repositories.map { repositories -> repositories.groupBy { it.owner }.values }

}