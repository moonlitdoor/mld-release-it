package com.moonlitdoor.release.it

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class HostRule : TestRule {
  override fun apply(base: Statement, description: Description?): Statement {
    val a = ApplicationProvider.getApplicationContext<App>()
    return base
  }
}