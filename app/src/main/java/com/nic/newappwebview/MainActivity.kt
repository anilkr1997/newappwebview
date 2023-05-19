package com.nic.newappwebview

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri

import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nic.newappwebview.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var url: String? = null

   

    private val URL = "https://themeatlas.nic.in/"
    private var isAlreadyCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        startLoaderAnimate()

     webview.settings.javaScriptEnabled = true
      webview.settings.setSupportZoom(false)

   webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                endLoaderAnimate()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                endLoaderAnimate()
                showErrorDialog("Error",
                    "No internet connection. Please check your connection.",
                    this@MainActivity)
            }
        }

       webview.loadUrl(URL)
    }

    override fun onResume() {
        super.onResume()

        if (isAlreadyCreated && !isNetworkAvailable()) {
            isAlreadyCreated = false
            showErrorDialog("Error", "No internet connection. Please check your connection.",
                this@MainActivity)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            this@MainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK &&    webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel") { _, _ ->
            this@MainActivity.finish()
        }
        dialog.setNeutralButton("Settings") { _, _ ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        dialog.setPositiveButton("Retry") { _, _ ->
            this@MainActivity.recreate()
        }
        dialog.create().show()
    }

    private fun endLoaderAnimate() {
      loaderImage.clearAnimation()
        loaderImage.visibility = View.GONE
    }

    private fun startLoaderAnimate() {
        val objectAnimator = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val startHeight = 170
                val newHeight = (startHeight + (startHeight + 40) * interpolatedTime).toInt()
                loaderImage.layoutParams.height = newHeight
                loaderImage.requestLayout()
            }

            override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
                super.initialize(width, height, parentWidth, parentHeight)
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        objectAnimator.repeatCount = -1
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.duration = 1000
        loaderImage.startAnimation(objectAnimator)
    }


}