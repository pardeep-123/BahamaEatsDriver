package com.bahamaeatsdriver.activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.activity.login_register.Identification_Activity
import com.bahamaeatsdriver.R
import java.io.File

class Documentation : AppCompatActivity() {
    var dialog: Dialog? = null
    private val mImageUri: Uri? = null
    private val sharedPreferences: SharedPreferences? = null
    var imgFile: File? = null
    var type: String? = null
    var Edit_details = ""
    var Tv_addidcard: TextView? = null
    var tv_add_licence: TextView? = null
    var Image_user: ImageView? = null
    var iv_edit_document: ImageView? = null
    var iv_idcard: ImageView? = null
    var iv_addlicence_icon: ImageView? = null
    var iv_addid_icon: ImageView? = null
    private val encodedImage: String? = null
    private val pictureFilePath: String? = null
    private val kk: String? = null
    private val strIMg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentationActivity = this
        setContentView(R.layout.activity_documentation)
        Image_user = findViewById(R.id.Image_user)
        iv_edit_document = findViewById(R.id.iv_edit)
        iv_idcard = findViewById(R.id.iv_idcard)
        Tv_addidcard = findViewById(R.id.Tv_addidcard)
        tv_add_licence = findViewById(R.id.tv_add_licence)
        iv_addid_icon = findViewById(R.id.iv_addid_icon)
        iv_addlicence_icon = findViewById(R.id.iv_addlicence_icon)
    }

    fun iv_backArrow_documents(view: View?) {
        onBackPressed()
        finish()
    }

    fun ll_idcard(view: View?) {
        val intent = Intent(this@Documentation, Identification_Activity::class.java)
        intent.putExtra("Typeof", "Idproof")
        startActivity(intent)
        finish()
    }

    fun LL_Licence(view: View?) {
        val intent = Intent(this@Documentation, Identification_Activity::class.java)
        intent.putExtra("Typeof", "Licence")
        startActivity(intent)
        finish()
    }

    companion object {
        var documentationActivity: Documentation? = null
    }
}