package com.bahamaeatsdriver.activity.login_register

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
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
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.CityAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.TermAnd_Conditions
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.getTokenPrefrence
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnCitySelection
import com.bahamaeatsdriver.model_class.get_city.Body
import com.bahamaeatsdriver.model_class.get_city.GetCityResponse
import com.bahamaeatsdriver.model_class.signup.SignUpResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_city_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Registration_Activity : ImagePicker(), View.OnClickListener, OnCitySelection, Observer<RestObservable> {
    private lateinit var dialog: Dialog
    private var mImageUri: Uri? = null

    private var encodedImage: String? = null
    private var pictureFilePath: String? = null
    private var kk: String? = null
    private var image_path = ""

    private var isTermsChecked = false
    private var isOpenDot = false
    private var popupWindow: PopupWindow? = null

    @Inject
    lateinit var validator: Validator

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        Glide.with(this).load(imagePath).into(iv_profileimage)
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
        //Not in use
    }

    private var countryCode = ""
    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        App.getinstance().getmydicomponent().inject(this)
        if (intent.getStringExtra("countryCode") != null && intent.getStringExtra("phone") != null) {
            countryCode = intent.getStringExtra("countryCode")!!
            phone = intent.getStringExtra("phone")!!
            et_phone.text = countryCode + phone
        }
        et_country.text = getString(R.string.countryName)
        rl_checkBox.setOnClickListener(this)
        btn_creataccount.setOnClickListener(this)
        iv_profileimage.setOnClickListener(this)
        tv_cityPicker.setOnClickListener(this)
        iv_backArrow.setOnClickListener(this)
    }


    fun tv_termcondition(view: View?) {
        launchActivity<TermAnd_Conditions>()
    }

    fun image() {
        dialog = Dialog(this@Registration_Activity)
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
                            Toast.makeText(this@Registration_Activity,
                                    "Photo file can't be created, please try again",
                                    Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (pictureFile != null) {
                            val photoURI = FileProvider.getUriForFile(this@Registration_Activity, com.bahamaeatsdriver.BuildConfig.APPLICATION_ID.toString() + ".provider", pictureFile)
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                    Toast.makeText(applicationContext, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.with(applicationContext)
                    .setPermissionListener(permissionlistener)
                    .setRationaleConfirmText("Permission")
                    .setRationaleTitle("Permission required.")
                    .setRationaleMessage("We need this permission to access camera and device storage.")
                    .setDeniedTitle("Permission denied")
                    .setDeniedMessage("Without this permission,receiver couldn't hear you.\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setGotoSettingButtonText("Settings")
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check()
        }
        gallery.setOnClickListener { openFileChooser() }
        cancel.setOnClickListener { dialog.dismiss() }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog.dismiss()
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                strIMg = convertImageToString(bitmap)
                iv_profileimage!!.setImageBitmap(bitmap)
                Log.e("catch_bitmap", "value: $strIMg")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val imgFile = File(pictureFilePath)
            val bmOptions = BitmapFactory.Options()
            var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath, bmOptions)
            bitmap = Bitmap.createScaledBitmap(bitmap, 700, 700, true)
            var ei: ExifInterface? = null
            try {
                ei = ExifInterface(pictureFilePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val orientation = ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED)

            var rotatedBitmap: Bitmap? = null
            rotatedBitmap = when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)

                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)

                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)

                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            try {
                rotatedBitmap = rotatedBitmap?.let { Bitmap.createScaledBitmap(it, 700, 700, true) }!!
                strIMg = convertimage(rotatedBitmap)

                iv_profileimage!!.setImageBitmap(bitmap)
            } catch (e: Exception) {
                dialog.dismiss()
            }
        }
    }
*/


    //    File imgFile = new File(pictureFilePath);
    fun convertimage(bit: Bitmap): String {
        val bout = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, 70, bout)
        val imagearray = bout.toByteArray()
        encodedImage = Base64.encodeToString(imagearray, Base64.DEFAULT)
        return encodedImage!!
    }

    @get:Throws(IOException::class)
    private val pictureFile: File
        private get() {
            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val pictureFile = "BusinessEvents$timeStamp"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(pictureFile, ".jpg", storageDir)
            pictureFilePath = image.absolutePath
            return image
        }

    fun convertImageToString(bit: Bitmap): String {
        val bout = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bout)
        val imagearray = bout.toByteArray()
        kk = (mImageUri.toString())
        kk = Base64.encodeToString(imagearray, Base64.DEFAULT)
        return kk!!
    }


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 1
        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                    matrix, true)
        }
    }

    override fun onClick(view: View?) {
        val itemid = view!!.id
        when (itemid) {
            R.id.btn_creataccount -> {
                /* launchActivity<Identification_Activity>()
                 {
                     putExtra("Typeof", "Normal")
                 }*/
                val fullname = et_username.text.toString().trim()
                val phone = et_phone.text.toString().trim()
                val email = et_email.text.toString().trim()
                val country = et_country.text.toString().trim()
                val city = tv_cityPicker.text.toString().trim()
                val password = et_password.text.toString().trim()
                val confirmPassword = et_confirmPassword.text.toString().trim()

                if (validator.signUpValid(this, fullname, email, password, confirmPassword, city, country, phone, image_path, isTermsChecked)) {
                    viewModel.signUpApi(this, fullname, email, password, phone, city, country, Constants.SIMPLE_LOGIN, Constants.ANDROID_DEVICE, getTokenPrefrence(Constants.DEVICE_TOKEN,""), image_path, true)
                    viewModel.getSignUpResponse().observe(this, this)
                }
            }
            R.id.iv_profileimage -> {
//                image()
                checkPermissionCamera(false, "1", "")

            }
            R.id.iv_backArrow -> {
                finish()
            }

            R.id.tv_cityPicker -> {
                /**
                 * 323:CityCode For Bahamas only which is by default
                 */
                viewModel.getCityApiCall(this, "323", true)
                viewModel.getCityApiResponse().observe(this, this)
            }

            R.id.rl_checkBox -> {
                isTermsChecked = if (!isTermsChecked) {
                    iv_tick!!.visibility = View.VISIBLE
                    true
                } else {
                    iv_tick!!.visibility = View.GONE
                    false
                }
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is SignUpResponse) {
                    if (liveData.data.body.isEmailAlreadyExist == 1) {
                        Helper.showSuccessToast(this, liveData.data.message)
                    } else {
                        if (liveData.data.body.isPhoneExist==0){
                            Helper.showSuccessToast(this, "Invalid phone number")
                        }else{
                        Helper.showSuccessToast(this, liveData.data.message)
                        launchActivity<Identification_Activity>()
                        {
                            putExtra("Typeof", "Normal")
                            putExtra("token", liveData.data.body.user.token)
                        }}
                    }
                }

                if (liveData.data is GetCityResponse) {
                    /*val dialog = Dialog(this@Registration_Activity)
                    dialog.setContentView(R.layout.custom_city_dialog)
                    dialog.setTitle("Select City")
                    val adapter = CityAdapter(this, liveData.data.body, this)
                    dialog.rv_cities.setAdapter(adapter)
                    dialog.show()*/
                    if (!isOpenDot) {
                        openFiltertBy(tv_cityPicker, liveData.data.body)
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
        tv_cityPicker.text = cityName
        isOpenDot = false
        popupWindow!!.dismiss()
    }

    fun openFiltertBy(ivSort: TextView?, body: List<Body>) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.custom_city_dialog, null)
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        val adapter = CityAdapter(this, body, this)
        customView!!.rv_cities.adapter = adapter
        popupWindow!!.showAsDropDown(ivSort)


    }

}