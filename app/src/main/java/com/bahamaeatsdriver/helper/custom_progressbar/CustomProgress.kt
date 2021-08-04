package com.bahamaeatsdriver.helper.custom_progressbar;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.bahamaeatsdriver.R
import java.util.*

class CustomProgress : Dialog {
    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, theme: Int) : super(context, theme) {}

    companion object {

        fun create(
            context: Context,
            title: String
        ): CustomProgress {
            val dialog = CustomProgress(context)
            dialog.setTitle(title)
            dialog.setContentView(R.layout.dialog_progress)

            dialog.setCancelable(true)
            Objects.requireNonNull<Window>(dialog.getWindow()).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.gravity = Gravity.CENTER
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val lp = dialog.window!!.attributes
            lp.dimAmount = 0.2f
            dialog.window!!.attributes = lp
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
             dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.show()
            return dialog
        }
    }
}