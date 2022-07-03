package kr.co.so.softcapus.chaptor8

import android.annotation.SuppressLint
import android.graphics.Bitmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val goHomeButton: ImageButton by lazy {
        findViewById(R.id.goHomebtn)
    }
    private val goBackbutton: ImageButton by lazy {
        findViewById(R.id.goBackBtn)
    }
    private val goForwardButton: ImageButton by lazy {
        findViewById(R.id.goForwardBtn)
    }

    private val addressBar: EditText by lazy {
        findViewById(R.id.adressBar)
    }

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }
    private val progressbar: ContentLoadingProgressBar by lazy {
     findViewById(R.id.progressbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        bindViews()
    }

    override fun onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack()
        }
        else super.onBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true




           // else Log.d("initView", "initView: fail")
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews() {

        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        addressBar.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = textView.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl)){
                    webView.loadUrl(loadingUrl)
                }else{
                    webView.loadUrl("http://$loadingUrl")
                }

            }
            return@setOnEditorActionListener false
        }
        goBackbutton.setOnClickListener{
            webView.goBack()
        }

        goForwardButton.setOnClickListener {
            webView.goForward()
        }
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }

    }

    inner class WebViewClient: android.webkit.WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressbar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressbar.hide()

            goBackbutton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()

            addressBar.setText(url)
        }
    }

    inner class WebChromeClient  :android.webkit.WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressbar.progress = newProgress
           // progressbar.hide()
        }
    }

    companion object{
        private const val DEFAULT_URL="http://www.google.com"
    }


}