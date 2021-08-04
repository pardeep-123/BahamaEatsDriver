package com.bahamaeatsdriver.activity.driver_documentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bahamaeatsdriver.R

class EditDocumentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_document)
    }
    fun goToBackScreen(view: View) {
        finish()
    }
    fun btnContineClick(view: View) {
        finish()
    }
}