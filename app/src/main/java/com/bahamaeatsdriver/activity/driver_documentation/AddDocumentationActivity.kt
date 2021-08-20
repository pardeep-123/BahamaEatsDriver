package com.bahamaeatsdriver.activity.driver_documentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Id_Details.Edit_Id_Details
import com.bahamaeatsdriver.activity.Licence_Detail.Edit_LicenceDetail_Activity
import com.bahamaeatsdriver.activity.login_register.Identification_Activity
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.model_class.delete_car_insurance.DeleteCarInsuranceResponse
import com.bahamaeatsdriver.model_class.delete_id_card_image.DeleteIdDetailsResponse
import com.bahamaeatsdriver.model_class.delete_police_record.DeletePoliceRecordResponse
import com.bahamaeatsdriver.model_class.driver_documentation_details.DriverDocumentaionResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_add_documentation.*

class AddDocumentationActivity : AppCompatActivity(), Observer<RestObservable> {
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var isAddedCarInsurace = ""
    private var isAddedPoliceRecord = ""
    private var isDriverLicence = ""
    private var isDriverIdDetails = ""
    private var isIdDetails = ""
    private var driverIdCardId = ""
    private var policeRecordImage = ""
    private var carInsuranceImage = ""
    private var isDriverIdCardBothImagesAvailable = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_documentation)

        ll_addDeletePoliceRecord.setOnClickListener {
            //  * type=1 for car insurance document
            // * type=2 for police record
            if (isAddedPoliceRecord.isEmpty())
                launchActivity<UploadDocumentActivity> {
                    putExtra("from", 2)
                    putExtra("image", policeRecordImage)
                } else {
                /***
                 * delete api will call here
                 */
                viewModel.deletePoliceRecordApi(this, true)
                viewModel.deletePoliceRecordResponse().observe(this, this)
            }
        }
        ll_deleteCarDocument.setOnClickListener {
//        * type=1 for car insurance document
//        * type=2 for police record
            if (isAddedCarInsurace.isEmpty())
                launchActivity<UploadDocumentActivity> {
                    putExtra("from", 1)
                    putExtra("image", carInsuranceImage)
                } else {
                /***
                 * delete api will call here
                 */
                viewModel.deleteCarInsuranceApi(this, true)
                viewModel.deleteCarInsuranceResponse().observe(this, this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        /*****
         * get documentation details
         */
        viewModel.getDocumentationDetailsApi(this, true)
        viewModel.getDocumentationDetails().observe(this, this)
    }

    fun goToBackScreen(view: View) {
        finish()
    }

    fun editDriverLicense(view: View) {
        launchActivity<Edit_LicenceDetail_Activity>()
        {
            putExtra("from", "2")
        }
    }

    fun editCarInsurance(view: View) {
        launchActivity<UploadDocumentActivity> {
            putExtra("from", 1)
            putExtra("image", carInsuranceImage)
        }
    }

    fun editPoliceRecord(view: View) {
        launchActivity<UploadDocumentActivity> {
            putExtra("from", 2)
            putExtra("image", policeRecordImage)
        }
    }


    fun addDeleteCardInsurance(view: View) {
//        * type=1 for car insurance document
//        * type=2 for police record
        if (isAddedCarInsurace.isEmpty())
            launchActivity<UploadDocumentActivity> {
                putExtra("from", 1)
                putExtra("image", carInsuranceImage)
            } else {
            /***
             * delete api will call here
             */
            viewModel.deleteCarInsuranceApi(this, true)
            viewModel.deleteCarInsuranceResponse().observe(this, this)
        }
    }

    fun deleteIdCardImages(view: View) {
        if (isDriverIdCardBothImagesAvailable.isEmpty()) {
            val intent = Intent(this@AddDocumentationActivity, Identification_Activity::class.java)
            intent.putExtra("Typeof", "Idproof")
            startActivity(intent)
        } else {
            /***
             * delete api will call here
             */
            viewModel.deleteDriverIdCardApi(this, driverIdCardId, true)
            viewModel.deleteDriverIdCardResponse().observe(this, this)
        }
    }

    fun addDeletePoliceRecord(view: View) {
        //  * type=1 for car insurance document
        // * type=2 for police record
        if (isAddedPoliceRecord.isEmpty())
            launchActivity<UploadDocumentActivity> {
                putExtra("from", 2)
                putExtra("image", policeRecordImage)
            } else {
            /***
             * delete api will call here
             */
            viewModel.deletePoliceRecordApi(this, true)
            viewModel.deletePoliceRecordResponse().observe(this, this)
        }
    }

    fun addIdCardRecord(view: View) {
        if (isIdDetails.isEmpty()) {
            val intent = Intent(this@AddDocumentationActivity, Identification_Activity::class.java)
            intent.putExtra("Typeof", "Idproof")
            startActivity(intent)
        } else {
            startActivity(Intent(this@AddDocumentationActivity, Edit_Id_Details::class.java))
        }

    }

    fun idCardRecord(view: View) {
        startActivity(Intent(this@AddDocumentationActivity, Edit_Id_Details::class.java))
    }


    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DeleteIdDetailsResponse) {
                    tv_isAppovedIdCard.text = getString(R.string.upload)
                    tv_isAppovedIdCard.visibility = View.VISIBLE
                    iv_driverIdCard.setImageResource(R.drawable.plus2)
                    iv_driverIdCard.isClickable = true
                    isDriverIdDetails = ""
                    isIdDetails = ""
                    isDriverIdCardBothImagesAvailable = ""
                    com.bahamaeatsdriver.helper.others.Helper.showSuccessToast(this, liveData.data.message)
                }
                if (liveData.data is DeleteCarInsuranceResponse) {
                    iv_editCarInsurance.visibility = View.GONE
                    tv_isApprrovedCar.text = getString(R.string.upload)
                    tv_isApprrovedCar.visibility = View.VISIBLE
                    iv_deleteCarDocument.setImageResource(R.drawable.plus2)
                    iv_deleteCarDocument.isClickable = true
                    ll_deleteCarDocument.isClickable = true
                    isAddedCarInsurace = ""
                    carInsuranceImage = ""
                    com.bahamaeatsdriver.helper.others.Helper.showSuccessToast(this, liveData.data.message)
                }
                if (liveData.data is DeletePoliceRecordResponse) {
                    iv_editPoliceRecord.visibility = View.GONE
                    tv_isAppovedPoliceRecord.text = getString(R.string.upload)
                    tv_isAppovedPoliceRecord.visibility = View.VISIBLE
                    iv_addDeletePoliceRecord.setImageResource(R.drawable.plus2)
                    ll_addDeletePoliceRecord.isClickable = true
                    iv_addDeletePoliceRecord.isClickable = true
                    isAddedPoliceRecord = ""
                    policeRecordImage = ""
                    com.bahamaeatsdriver.helper.others.Helper.showSuccessToast(this, liveData.data.message)
                }
                if (liveData.data is DriverDocumentaionResponse) {
                    //Check driver licence is added or not
                    if (liveData.data.body.licenseId != 0) {
                        isDriverLicence = "1"
                        editDriverLicence.visibility = View.VISIBLE
                        /**
                         * isDriverLicenseApproved==0 for not uploaded
                         * isDriverLicenseApproved==1 for approved
                         * isDriverLicenseApproved==2 for not apporved
                         */
                        if (liveData.data.body.isDriverLicenseApproved == 0) {
                            iv_driverLicenceApprove.setImageResource(R.drawable.plus2)
                            if (liveData.data.body.licenseFrontPhoto.isEmpty() || liveData.data.body.licenseBackPhoto.isEmpty()) {
                                tv_isApprrovedDriver.text = getString(R.string.upload)
                                iv_driverLicenceApprove.visibility = View.VISIBLE
                            } else {
                                tv_isApprrovedDriver.text = getString(R.string.in_review)
                                iv_driverLicenceApprove.visibility = View.INVISIBLE
                            }
                        } else if (liveData.data.body.isDriverLicenseApproved == 1) {
                            iv_driverLicenceApprove.setImageResource(R.drawable.green_tick)
                            tv_isApprrovedDriver.text = getString(R.string.approved)
                        } else if (liveData.data.body.isDriverLicenseApproved == 2) {
                            iv_driverLicenceApprove.setImageResource(R.drawable.close_icon)
                            tv_isApprrovedDriver.text = getString(R.string.upload_again)
                        }
                    } else {
                        editDriverLicence.visibility = View.GONE
                        tv_isApprrovedDriver.visibility = View.GONE
                    }
                    //Check driver car Insurance is added or not
                    if (liveData.data.body.carInsurance.isNotEmpty()) {
                        isAddedCarInsurace = "1"
                        iv_editCarInsurance.visibility = View.VISIBLE
                        carInsuranceImage = liveData.data.body.carInsurance
                        /**
                         * isCarInsuranceApproved==0 for not uploaded
                         * isCarInsuranceApproved==1 for approved
                         * isCarInsuranceApproved==2 for not apporved
                         */
                        if (liveData.data.body.isCarInsuranceApproved == 0) {
                            iv_deleteCarDocument.setImageResource(R.drawable.close_icon)
                            tv_isApprrovedCar.text = getString(R.string.in_review)
                            iv_deleteCarDocument.isClickable = true
                            ll_deleteCarDocument.isClickable = false
                        } else if (liveData.data.body.isCarInsuranceApproved == 1) {
                            iv_deleteCarDocument.setImageResource(R.drawable.green_tick)
                            iv_deleteCarDocument.isClickable = false
                            ll_deleteCarDocument.isClickable = false
                            tv_isApprrovedCar.text = getString(R.string.approved)
                        } else if (liveData.data.body.isCarInsuranceApproved == 2) {
                            iv_deleteCarDocument.setImageResource(R.drawable.close_icon)
                            tv_isApprrovedCar.text = getString(R.string.upload_again)
                        }
                    } else {
                        tv_isApprrovedCar.text = getString(R.string.upload)
                        iv_editCarInsurance.visibility = View.GONE
                        tv_isApprrovedCar.visibility = View.VISIBLE
                        iv_deleteCarDocument.isClickable = true
                        ll_deleteCarDocument.isClickable = true
                    }
                    //Check driver police record is added or not
                    if (liveData.data.body.policeRecord.isNotEmpty()) {
                        isAddedPoliceRecord = "1"
                        iv_editPoliceRecord.visibility = View.VISIBLE
                        policeRecordImage = liveData.data.body.policeRecord

                        /**
                         * isPoliceReportApproved==0 for not uploaded
                         * isPoliceReportApproved==1 for approved
                         * isPoliceReportApproved==2 for not apporved
                         */
                        if (liveData.data.body.isPoliceReportApproved == 0) {
                            iv_addDeletePoliceRecord.setImageResource(R.drawable.close_icon)
                            //                             iv_addDeletePoliceRecord.visibility=View.INVISIBLE
                            iv_addDeletePoliceRecord.isClickable = true
                            ll_addDeletePoliceRecord.isClickable = false
                            tv_isAppovedPoliceRecord.text = getString(R.string.in_review)
                        } else if (liveData.data.body.isPoliceReportApproved == 1) {
                            iv_addDeletePoliceRecord.setImageResource(R.drawable.green_tick)
                            iv_addDeletePoliceRecord.isClickable = false
                            ll_addDeletePoliceRecord.isClickable = false
                            tv_isAppovedPoliceRecord.text = getString(R.string.approved)
                        } else if (liveData.data.body.isPoliceReportApproved == 2) {
                            iv_addDeletePoliceRecord.setImageResource(R.drawable.close_icon)
                            tv_isAppovedPoliceRecord.text = getString(R.string.upload_again)
                            iv_addDeletePoliceRecord.isClickable = true
                        }
                    } else {
                        iv_editPoliceRecord.visibility = View.GONE
                        tv_isAppovedPoliceRecord.visibility = View.VISIBLE
                        tv_isAppovedPoliceRecord.text = getString(R.string.upload)
                        iv_addDeletePoliceRecord.isClickable = true
                        ll_addDeletePoliceRecord.isClickable = true
                    }
                    //Check driver Id card is added or not
                    if (liveData.data.body.cardId != 0) {
                        isIdDetails = "1"
                        driverIdCardId = liveData.data.body.cardId.toString()
                        iv_idCardRecord.visibility = View.VISIBLE
                        /**
                         * isDriverIdApproved==0 for not uploaded
                         * isDriverIdApproved==1 for approved
                         * isDriverIdApproved==2 for not apporved
                         */
                        if (liveData.data.body.isDriverIdApproved == 0) {
//                              iv_driverIdCard.setImageResource(R.drawable.plus2)
                            if (liveData.data.body.cardFrontPhoto.isEmpty() || liveData.data.body.cardBackPhoto.isEmpty()) {
                                tv_isAppovedIdCard.text = getString(R.string.upload)
                                iv_driverIdCard.visibility = View.VISIBLE
                                isDriverIdCardBothImagesAvailable = ""
                            } else {
                                isDriverIdCardBothImagesAvailable = "1"
                                tv_isAppovedIdCard.text = getString(R.string.in_review)
                                iv_driverIdCard.visibility = View.VISIBLE
                                iv_driverIdCard.isClickable = true
                                iv_driverIdCard.setImageResource(R.drawable.close_icon)
                            }
                        } else if (liveData.data.body.isDriverIdApproved == 1) {
                            iv_driverIdCard.setImageResource(R.drawable.green_tick)
                            iv_driverIdCard.isClickable = false
                            tv_isAppovedIdCard.text = getString(R.string.approved)
                            isDriverIdCardBothImagesAvailable = "1"
                        } else if (liveData.data.body.isDriverIdApproved == 2) {
                            iv_driverIdCard.setImageResource(R.drawable.close_icon)
                            tv_isAppovedIdCard.text = getString(R.string.upload_again)
                            iv_driverIdCard.visibility = View.VISIBLE
//                            iv_driverIdCard.isClickable = true
                            isDriverIdCardBothImagesAvailable = "1"
                        }
                    } else {
                        iv_idCardRecord.visibility = View.GONE
                        tv_isAppovedIdCard.visibility = View.VISIBLE
                        iv_driverIdCard.isClickable = true
                        tv_isAppovedIdCard.text = getString(R.string.upload)
                        isDriverIdCardBothImagesAvailable = ""
                    }

                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }
}