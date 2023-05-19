package com.nic.newappwebview

import android.R
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.nic.newappwebview.databinding.PrograssBarDialogBinding


class CustomProgress {

private lateinit var binding: PrograssBarDialogBinding
    var customProgress: CustomProgress? = null
    private var mDialog: Dialog? = null
    var mProgressBar: ProgressBar? = null


    fun getInstance(): CustomProgress? {
        if (customProgress == null) {
            customProgress = CustomProgress()
        }
        return customProgress
    }

    fun showProgress(context: Context?, message: String, cancelable: Boolean) {

        mDialog = context?.let { Dialog(it) }
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
binding= PrograssBarDialogBinding.inflate(LayoutInflater.from(context))
        val view = binding.root
        mDialog!!.setContentView(view)

        binding.progressText.text = "" + message
        binding.progressText.visibility = View.VISIBLE
        binding.progressBar!!.visibility = View.VISIBLE
        binding.progressBar!!.isIndeterminate = true
        mDialog!!.setCancelable(cancelable)
        mDialog!!.setCanceledOnTouchOutside(cancelable)
        mDialog!!.show()
    }

    fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }
}