package com.moonlitdoor.release.it.domain.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moonlitdoor.release.it.domain.query.Organization
import com.moonlitdoor.release.it.domain.query.Viewer

@Entity(tableName = "owner", indices = [Index(value = ["github_id"], unique = true)])
class OwnerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "github_id")
    val githubId: String,
    val login: String,
    val name: String?,
    val email: String,
    val avatarUrl: Uri,
    val isUser: Boolean
) {

  companion object {
    fun from(viewer: Viewer) = OwnerEntity(
        githubId = viewer.id,
        login = viewer.login,
        name = viewer.name,
        email = viewer.email,
        avatarUrl = viewer.avatarUrl,
        isUser = true
    )

    fun from(viewer: Organization) = OwnerEntity(
        githubId = viewer.id,
        login = viewer.login,
        name = viewer.name,
        email = viewer.email,
        avatarUrl = viewer.avatarUrl,
        isUser = false
    )
  }

}