package com.moonlitdoor.release.it

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class WebServerRule(
    private val map: Map<String, (RecordedRequest) -> MockResponse>? = null,
    private val default: (RecordedRequest) -> MockResponse = { MockResponse().setResponseCode(HTTP_501_NOT_IMPLEMENTED) }
) : TestRule {

  private val server = MockWebServer()
  lateinit var host: String


  override fun apply(base: Statement, description: Description?): Statement {
    return object : Statement() {
      override fun evaluate() {
        description?.getAnnotation(WebServer::class.java)?.let { dispatch ->
          server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
              request?.let {
                return if (dispatch.value == "null" || map == null) default.invoke(it) else map[dispatch.value]?.invoke(it) ?: default.invoke(it)
              } ?: return MockResponse().setResponseCode(HTTP_501_NOT_IMPLEMENTED)
            }
          })
          server.start()
          host = server.url("").toString()
          base.evaluate()
          server.close()
        } ?: base.evaluate()
      }
    }
  }

  companion object {
    const val HTTP_501_NOT_IMPLEMENTED = 501

  }
}