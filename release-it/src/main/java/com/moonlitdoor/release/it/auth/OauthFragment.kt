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
import androidx.navigation.fragment.NavHostFragment
import com.moonlitdoor.release.it.BuildConfig
import com.moonlitdoor.release.it.databinding.FragmentOauthBinding
import okhttp3.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.IOException

class OauthFragment : Fragment() {

  private val viewModel: AuthViewModel by inject()

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val binding = FragmentOauthBinding.inflate(inflater, container, false)
    CookieManager.getInstance().removeAllCookies { }

    binding.webView.settings.javaScriptEnabled = true
    binding.webView.webViewClient = object : WebViewClient() {

      override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url2 = request.url.toString()
        if (!url2.contains("?code=")) return false

        val CODE = url2.substring(url2.lastIndexOf("?code=") + 1)
        val token_code = CODE.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val tokenFetchedIs = token_code[1]
        val cleanToken = tokenFetchedIs.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val client = OkHttpClient()
        val url = HttpUrl.parse(GITHUB_OAUTH)!!.newBuilder()
        url.addQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
        url.addQueryParameter("client_secret", BuildConfig.GITHUB_CLIENT_SECRET)
        url.addQueryParameter("code", cleanToken[0])

        val url_oauth = url.build().toString()

        val request = Request.Builder()
          .header("Accept", "application/json")
          .url(url_oauth)
          .build()

        client.newCall(request).enqueue(object : Callback {
          override fun onFailure(call: Call, e: IOException) {

            Log.d(TAG, "IOException: " + e.message)


          }

          @Throws(IOException::class)
          override fun onResponse(call: Call, response: Response) {

            if (response.isSuccessful) {
              val JsonData = response.body()!!.string()
              val jsonObject = JSONObject(JsonData)
              val auth_token = jsonObject.getString("access_token")
              viewModel.setAuthToken(auth_token)
              NavHostFragment.findNavController(this@OauthFragment).navigateUp()
            }
          }
        })

        return false
      }
    }

    binding.webView.loadUrl(GITHUB_URL)
    return binding.root
  }

  companion object {

    private const val GITHUB_URL = "https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}&scope=repo,read:org"
    private const val GITHUB_OAUTH = "https://github.com/login/oauth/access_token"

    private val TAG = "github-oauth"
  }

}