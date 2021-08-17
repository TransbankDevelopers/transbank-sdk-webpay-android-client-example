package cl.transbank.sample

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.RuntimeException


class WebViewTbkClient : WebViewClient() {

    private val PREFIX = "intent://"
    private val EMPTY_STRING = ""
    private val FALLBACK_URL = "browser_fallback_url"
    private val TAG = "WebViewTbkClient"
    private val CONTEXT_ERROR_MESSAGE = "Se necesita el contexto para utilizar esta clase"
    private val FALLBACK_ERROR_MESSAGE = "Se necesita el parametro fallback_url en el Bundle del Intent"

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return if (isValidUrl(url)) {
            try {
                val context = getContextFromWebView(view)
                val intent = getIntentFromURL(url)
                view?.stopLoading()
                goToNewActivity(context, intent, view)
            } catch (e: Exception) {
                saveMessageLog(e.message ?: EMPTY_STRING)
            }
            true
        } else {
            false
        }
    }

    private fun getIntentFromURL(url: String?): Intent {
        return Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
    }

    private fun getContextFromWebView(view: WebView?): Context {
        return view?.context ?: throw RuntimeException(CONTEXT_ERROR_MESSAGE)
    }

    private fun goToNewActivity(context: Context, intent: Intent, view: WebView?) {
        val info : ResolveInfo? = obtainInfoFromIntent(context, intent)

        if (info != null) {
            context.startActivity(intent)
        } else {
            val fallbackUrl = intent.getStringExtra(FALLBACK_URL) ?: throw RuntimeException(FALLBACK_ERROR_MESSAGE)
            view?.loadUrl(fallbackUrl)
        }
    }

    private fun obtainInfoFromIntent(context: Context, intent: Intent): ResolveInfo? {
        return context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }

    private fun saveMessageLog(message: String) {
        Log.e(TAG, message)
    }

    private fun isValidUrl(url: String?): Boolean {
        return url?.startsWith(PREFIX) ?: false
    }

}