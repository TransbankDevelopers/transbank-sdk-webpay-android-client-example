package cl.transbank.sample


import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import cl.transbank.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //Esta URL debe ser la de la página que quieras mostrar
    private val URL_TEST = "https://google.cl"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWebView(binding.wvDynamicLink)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(webView: WebView) {
        webView.webViewClient = WebViewTbkClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.loadUrl(URL_TEST)
    }

    private fun destroyWebView(webView: WebView){
        CookieManager.getInstance().removeAllCookies(null)
        webView.clearCache(true)
        webView.webChromeClient = null
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyWebView(binding.wvDynamicLink)
    }

}
