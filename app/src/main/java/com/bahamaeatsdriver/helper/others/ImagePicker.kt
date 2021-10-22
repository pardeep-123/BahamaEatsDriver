package com.bahamaeatsdriver.helper.others

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bahamaeatsdriver.BuildConfig
import com.bahamaeatsdriver.R
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

abstract class ImagePicker : AppCompatActivity() {

    private val selectFile = 200
    private val requestCamera = 201

    companion object {
        val pickPhoneNumberResultCode = 1
        val uploadSlotcode = 101011
    }

    private val requestPermissionsCamera = 20
    private lateinit var mImageFile: File
    var isVideoEnable: Boolean = false
    private var from1 = ""
    private var forCamera = ""
    fun checkPermissionCamera(isVideoEnable: Boolean, from: String, isCamera: String) {
        from1 = from

        /***
         * if forCamera=1 open camera only
         * if forCamera=2 open gallery only
         */
        forCamera = isCamera
        this.isVideoEnable = isVideoEnable
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!cameraPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), requestPermissionsCamera
                )
                Helper.showSuccessToast(this, "You  can go to settings to allow permission")
                return
            } else {
//                if (from1.equals("1")) {
                selectImage(isVideoEnable)
//                }
            }
        } else {
//            if (from1.equals("1")) {
            selectImage(isVideoEnable)
//            }
        }

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        var permissionDeny = PackageManager.PERMISSION_DENIED
        for (permission in grantResults) {
            permissionCheck += permission
        }

        if (grantResults.isNotEmpty() && permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            if (from1.equals("1")) {
            selectImage(isVideoEnable)
//            }
        }
    }

    private fun cameraPermission(permissions: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                this,
                permissions[2]
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun selectImage(isVideoEnable: Boolean) {
        if (isVideoEnable) {
            if (forCamera.equals("")) {
                openPickerDailog()
            } else if (forCamera.equals("1")) {
                cameraIntent()
            } else if (forCamera.equals("2")) {
                galleryIntent()
            }

        } else {
            if (forCamera.equals("")) {
                openPickerDailog()
            } else if (forCamera.equals("1")) {
                cameraIntent()
            } else if (forCamera.equals("2")) {
                galleryIntent()
            }
        }
    }

    private fun openPickerDailog() {
        val dialog = Dialog(this)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_photo)
        dialog.setCancelable(true)
        dialog.show()
        val take_photo = dialog.findViewById<LinearLayout>(R.id.take_photo)
        val gallery = dialog.findViewById<LinearLayout>(R.id.gallery)
        val cancel = dialog.findViewById<LinearLayout>(R.id.cancel)
        take_photo.setOnClickListener {
            cameraIntent()
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            galleryIntent()
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun cameraIntent() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        try {
            createImageFile(this, imageFileName, ".jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mImageFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, requestCamera)
    }

    private fun galleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, selectFile)
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String, extension: String) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mImageFile = File.createTempFile(
                name,
                extension,
                storageDir
        )
    }

    /*   /* @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (requestCode == LOCATION_REQUEST) {
                    if ( data!=null&& data.getSerializableExtra("selectedaddress")!=null){
                        val selectedAddress = data.getSerializableExtra("selectedaddress") as SelectAddressModel
                        et_country.text = selectedAddress.getcountry()
                        et_island.text = selectedAddress.state
                        et_city.text = selectedAddress.city
                        et_address.text = selectedAddress.address
                        latitude = selectedAddress.latitude.toString()
                        longitude = selectedAddress.longitude.toString()
                    }
                }
            }
        }
    }*/*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = CropImage.getActivityResult(data)

        if (resultCode == pickPhoneNumberResultCode) {
            val phoneNumber = data!!.getStringExtra("phone")
            val updatedCountryCode = data.getStringExtra("updatedCountryCode")
            getUpdatedPhoneNoAfterVerify(phoneNumber!!, updatedCountryCode!!)
        }else if (requestCode==uploadSlotcode){
            uploadSlotsCodeFuncation()
        }
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                /* pickPhoneNumberResultCode -> {
                     val phoneNumber = data!!.getStringExtra("phone")
                     getUpdatedPhoneNoAfterVerify(phoneNumber)
                 }*/
                requestCamera -> {
                    val uri = Uri.fromFile(mImageFile)
                    val picturePath = Helper.getAbsolutePath(this, uri)
//                    selectedImage(picturePath, "")
                    // start cropping activity for pre-acquired image saved on the device
                    if (from1.equals("1")) {
                        CropImage.activity(uri).setAspectRatio(70, 70).start(this)
                    } else {
                        selectedImage(picturePath, "")
                    }
                }
                selectFile -> {
                    val uri = data?.data
                    val picturePath = Helper.getAbsolutePath(this, uri!!)
//                    selectedImage(picturePath, "")
                    if (from1.equals("1")) {
                        CropImage.activity(uri).setAspectRatio(70, 70).start(this)
                    } else {
                        selectedImage(picturePath, "")
                    }
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val resultUri = result.uri
                    val picturePath = Helper.getAbsolutePath(this, resultUri)
                    selectedImage(picturePath, "")

                }
            }
        }
    }

    abstract fun uploadSlotsCodeFuncation()
    abstract fun selectedImage(imagePath: String?, thumbnailVideoPath: String)
    abstract fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String)
}