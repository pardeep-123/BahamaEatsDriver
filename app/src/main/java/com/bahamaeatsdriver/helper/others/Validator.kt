package com.bahamaeatsdriver.helper.others

import android.app.Activity
import com.bahamaeatsdriver.R
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validator {
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

    fun isNotNull(txt: String?): Boolean {
        return if (txt != null && txt.trim { it <= ' ' }.length > 0) true else false
    }

    /*  fun isValidPassword(password: String): Boolean {
          val PASSWORD_PATTERN = "^(?=\\D*\\d)(?=.*?[a-zA-Z]).*[\\W_].*$"
          val pattern = Pattern.compile(PASSWORD_PATTERN)
          val matcher = pattern.matcher(password)
          return !matcher.matches()
      }*/

    /****
     * Email Valid
     */
    fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                ")+"
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    /***
     * Login In Page validation
     */
    fun loginValid(context: Activity, email: String, password: String): Boolean {
        var check = false
        if (email.isEmpty() && password.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_all_fields))
        } else if (email.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_email))
        } else if (password.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_password))
        } else {
            check = true
        }
        return check
    }

    /***
     * Valid Password
     */
    fun isValidPassword(password: String?): Boolean {
        val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\W]{8,}$"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    /***
     * Sign Up Page validation
     **/
    fun signUpValid(context: Activity, fullname: String, email: String, password: String, confirmPassword: String, city: String, country: String, phone: String, imagePath: String,
                   dob:String,gender:String, isTermsCondition: Boolean): Boolean {
        var check = false
        if (fullname.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && city.isEmpty() && country.isEmpty() && phone.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_all_fields))
        } else if (imagePath.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_image_mandetory))
        }  else if (fullname.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_fullname))
        } else if (phone.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number))
        } else if (!phone.isEmpty() && phone.length <= 9) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number_limit))
        } else if (email.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_email))
        } else if (!email.isEmpty() && !isValidEmail(email)) {
            Helper.showErrorAlert(context, context.getString(R.string.please_enter_valid_email_address))
        } else if (country.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.please_enter_country))
        } else if (city.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_city))
        } else if (dob.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_dob))
        } else if (gender.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_gender))
        } else if (password.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_password))
        } else if (confirmPassword.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_confirm_password))
        } else if (!confirmPassword.isEmpty() && confirmPassword != password) {
            Helper.showErrorAlert(context, context.getString(R.string.error_mismatch_password))
        } else if (!isValidPassword(password)) {
//            Helper.showErrorAlert(context, context.getString(R.string.error_password_short_length))
            Helper.showErrorAlert(context, "Password contain at least 8 characters, contain at least one uppercase letter, contain at least one lowercase letter, contain at least one number")
        }/* else if (!password.isEmpty() && password.length <= 6) {
            Helper.showErrorAlert(context, context.getString(R.string.error_password_short_length))
        }*/ /*else if (imagePath.equals("")) {
            Helper.showErrorAlert(context, context.getString(R.string.error_image_mandetory))
        }*/ else if (!isTermsCondition) {
            Helper.showErrorAlert(
                    context,
                    context.getString(R.string.error_accpet_terms_conditions1)
            )
        } else {
            check = true
        }
        return check
    }


    fun emailValid(context: Activity, email: String): Boolean {
        var check = false
        if (email.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_email))
        } else if (!email.isEmpty() && !email.matches(EMAIL_REGEX.toRegex())) {
            Helper.showErrorAlert(context, context.getString(R.string.please_enter_valid_email_address))
        } else {
            check = true
        }

        return check
    }


    fun getOtpValid(context: Activity, phone: String, countryCode: String): Boolean {
        var check = false
        if (phone.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number))
        }
        if (countryCode.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number))
        } else if (countryCode.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_country_code))
        } else {
            check = true
        }

        return check
    }


    fun addLicenseValid(context: Activity, licenseType: String, licenseNo: String, dob: String, issueDate: String, expiryDate: String, nationality: String, vehicalColor: String, vehicalMake: String, vehicalModel: String): Boolean {
        var check = false
        if (licenseType.isEmpty() && licenseNo.isEmpty() && dob.isEmpty() && issueDate.isEmpty() && expiryDate.isEmpty() && nationality.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_details))
        } else if (licenseNo.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_license_no))
        } else if (dob.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_add_dob))
        } else if (issueDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_issuedate))
        } else if (expiryDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_expiry_date))
        } else if (nationality.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_naitionality))
        } else if (licenseType.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_type))
        } /*else if (vehicalModel.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_vehical_model))
        } else if (vehicalMake.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_vehical_make))
        } else if (vehicalColor.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_vehical_color))
        }*/ else {
            check = true
        }
        return check
    }


    fun addBankDetails(context: Activity, accountType: String, beneficiaryName: String, bankName: String, accountNumber: String, confirmAccountNumber: String, bankBranch: String): Boolean {
        var check = false
        if (accountType.isEmpty() && beneficiaryName.isEmpty() && beneficiaryName.isEmpty() && bankName.isEmpty() && accountNumber.isEmpty() && bankBranch.isEmpty()&& confirmAccountNumber.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_bank_details))
        } else if (beneficiaryName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_beneficiary_name))
        } else if (bankName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_bank_name))
        } else if (bankBranch.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_bank_brank_name))
        } else if (accountNumber.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_account_number))
        } else if (confirmAccountNumber.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_confirm_account_number))
        } else if (accountNumber.isNotEmpty()&&confirmAccountNumber.isNotEmpty()&&accountNumber!=confirmAccountNumber) {
            Helper.showErrorAlert(context, context.getString(R.string.error_mismatch_account_number))
        } else if (accountType.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_account_type))
        } else {
            check = true
        }
        return check
    }
    fun addPayoutDetails(
        context: Activity,
        firstName: String,
        lastName: String,
        dob: String,
        email: String,
        nib: String,
        confirmNib: String
    ): Boolean {
        var check = false
        if (firstName.isEmpty() && lastName.isEmpty() && dob.isEmpty() && dob.isEmpty() &&
            email.isEmpty() && nib.isEmpty()&& confirmNib.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_payout_details))
        } else if (firstName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_first_name))
        } else if (lastName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_last_name))
        } else if (dob.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_dob))
        } else if (email.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_email))
        } else if (nib.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_nib_number))
        } else if (confirmNib.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_mismatch_nib_number))
        } else if (nib.isNotEmpty()&&confirmNib.isNotEmpty()&&nib!=confirmNib) {
            Helper.showErrorAlert(context, context.getString(R.string.error_mismatch_nib_number))
        } else {
            check = true
        }
        return check
    }


    fun addIdCardDetailsValid(context: Activity, firstName: String, lastName: String, idNumber: String, issueDate: String, expiryDate: String, address: String): Boolean {
        var check = false
        if (firstName.isEmpty() && lastName.isEmpty() && idNumber.isEmpty() && issueDate.isEmpty() && expiryDate.isEmpty() && address.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_id_details))
        }else if (firstName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_firstname))
        } else if (lastName.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_lastname))
        } else if (idNumber.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_id_no))
        }  else if (issueDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_id_issuedate))
        } else if (expiryDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_id_expiry_date))
        } else if (address.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_address))
        } else {
            check = true
        }
        return check
    }


    fun editLicenseValid(context: Activity, licenseType: String, licenseNo: String, dob: String, issueDate: String, expiryDate: String, nationality: String): Boolean {
        var check = false
        if (licenseType.isEmpty() && licenseNo.isEmpty() && dob.isEmpty() && issueDate.isEmpty() && expiryDate.isEmpty() && nationality.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_id_details))
        } else if (licenseNo.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_license_no))
        } else if (dob.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_add_dob))
        } else if (issueDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_issuedate))
        } else if (expiryDate.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_expiry_date))
        } else if (nationality.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_naitionality))
        } else if (licenseType.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_license_type))
        } else {
            check = true
        }

        return check
    }


    /***
     * Sign Up Page validation
     **/
    fun editProfileUpValid(context: Activity, fullname: String, city: String, country: String, phone: String, dob: String, genderValue: String): Boolean {
        var check = false
        if (fullname.isEmpty() && city.isEmpty() && country.isEmpty() && phone.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_all_fields))
        } else if (fullname.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_fullname))
        } else if (phone.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number))
        } else if (!phone.isEmpty() && phone.length <= 9) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_phone_number_limit))
        } else if (country.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.please_enter_country))
        } else if (city.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_city))
        }  else if (dob.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_dob))
        }  else if (genderValue.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_gender))
        } else {
            check = true
        }
        return check
    }

    /***
     * Change passowrd validation
     */
    fun changePasswordValid(context: Activity, current: String, newPassword: String): Boolean {
        var check = false
        if (current.isEmpty() && newPassword.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_all_fields))
        } else if (current.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_current_passoword))
        } else if (newPassword.isEmpty()) {
            Helper.showErrorAlert(context, context.getString(R.string.error_empty_new_password))
        } else if (!isValidPassword(newPassword)) {
//            Helper.showErrorAlert(context, context.getString(R.string.error_password_short_length))
            Helper.showErrorAlert(context, "Password contain at least 8 characters, contain at least one uppercase letter, contain at least one lowercase letter, contain at least one number")

        } else {
            check = true
        }
        return check
    }
}