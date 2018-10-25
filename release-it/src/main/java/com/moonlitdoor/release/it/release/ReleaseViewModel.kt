package com.moonlitdoor.release.it.release

import android.app.Application
import com.moonlitdoor.release.it.component.BaseAndroidViewModel
import com.moonlitdoor.release.it.domain.repository.RepoRepository
import com.moonlitdoor.release.it.domain.service.RepoService
import com.moonlitdoor.release.it.extension.act
import com.moonlitdoor.release.it.extension.map

class ReleaseViewModel(application: Application, repoRepository: RepoRepository) : BaseAndroidViewModel(application) {

  val repos = repoRepository.repos.act {
    if (it.isEmpty()) RepoService.start(application)
  }

  val repoNames = repos.map { repos -> repos.map { it.name } }
}