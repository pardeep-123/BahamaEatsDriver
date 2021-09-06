package com.bahamaeatsdriver.activity.login_register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Id_Details.Id_details
import com.bahamaeatsdriver.activity.Licence_Detail.Fill_LicenseDetail_Activity
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_identification.*

class Identification_Activity : ImagePicker(), View.OnClickListener {

    private var Type_image: String? = null
    private var TypeofActivity = ""
    private var token = ""
    private var image_path = ""
    private var image_pathFront = ""
    private var image_pathBack = ""
    private lateinit var Tv_hedder: TextView
    private var Image_identification_Front: ImageView? = null
    private var Image2_identification_back: ImageView? = null
    private var temp = 2

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
        //Not in use
    }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        if (Type_image == "Front") {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(Image_identification_Front!!)
            image_pathFront = image_path
        } else {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(Image2_identification_back!!)
            image_pathBack = image_path
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        identificationActivity = this
        setContentView(R.layout.activity_identification)

        Tv_hedder = findViewById(R.id.Tv_hedder)
        Image_identification_Front = findViewById(R.id.Image_identification)
        Image2_identification_back = findViewById(R.id.Image2_identification_back)
        try {
            TypeofActivity = intent.getStringExtra("Typeof")!!
            token = intent.getStringExtra("token")!!
        } catch (e: Exception) {
        }

        if (TypeofActivity == "Idproof") {
            Tv_hedder.text = "ADD ID Details"
            Tv_upload.text=getString(R.string.upload_front_and_back_id_proof)
        } else if (TypeofActivity == "Licence") {
            Tv_hedder.text = "ADD License Details"
            Tv_upload.text=getString(R.string.upload_front_and_back_id_driver)
        }else if (TypeofActivity == "Normal") {
            Tv_upload.text=getString(R.string.upload_front_and_back_id_driver)
        }

        LL_UserCamera.setOnClickListener(this)
        image1_plus_front.setOnClickListener(this)
        LL_gallery.setOnClickListener(this)
        iv_backArrow_indentification.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        img_plus_back.setOnClickListener(this)

    }

    companion object {
        var identificationActivity: Identification_Activity? = null
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.LL_UserCamera -> {
                Type_image = if (temp % 2 == 0) {
                    "Front"
                } else {
                    "Back"
                }
                temp++
                checkPermissionCamera(false, "", "1")
            }

            R.id.LL_gallery -> {
                Type_image = if (temp % 2 == 0) {
                    "Front"
                } else {
                    "Back"
                }
                temp++
                checkPermissionCamera(false, "", "2")

            }
            R.id.image1_plus_front -> {
                Type_image = "Front"
                checkPermissionCamera(false, "", "")
            }
            R.id.img_plus_back -> {
                Type_image = "Back"
                checkPermissionCamera(false, "", "")
            }
            R.id.iv_backArrow_indentification -> {
                finish()
            }
            R.id.btn_continue -> {
                when (TypeofActivity) {
                    "Idproof" -> {
                        val intent = Intent(this@Identification_Activity, Id_details::class.java)
                        intent.putExtra("type", "Idproof")
                        intent.putExtra("frontImage", image_pathFront)
                        intent.putExtra("backImage", image_pathBack)
                        startActivity(intent)
                        finish()
                    }
                    "Licence" -> {
                        val intent = Intent(this@Identification_Activity, Fill_LicenseDetail_Activity::class.java)
                        intent.putExtra("type", "Licence")
                        intent.putExtra("frontImage", image_pathFront)
                        intent.putExtra("backImage", image_pathBack)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        val intent = Intent(this@Identification_Activity, Fill_LicenseDetail_Activity::class.java)
                        intent.putExtra("type", "Normal")
                        intent.putExtra("frontImage", image_pathFront)
                        intent.putExtra("backImage", image_pathBack)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}