package com.moonlitdoor.release.it.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity
import com.moonlitdoor.release.it.domain.graph.DataGraph

@Dao
abstract class ServiceDao {

  @Transaction
  open fun insert(data: DataGraph) {
//    val userId = insert(UserEntity.from(data.user))
//    data.repos.forEach { repo ->
//      val repoId = insert(RepositoryEntity.from(userId, repo))
//      repo.releases.forEach { release ->
//        insert(ReleaseEntity.from(repoId, release))
//      }
//    }
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  protected abstract fun insert(entity: UserEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  protected abstract fun insert(repository: RepositoryEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  protected abstract fun insert(release: ReleaseEntity)


}