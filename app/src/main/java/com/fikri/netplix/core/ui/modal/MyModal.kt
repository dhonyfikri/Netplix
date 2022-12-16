package com.fikri.netplix.core.ui.modal

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

open class MyModal {
    protected var modal: Dialog? = null

    protected fun showModal() {
        modal?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            show()
        }
    }

    fun dismiss() {
        if (modal != null && modal!!.isShowing) {
            modal?.dismiss()
        }
    }
}