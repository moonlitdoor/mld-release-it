package com.moonlitdoor.release.it.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.moonlitdoor.release.it.BuildConfig
import com.moonlitdoor.release.it.HOST
import com.moonlitdoor.release.it.databinding.FragmentAuthBinding
import com.moonlitdoor.release.it.extension.observe
import okhttp3.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.IOException

class AuthFragment : Fragment() {

  private val viewModel: AuthViewModel by inject()
  private val host: String by inject(name = HOST)

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      FragmentAuthBinding.inflate(inflater, container, false).also {
        viewModel.authToken.observe(this) { token ->
          token?.let {
            findNavController().navigateUp()
          }
        }
        CookieManager.getInstance().removeAllCookies { }
        it.webView.settings.javaScriptEnabled = true
        it.webView.webViewClient = GithubWebViewClient()
        val s = "${host}login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}&scope=user:email,repo,read:user,read:org"
        it.webView.loadUrl(s)
      }.root

  private inner class GithubWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
      if (request.url.queryParameterNames.contains(QUERY_CODE)) {
        val code = request.url.getQueryParameter(QUERY_CODE)
        OkHttpClient().newCall(
            Request.Builder()
                .header(ACCEPT_HEADER, ACCEPT_VALUE)
                .url(
                    "${host}login/oauth/access_token?client_id=${BuildConfig.GITHUB_CLIENT_ID}&client_secret=${BuildConfig.GITHUB_CLIENT_SECRET}&code=$code")
                .build()
        ).enqueue(object : Callback {
          override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
              response.body()?.string().let {
                viewModel.setAuthToken(JSONObject(it).getString(ACCESS_TOKEN))
              }
            }
          }

          override fun onFailure(call: Call, e: IOException) {
            Log.d(LOG_TAG, e.message)
          }
        })
      }
      return false
    }
  }

  companion object {
    private const val QUERY_CODE = "code"
    private const val ACCESS_TOKEN = "access_token"
    private const val ACCEPT_HEADER = "Accept"
    private const val ACCEPT_VALUE = "application/json"

    private const val LOG_TAG = "AuthFragment"
  }

}