package com.fikri.netplix.core.ui.modal

import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fikri.netplix.R

class LoadingModal(private val context: Context) : MyModal() {
    companion object {
        const val TYPE_GENERAL = "general"
    }

    fun showLoadingModal(
        type: String = TYPE_GENERAL,
        message: String?
    ) {
        dismiss()
        modal = Dialog(context)
        modal?.setContentView(R.layout.common_loading)
        val ivIllustration = modal?.findViewById<ImageView>(R.id.iv_illustration)
        val tvMessage = modal?.findViewById<TextView>(R.id.tv_message)

        if (ivIllustration != null) {
            Glide.with(context).load(
                when (type) {
                    TYPE_GENERAL -> ContextCompat.getDrawable(context, R.drawable.il_loading)
                    else -> ContextCompat.getDrawable(context, R.drawable.il_loading)
                }
            ).into(ivIllustration)
        }
        tvMessage?.text = message

        showModal()
    }
}