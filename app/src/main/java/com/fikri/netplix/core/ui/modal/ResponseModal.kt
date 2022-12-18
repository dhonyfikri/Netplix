package com.fikri.netplix.core.ui.modal

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fikri.netplix.R

class ResponseModal : MyModal() {
    companion object {
        const val TYPE_GENERAL = "general"
        const val TYPE_ERROR = "error"
        const val TYPE_FAILED = "failed"
        const val TYPE_SUCCESS = "success"
        const val TYPE_MISTAKE = "mistake"
    }

    fun showResponseModal(
        context: Context,
        type: String = TYPE_GENERAL,
        message: String?,
        callback: (() -> Unit)? = null
    ) {
        dismiss()
        modal = Dialog(context)
        modal?.setContentView(R.layout.response_modal)
        val ivIllustration = modal?.findViewById<ImageView>(R.id.iv_illustration)
        val tvMessage = modal?.findViewById<TextView>(R.id.tv_message)
        val btnClose = modal?.findViewById<Button>(R.id.btn_close)

        ivIllustration?.setImageDrawable(
            when (type) {
                TYPE_GENERAL -> ContextCompat.getDrawable(context, R.drawable.il_emotion_general)
                TYPE_ERROR -> ContextCompat.getDrawable(context, R.drawable.il_emotion_confused)
                TYPE_FAILED -> ContextCompat.getDrawable(context, R.drawable.il_emotion_sad)
                TYPE_SUCCESS -> ContextCompat.getDrawable(context, R.drawable.il_emotion_happy)
                TYPE_MISTAKE -> ContextCompat.getDrawable(context, R.drawable.il_emotion_angry)
                else -> ContextCompat.getDrawable(context, R.drawable.il_emotion_general)
            }
        )
        tvMessage?.text = message

        btnClose?.setOnClickListener {
            callback?.invoke()
        }

        showModal()
    }
}