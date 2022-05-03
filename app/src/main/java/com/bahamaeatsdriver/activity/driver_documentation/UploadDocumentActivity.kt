package com.bahamaeatsdriver.activity.driver_documentation

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.model_class.add_update_car_insurance.AddUpdateCarInsurance
import com.bahamaeatsdriver.model_class.add_update_police_record.AddUpdatePoliceRecord
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_upload_document.*

class UploadDocumentActivity : ImagePicker(), Observer<RestObservable> {


    /***
     * type=1 for car insurance document
     * type=2 for police record
     */
    private var type = 0
    private var image_path = ""
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }


    override fun uploadSlotsCodeFuncation() {
        //Not in use
    }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        Glide.with(this).load(image_path).placeholder(R.drawable.placeholder_rectangle)
            .into(Image_identification)
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
        //not in use
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_document)
        if (intent.getIntExtra("from", 0) != 0) {
            type = intent.getIntExtra("from", 0)
            if (intent.getStringExtra("image") != null && intent.getStringExtra("image")!!
                    .isNotEmpty()
            ) {
                image_path = intent.getStringExtra("image")!!
                Glide.with(this).load(image_path).placeholder(R.drawable.placeholder_rectangle)
                    .into(Image_identification)
            }
        }
//        type=1 for car insurance document
//        type=2 for police record
        if (type == 1) {
            tv_hedder.text = getString(R.string.upload_car_insurance)
            tv_upload.text = ""
        } else {
            tv_hedder.text = getString(R.string.upload_police_record)
            tv_upload.text = ""
        }
        onClickListners()
    }

    private fun onClickListners() {
        btn_upload.setOnClickListener {

            if (image_path.isEmpty()) {
                Helper.showErrorAlert(this, "Please upload image to upload")
            } else {
                /**
                 * Upload image here
                 */
                if (type == 1) {
                    viewModel.addUpdateCarInsuranceApi(this, image_path, true)
                    viewModel.getCarInsuranceResponse().observe(this, this)
                } else {
                    viewModel.addUpdatePoliceRecordApi(this, image_path, true)
                    viewModel.getPoliceRecordResponse().observe(this, this)
                }
            }
        }
        iv_backArrow_indentification.setOnClickListener {
            finish()
        }
        LL_UserCamera.setOnClickListener {
            checkPermissionCamera(false, "", "1")

        }
        LL_gallery.setOnClickListener {
            checkPermissionCamera(false, "", "2")
        }
        image1_plus_front.setOnClickListener {
            checkPermissionCamera(false, "", "")
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is AddUpdateCarInsurance) {
                    Helper.showSuccessToast(this, "Uploaded")
                    finish()
                }
                if (liveData.data is AddUpdatePoliceRecord) {
                    Helper.showSuccessToast(this, "Uploaded")
                    finish()
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

}