package com.bahamaeatsdriver.activity.driver_profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.CityAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.UpdateContactNumberActivity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnCitySelection
import com.bahamaeatsdriver.model_class.edit_driver_profile.EditDriverProfileResponse
import com.bahamaeatsdriver.model_class.get_city.Body
import com.bahamaeatsdriver.model_class.get_city.GetCityResponse
import com.bahamaeatsdriver.model_class.profile_details.DriverProfileDetailsResposne
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.custom_city_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Edit_profile : ImagePicker(), View.OnClickListener, Observer<RestObservable>, OnCitySelection {
    private lateinit var dialog: Dialog
    private var encodedImage: String? = null
    private var pictureFilePath: String? = null
    private var imageUrl = ""
    private var profileDetails: DriverProfileDetailsResposne? = null
    private var isOpenDot = false
    private var popupWindow: PopupWindow? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    @Inject
    lateinit var validator: Validator
    private var updatedCountry_Code = ""
    private var contact_Number = ""
    @SuppressLint("SetTextI18n")
    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
        updatedCountry_Code = updatedCountryCode
        contact_Number = contactNumber
        et_contactNumber.text = updatedCountryCode + contactNumber
    }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        imageUrl = imagePath!!
        Glide.with(this).load(imageUrl).placeholder(R.drawable.profileimage).into(iv_edit_profile!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        App.getinstance().getmydicomponent().inject(this)

        iv_edit_profile.setOnClickListener(this)
        tv_selectedCity.setOnClickListener(this)
        iv_backArrow_editprofile.setOnClickListener(this)
        et_contactNumber.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        if (intent.getSerializableExtra("profileDetails") != null) {
            profileDetails = (intent.getSerializableExtra("profileDetails") as DriverProfileDetailsResposne?)!!
            Glide.with(this).load(profileDetails!!.body.image).placeholder(R.drawable.profileimage).into(iv_edit_profile!!)
//            et_fullName.setText(profileDetails!!.body.fullName)
            if (profileDetails!!.body.fullName.isNotEmpty())
                et_fullName.setText(profileDetails!!.body.fullName)
            else
                et_fullName.setText(profileDetails!!.body.firstName + " " + profileDetails!!.body.lastName)
            et_contactNumber.text = profileDetails!!.body.countryCodePhone
            et_email.text = profileDetails!!.body.email
            et_country.text = getString(R.string.countryName)
            tv_selectedCity.text = profileDetails!!.body.city
            updatedCountry_Code = profileDetails!!.body.countryCode
            contact_Number = profileDetails!!.body.contactNo
        }

    }


    fun image() {
        dialog = Dialog(this@Edit_profile)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_photo)
        dialog.setCancelable(true)
        dialog.show()
        val take_photo = dialog.findViewById<LinearLayout>(R.id.take_photo)
        val gallery = dialog.findViewById<LinearLayout>(R.id.gallery)
        val cancel = dialog.findViewById<LinearLayout>(R.id.cancel)

        take_photo.setOnClickListener {
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    if (cameraIntent.resolveActivity(packageManager) != null) {

                        try {
                            pictureFile
                        } catch (ex: IOException) {
                            Toast.makeText(this@Edit_profile, "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (pictureFile != null) {
//                            val photoURI = FileProvider.getUriForFile(this@Edit_profile, BuildConfig.APPLICATION_ID + ".provider", pictureFile)
//                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                            startActivityForResult(cameraIntent, Registration_Activity.REQUEST_IMAGE_CAPTURE)
                        }
                    }
//                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                    val imageFileName = "JPEG_" + timeStamp + "_"
//                    try {
//                        createImageFile(this@Edit_profile, imageFileName, ".jpg")
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    val fileUri = FileProvider.getUriForFile(this@Edit_profile, APPLICATION_ID + ".provider", mImageFile)
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                    Toast.makeText(applicationContext, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.with(applicationContext).setPermissionListener(permissionlistener).setRationaleConfirmText("Permission").setRationaleTitle("Permission required.").setRationaleMessage("We need this permission to access camera and device storage.").setDeniedTitle("Permission denied").setDeniedMessage("Without this permission,receiver couldn't hear you.\n\nPlease turn on permissions at [Setting] > [Permission]").setGotoSettingButtonText("Settings").setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).check()
        }
        gallery.setOnClickListener { openFileChooser() }
        cancel.setOnClickListener { dialog.dismiss() }
    }

    @get:Throws(IOException::class)
    private val pictureFile: File
        get() {
            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val pictureFile = "BusinessEvents$timeStamp"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(pictureFile, ".jpg", storageDir)
            pictureFilePath = image.absolutePath
            return image
        }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
/*
    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String, extension: String) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mImageFile = File.createTempFile(
                name,
                extension,
                storageDir
        )
    }*/

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog.dismiss()
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                strIMg = convertImageToString(bitmap)
                iv_edit_profile!!.setImageBitmap(bitmap)
                Log.e("catch_bitmap", "value: $strIMg")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                imgFile = File(pictureFilePath)
                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(imgFile!!.absolutePath, bmOptions)
                strIMg = convertimage(bitmap)
                Log.e("cactch_bit", "bitmap : $strIMg")
                iv_edit_profile!!.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
*/


    //    File imgFile = new File(pictureFilePath);
    fun convertimage(bit: Bitmap): String? {
        val bout = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, 70, bout)
        val imagearray = bout.toByteArray()
        encodedImage = Base64.encodeToString(imagearray, Base64.DEFAULT)
        return encodedImage
    }



    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_edit_profile -> {
                checkPermissionCamera(false, "1", "")
            }

            R.id.tv_selectedCity -> {
                viewModel.getCityApiCall(this, "323", true)
                viewModel.getCityApiResponse().observe(this, this)
            }

            R.id.iv_backArrow_editprofile -> {
                finish()
            }
            R.id.et_contactNumber -> {
                val initData = Intent(this, UpdateContactNumberActivity::class.java)
                startActivityForResult(initData, pickPhoneNumberResultCode)
            }
            /**
             * API call
             */
            R.id.btn_update -> {
                val fullname = et_fullName.text.toString().trim()
                val country = et_country.text.toString().trim()
                val city = tv_selectedCity.text.toString().trim()
                if (validator.editProfileUpValid(this, fullname, city, country, contact_Number)) {
                    viewModel.editProfileApi(this, updatedCountry_Code, contact_Number, "", "", city, country, fullname, imageUrl, true)
                    viewModel.getEditProfileResponse().observe(this, this)
                }
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is EditDriverProfileResponse) {
                    if (liveData.data.code == 403) {
                        Helper.showErrorAlert(this, liveData.data.message)
                    } else {
                        Helper.showSuccessToast(this, liveData.data.message)
                        finish()
                    }

                }

                if (liveData.data is GetCityResponse) {
                    if (!isOpenDot) {
                        openFiltertBy(tv_selectedCity, liveData.data.body)
                        isOpenDot = true
                    } else {
                        if (popupWindow != null)
                            popupWindow!!.dismiss()
                        isOpenDot = false
                    }
                }
            }


            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    override fun onCitySelect(cityName: String, position: Int) {
        isOpenDot = false
        popupWindow!!.dismiss()
        tv_selectedCity.text = cityName

    }

    private fun openFiltertBy(ivSort: TextView?, body: List<Body>) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.custom_city_dialog, null)
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        val adapter = CityAdapter(this, body, this)
        customView!!.rv_cities.adapter = adapter
        popupWindow!!.showAsDropDown(ivSort)

    }

}