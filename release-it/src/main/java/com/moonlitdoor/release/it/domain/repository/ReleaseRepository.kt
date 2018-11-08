package com.moonlitdoor.release.it.domain.repository

import androidx.lifecycle.LiveData
import com.moonlitdoor.release.it.domain.dao.ReleaseDao
import com.moonlitdoor.release.it.domain.model.Release
import com.moonlitdoor.release.it.extension.map

class ReleaseRepository(private val releaseDao: ReleaseDao) {

  fun releases(id: Long): LiveData<List<Release>> = releaseDao.get(id).map { list ->
    list.map {
      Release.from(it)
    }
  }
}